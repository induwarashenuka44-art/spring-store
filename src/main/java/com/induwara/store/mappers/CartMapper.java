package com.induwara.store.mappers;

import com.induwara.store.dtos.CartDto;
import com.induwara.store.dtos.CartItemDto;
import com.induwara.store.entities.Cart;
import com.induwara.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  CartMapper {
        @Mapping(target = "totalPrice" , expression = "java(cart.getTotalPrice())")
        CartDto toDto(Cart cart);

        @Mapping(target="totalPrice", expression = "java(cartItem.getTotalPrice())")
        CartItemDto toDto(CartItem cartItem);
}
