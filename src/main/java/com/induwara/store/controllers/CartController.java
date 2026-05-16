package com.induwara.store.controllers;

import com.induwara.store.dtos.AddItemToCartRequest;
import com.induwara.store.dtos.CartDto;
import com.induwara.store.dtos.CartItemDto;
import com.induwara.store.dtos.UpdateCartItemRequest;
import com.induwara.store.entities.Cart;
import com.induwara.store.entities.CartItem;
import com.induwara.store.mappers.CartMapper;
import com.induwara.store.repositories.CartRepository;
import com.induwara.store.repositories.ProductRepository;
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
public class CartController  {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder){
         var cart = new Cart();
         cartRepository.save(cart);

         var cartDto = cartMapper .toDto(cart);
//         return new ResponseEntity<>(cartDto, HttpStatus.CREATED);

        var uri = uriBuilder.path("carts").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID  cartId,
            @RequestBody AddItemToCartRequest request
            ){
            var cart = cartRepository.getCartWithItems(cartId).orElse(null);
            if(cart == null){
                return ResponseEntity.notFound().build();
            }

            var product = productRepository.findById(request.getProductId()).orElse(null);
            if(product == null){
                return ResponseEntity.badRequest().build();
            }

            var cartItem = cart.addItem(product);
            cartRepository.save(cart);
            var cartItemDto = cartMapper.toDto(cartItem);

            return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.notFound().build();
        }

        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
            ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart was not found")
            );
        }

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product was not found in the cart")
            );
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.ok().body(cartItemDto);

    }

    @DeleteMapping("{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart was not found")
            );
        } else {
            cart.removeItem(productId);
        }

        cartRepository.save(cart);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("{cartId}/items")
    public ResponseEntity<?> deleteCart(@PathVariable UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart was not found")
            );
        } else {
           cart.clearCart();
        }

        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }

}
