package com.example.billiardsstore.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String customerName;
    private String customerUsername;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private String shippingAddress;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDTO> items;
}
