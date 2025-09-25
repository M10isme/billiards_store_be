package com.example.billiardsstore.dto.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull(message = "productId is required")
    @JsonAlias({"product_id", "productId"})
    private Long productId;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;
}
