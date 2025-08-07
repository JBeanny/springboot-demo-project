package com.beanny.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "qty")
    private Integer quantity;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
