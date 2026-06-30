package com.induwara.store.services;

import com.induwara.store.dtos.CheckoutRequest;
import com.induwara.store.dtos.CheckoutResponse;
import com.induwara.store.entities.Order;
import com.induwara.store.exceptions.CartEmptyException;
import com.induwara.store.exceptions.CartNotFoundException;
import com.induwara.store.exceptions.PaymentException;
import com.induwara.store.repositories.CartRepository;
import com.induwara.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkoutResponse(CheckoutRequest request) throws PaymentException {
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
            var checkoutSession = paymentGateway.createCheckoutSession(order);
            cartService.deleteCart(cart.getId());

            return new CheckoutResponse(order.getId(), checkoutSession.getCheckoutUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }

    }
}
