package com.example.billiardsstore.service;
import com.example.billiardsstore.dto.order.CreateOrderRequest;
import com.example.billiardsstore.dto.order.OrderDTO;
import com.example.billiardsstore.dto.order.OrderItemDTO;
import com.example.billiardsstore.model.*;
import com.example.billiardsstore.repository.OrderRepository;
import com.example.billiardsstore.repository.ProductRepository;
import com.example.billiardsstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // chuyển entity sang DTO
    public OrderDTO toDto(Order order) {
        var items = order.getItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()))
                .toList();

        // Get user information
        User user = userRepository.findByUsername(order.getCustomerUsername()).orElse(null);

        return new OrderDTO(
                order.getId(), 
                order.getCustomerName(),
                order.getCustomerUsername(),
                user != null ? user.getEmail() : null,
                user != null ? user.getPhoneNumber() : order.getPhone(),
                user != null ? user.getAddress() : null,
                order.getAddress(), // shipping address
                order.getTotalPrice(), 
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items);
    }

    // tạo đơn hàng từ giỏ hàng
    public OrderDTO checkout(CreateOrderRequest req, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setCustomerName(user.getFullName());
        // persist username so orders can be queried reliably even if fullName changes
        order.setCustomerUsername(user.getUsername());
        order.setAddress(req.getAddress());
        order.setPhone(req.getPhone());
        order.setPaymentMethod(PaymentMethod.valueOf(req.getPaymentMethod()));
        order.setStatus(OrderStatus.PENDING);

        // validate items before processing to return clear error instead of NPE in repository
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order items must not be empty");
        }

        // xử lý items, trừ tồn kho
        var items = req.getItems().stream().map(itemReq -> {
            if (itemReq.getProductId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productId is required for each item");
            }
            Product p = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (p.getQuantityInStock() < itemReq.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + p.getName());
            }
            p.setQuantityInStock(p.getQuantityInStock() - itemReq.getQuantity());
            productRepository.save(p);

            return OrderItem.builder()
                    .order(order)
                    .product(p)
                    .quantity(itemReq.getQuantity())
                    .price(p.getPrice())
                    .build();
        }).toList();

        order.setItems(items);
        order.setTotalPrice(
                items.stream()
                        .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        return toDto(orderRepository.save(order));
    }

    // lấy đơn hàng của khách hiện tại
    public List<OrderDTO> getOrdersByUser(String username) {
        // query by stored username to avoid issues when fullName changes
        return orderRepository.findByCustomerUsername(username)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // lấy chi tiết đơn hàng theo ID
    public OrderDTO getOrderById(Long id, String username) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        // Check if user has permission to view this order
        // Admin can view all orders, customers can only view their own
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getRole() != Role.ADMIN && !username.equals(order.getCustomerUsername())) {
            throw new AccessDeniedException("Access denied");
        }
        
        return toDto(order);
    }

    // lấy toàn bộ đơn cho admin
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    // cập nhật trạng thái đơn (cho admin)
    public OrderDTO updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return toDto(orderRepository.save(order));
    }

    public OrderDTO cancelOrder(Long orderId, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        
        // Only allow cancellation by the owner (matching username) and when status is PENDING
        if (!username.equals(order.getCustomerUsername())) {
            throw new RuntimeException("Bạn không có quyền hủy đơn hàng này");
        }
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng đang chờ xử lý");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        // Note: updatedAt will be automatically set by JPA @PreUpdate if configured
        return toDto(orderRepository.save(order));
    }

}
