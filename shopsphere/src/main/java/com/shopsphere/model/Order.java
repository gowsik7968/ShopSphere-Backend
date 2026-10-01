package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private String fullName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private Double subtotal;

    private Double deliveryCharge;

    private Double total;

    private String status;

    private LocalDateTime orderDate;
}