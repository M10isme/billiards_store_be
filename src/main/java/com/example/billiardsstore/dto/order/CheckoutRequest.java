package com.example.billiardsstore.dto.order;

import com.example.billiardsstore.model.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {
    private String customerName;
    private String address;
    private String phone;
    private PaymentMethod paymentMethod;
    private Double totalPrice;
}
