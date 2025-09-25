package com.example.billiardsstore.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantityInStock;
    private String imageUrl;
    private Long supplierId;
    private String categoryTag;
    private boolean active = true;
}