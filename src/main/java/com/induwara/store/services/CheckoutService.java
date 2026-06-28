package com.induwara.store.services;

import com.induwara.store.dtos.CheckoutRequest;
import com.induwara.store.dtos.CheckoutResponse;
import com.induwara.store.entities.Order;
import com.induwara.store.exceptions.CartEmptyException;
import com.induwara.store.exceptions.CartNotFoundException;
import com.induwara.store.repositories.CartRepository;
import com.induwara.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final AuthService authService;

    public CheckoutResponse checkoutResponse(CheckoutRequest request){
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if( cart == null){
            throw new CartNotFoundException();
        }

        if(cart.isCartEmpty()){
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);
        cartService.deleteCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
