package com.induwara.store.orders;

import com.induwara.store.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
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

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "product_id")
    private Product product;

    public OrderItem(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

    }
}
