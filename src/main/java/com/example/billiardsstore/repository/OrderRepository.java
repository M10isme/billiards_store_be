package com.example.billiardsstore.repository;

import com.example.billiardsstore.model.Order;
import com.example.billiardsstore.model.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByCustomerName(String customerName);

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByCustomerUsername(String customerUsername);
    
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findById(Long id);
    
    Long countByStatus(OrderStatus status);
    
    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findAllByOrderByIdDesc();
}
