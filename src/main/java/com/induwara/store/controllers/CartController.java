package com.induwara.store.controllers;

import com.induwara.store.dtos.AddItemToCartRequest;
import com.induwara.store.dtos.CartDto;
import com.induwara.store.dtos.CartItemDto;
import com.induwara.store.dtos.UpdateCartItemRequest;
import com.induwara.store.exceptions.CartNotFoundException;
import com.induwara.store.exceptions.ProductNotFoundException;
import com.induwara.store.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Cart")
public class CartController  {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder){
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("carts").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID  cartId,
            @RequestBody AddItemToCartRequest request
            ){
            var cartItemDto = cartService.addToCart(cartId, request.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId){
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
            ) {

        return cartService.updateCart(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        cartService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{cartId}/items")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId){
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart was not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Product was not found in the cart")
        );
    }

}
