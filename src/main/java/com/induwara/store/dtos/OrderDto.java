package com.induwara.store.dtos;

import com.induwara.store.entities.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items = new ArrayList<>();

}
