package com.example.billiardsstore.service;

import com.example.billiardsstore.dto.admin.DashboardStatsResponse;
import com.example.billiardsstore.model.Order;
import com.example.billiardsstore.model.OrderStatus;
import com.example.billiardsstore.model.Role;
import com.example.billiardsstore.repository.OrderRepository;
import com.example.billiardsstore.repository.ProductRepository;
import com.example.billiardsstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DashboardStatsResponse getDashboardStats() {
        Long totalProducts = productRepository.count();
        Long totalOrders = orderRepository.count();
        Long totalCustomers = userRepository.countByRole(Role.CUSTOMER);
        
        BigDecimal totalRevenue = orderRepository.findAll().stream()
            .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
            .map(order -> order.getTotalPrice() != null ? order.getTotalPrice() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        Long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        Long completedOrders = orderRepository.countByStatus(OrderStatus.COMPLETED);

        return new DashboardStatsResponse(
            totalProducts,
            totalOrders,
            totalCustomers,
            totalRevenue,
            pendingOrders,
            completedOrders
        );
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByIdDesc();
    }
    
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(orderStatus);
        
        return orderRepository.save(order);
    }
}