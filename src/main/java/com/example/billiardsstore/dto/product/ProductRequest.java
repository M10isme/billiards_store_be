package com.example.billiardsstore.dto.product;

import java.math.BigDecimal;

public record ProductRequest(String sku, String name, String description, BigDecimal price, int quantityInStock, Long supplierId, boolean active) {}
