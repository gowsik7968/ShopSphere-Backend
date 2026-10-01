package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Double price;
    private int quantity;
    private Double total;
}
