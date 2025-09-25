package com.example.billiardsstore.controller;
import com.example.billiardsstore.dto.order.CreateOrderRequest;
import com.example.billiardsstore.dto.order.OrderDTO;
import com.example.billiardsstore.model.OrderStatus;
import com.example.billiardsstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // checkout
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public OrderDTO checkout(@RequestBody CreateOrderRequest req, Authentication auth) {
        return orderService.checkout(req, auth.getName());
    }

    // xem đơn hàng của mình
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<OrderDTO> getMyOrders(Authentication auth) {
        return orderService.getOrdersByUser(auth.getName());
    }

    // get order detail by ID (customer can only see their own orders)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public OrderDTO getOrderById(@PathVariable("id") Long id, Authentication auth) {
        return orderService.getOrderById(id, auth.getName());
    }

    // admin xem tất cả đơn
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAll() {
        return orderService.getAllOrders();
    }

    // admin cập nhật trạng thái
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO updateStatus(@PathVariable("id") Long id,
                                 @RequestParam("status") OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public OrderDTO cancelOrder(@PathVariable("id") Long id, Authentication auth) {
        String username = auth.getName();
        return orderService.cancelOrder(id, username);
    }

}

