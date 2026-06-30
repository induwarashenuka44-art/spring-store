package com.induwara.store.services;

import com.induwara.store.dtos.OrderDto;
import com.induwara.store.exceptions.OrderNotFoundException;
import com.induwara.store.mappers.OrderMapper;
import com.induwara.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(){
        var orders = orderRepository.findAllByCustomerId(authService.getCurrentUser().getId());
        return orders.stream().map(orderMapper::toDto)
                .toList();
    }

    public OrderDto getSingleOrder(Long orderId) {
        System.out.print("orderId" + orderId);
        var order = orderRepository.findOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if(!order.isPlacedBy(authService.getCurrentUser())){
            throw new AccessDeniedException("You don't have access to this order");
        }

        return orderMapper.toDto(order);
    }

}
