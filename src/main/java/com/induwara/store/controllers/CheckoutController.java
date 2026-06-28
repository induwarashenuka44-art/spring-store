package com.induwara.store.controllers;

import com.induwara.store.dtos.CheckoutRequest;
import com.induwara.store.dtos.CheckoutResponse;
import com.induwara.store.entities.Order;
import com.induwara.store.entities.OrderItem;
import com.induwara.store.entities.OrderStatus;
import com.induwara.store.repositories.CartRepository;
import com.induwara.store.repositories.OrderRepository;
import com.induwara.store.services.AuthService;
import com.induwara.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping ("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request
            ){
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if( cart == null){
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cart not found")
            );
        }

        if(cart.getItems().isEmpty()){
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cart is empty ")
            );
        }

        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });
        
        orderRepository.save(order);

        cartService.deleteCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
