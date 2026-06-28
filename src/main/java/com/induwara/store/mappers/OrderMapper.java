package com.induwara.store.mappers;

import com.induwara.store.dtos.OrderDto;
import com.induwara.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
