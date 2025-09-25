package com.example.billiardsstore.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

// dto/order/CheckoutResponse.java
@Data
@AllArgsConstructor
public class CheckoutResponse {
    private Long orderId;
    private String message;
}

