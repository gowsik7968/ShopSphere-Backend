package com.shopsphere.service;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Product;
import com.shopsphere.repository.OrderItemRepository;
import com.shopsphere.repository.OrderRepository;
import com.shopsphere.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }


    // =========================================================
    // CREATE ORDER
    // =========================================================

    public Order createOrder(Order order) {

        if (order.getStatus() == null ||
                order.getStatus().isBlank()) {

            order.setStatus("PLACED");
        }

        if (order.getOrderDate() == null) {

            order.setOrderDate(
                    java.time.LocalDateTime.now()
            );
        }

        return orderRepository.save(order);
    }


    // =========================================================
    // GET ALL ORDERS
    // ADMIN ONLY
    // =========================================================

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }


    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        ));
    }


    // =========================================================
    // GET USER ORDER BY ID
    // OWNER ONLY
    // =========================================================

    public Order getOrderByIdAndEmail(
            Long id,
            String email) {

        return orderRepository
                .findByIdAndEmail(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        ));
    }


    // =========================================================
    // GET USER'S ORDERS
    // =========================================================

    public List<Order> getOrdersByEmail(
            String email) {

        return orderRepository.findByEmail(email);
    }


    // =========================================================
    // ADMIN GET ANY ORDER
    // =========================================================

    public Order getOrderByIdForAdmin(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        ));
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // ADMIN ONLY
    // =========================================================

    public Order updateOrderStatus(
            Long id,
            String status) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                ));

        if (status == null ||
                !isValidStatus(status)) {

            throw new RuntimeException(
                    "Invalid order status"
            );
        }

        order.setStatus(status.toUpperCase());

        return orderRepository.save(order);
    }


    // =========================================================
    // VALIDATE ORDER STATUS
    // =========================================================

    private boolean isValidStatus(
            String status) {

        String value = status.toUpperCase();

        return "PLACED".equals(value)
                || "PROCESSING".equals(value)
                || "SHIPPED".equals(value)
                || "DELIVERED".equals(value)
                || "CANCELLED".equals(value);
    }


    // =========================================================
    // CREATE ORDER ITEM
    // USER OWNERSHIP CHECK
    // =========================================================

    public OrderItem createOrderItem(
            OrderItem orderItem,
            String email) {

        // Make sure this order belongs to the logged-in user
        orderRepository
                .findByIdAndEmail(
                        orderItem.getOrderId(),
                        email
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        ));

        if (orderItem.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        // Get actual product
        Product product =
                productRepository
                        .findById(orderItem.getProductId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        // Never trust price/product name from frontend
        orderItem.setProductName(
                product.getName()
        );

        orderItem.setPrice(
                product.getPrice()
        );

        // Calculate total on backend
        double total =
                product.getPrice()
                        * orderItem.getQuantity();

        orderItem.setTotal(total);

        return orderItemRepository.save(orderItem);
    }


    // =========================================================
    // GET USER ORDER ITEMS
    // OWNER ONLY
    // =========================================================

    public List<OrderItem>
    getOrderItemsByOrderIdAndEmail(
            Long orderId,
            String email) {

        orderRepository
                .findByIdAndEmail(
                        orderId,
                        email
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        ));

        return orderItemRepository
                .findByOrderId(orderId);
    }


    // =========================================================
    // GET ANY ORDER ITEMS
    // ADMIN ONLY
    // =========================================================

    public List<OrderItem>
    getOrderItemsByOrderId(
            Long orderId) {

        orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        ));

        return orderItemRepository
                .findByOrderId(orderId);
    }
}
