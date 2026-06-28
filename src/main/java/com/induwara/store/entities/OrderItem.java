package com.induwara.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column( name = "unit_price")
    private BigDecimal unitPrice;

    @Column( name = "total_price")
    private BigDecimal totalPrice;

    @Column( name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn( name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn( name = "product_id")
    private Product product;
}
