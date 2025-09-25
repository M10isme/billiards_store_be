package com.example.billiardsstore.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddToCartRequest {
    private Long productId;
    private int quantity;
}
