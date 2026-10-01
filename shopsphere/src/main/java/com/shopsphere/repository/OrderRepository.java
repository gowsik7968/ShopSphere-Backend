package com.shopsphere.repository;

import com.shopsphere.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByEmail(String email);

    Optional<Order> findByIdAndEmail(
            Long id,
            String email
    );
}