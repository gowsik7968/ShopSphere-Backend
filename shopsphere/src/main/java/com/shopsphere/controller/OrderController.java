package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService = orderService;
    }


    // =========================================================
    // CREATE ORDER
    // =========================================================

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody Order order,
            Authentication authentication) {

        String loggedInEmail =
                authentication.getName();

        // Always use the email from JWT
        order.setEmail(loggedInEmail);

        Order savedOrder =
                orderService.createOrder(order);

        return new ResponseEntity<>(
                savedOrder,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET ALL ORDERS
    // ADMIN ONLY
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        List<Order> orders =
                orderService.getAllOrders();

        return new ResponseEntity<>(
                orders,
                HttpStatus.OK
        );
    }


    // =========================================================
    // GET LOGGED-IN USER'S ORDERS
    // =========================================================

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(
            Authentication authentication) {

        String email =
                authentication.getName();

        List<Order> orders =
                orderService.getOrdersByEmail(
                        email
                );

        return new ResponseEntity<>(
                orders,
                HttpStatus.OK
        );
    }


    // =========================================================
    // GET USER'S ORDER BY ID
    // OWNER ONLY
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long id,
            Authentication authentication) {

        String email =
                authentication.getName();

        Order order =
                orderService.getOrderByIdAndEmail(
                        id,
                        email
                );

        return new ResponseEntity<>(
                order,
                HttpStatus.OK
        );
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // ADMIN ONLY
    // =========================================================

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Order updatedOrder =
                orderService.updateOrderStatus(
                        id,
                        status
                );

        return new ResponseEntity<>(
                updatedOrder,
                HttpStatus.OK
        );
    }


    // =========================================================
    // ADMIN GET ANY ORDER BY ID
    // =========================================================

    @GetMapping("/admin/{id}")
    public ResponseEntity<Order> getOrderByIdForAdmin(
            @PathVariable Long id) {

        Order order =
                orderService.getOrderByIdForAdmin(
                        id
                );

        return new ResponseEntity<>(
                order,
                HttpStatus.OK
        );
    }


    // =========================================================
    // ADMIN GET ANY ORDER ITEMS
    // =========================================================

    @GetMapping("/admin/{orderId}/items")
    public ResponseEntity<List<OrderItem>>
    getOrderItemsForAdmin(
            @PathVariable Long orderId) {

        List<OrderItem> items =
                orderService.getOrderItemsByOrderId(
                        orderId
                );

        return new ResponseEntity<>(
                items,
                HttpStatus.OK
        );
    }


    // =========================================================
    // CREATE ORDER ITEM
    // USER OWNERSHIP CHECK
    // =========================================================

    @PostMapping("/items")
    public ResponseEntity<OrderItem> createOrderItem(
            @RequestBody OrderItem orderItem,
            Authentication authentication) {

        String email =
                authentication.getName();

        OrderItem savedItem =
                orderService.createOrderItem(
                        orderItem,
                        email
                );

        return new ResponseEntity<>(
                savedItem,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET USER ORDER ITEMS
    // OWNER ONLY
    // =========================================================

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>>
    getOrderItemsByOrderId(
            @PathVariable Long orderId,
            Authentication authentication) {

        String email =
                authentication.getName();

        List<OrderItem> items =
                orderService
                        .getOrderItemsByOrderIdAndEmail(
                                orderId,
                                email
                        );

        return new ResponseEntity<>(
                items,
                HttpStatus.OK
        );
    }
}