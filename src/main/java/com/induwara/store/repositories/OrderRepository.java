package com.induwara.store.repositories;

import com.induwara.store.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findAllByCustomerId(Long customerId);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findOrderById(Long orderId);
}