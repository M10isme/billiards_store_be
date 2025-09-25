package com.example.billiardsstore.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String address;
    private String phone;
    private String paymentMethod;  // Ví dụ: "COD", "BANK_TRANSFER", "PAYPAL"
    private List<OrderItemRequest> items;
}
