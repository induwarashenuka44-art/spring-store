package com.induwara.store.services;

import com.induwara.store.dtos.CheckoutRequest;
import com.induwara.store.dtos.CheckoutResponse;
import com.induwara.store.entities.Order;
import com.induwara.store.exceptions.CartEmptyException;
import com.induwara.store.exceptions.CartNotFoundException;
import com.induwara.store.repositories.CartRepository;
import com.induwara.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final AuthService authService;

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Transactional
    public CheckoutResponse checkoutResponse(CheckoutRequest request) throws StripeException {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if( cart == null){
            throw new CartNotFoundException();
        }

        if(cart.isCartEmpty()){
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        try {
            //Create a checkout session
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel");

            order.getItems().forEach(item -> {
                var lineitem = SessionCreateParams.LineItem.builder()
                        .setQuantity((long) item.getQuantity())
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();
                builder.addLineItem(lineitem);
            });

            var session = Session.create(builder.build());
            cartService.deleteCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getUrl());
        } catch (StripeException ex) {
            System.out.println(ex.getMessage());
            orderRepository.delete(order);
            throw ex;
        }

    }
}
