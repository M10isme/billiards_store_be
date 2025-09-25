package com.example.billiardsstore.model;

public enum OrderStatus {
    PENDING,     // mới đặt
    PROCESSING,  // đang xử lý
    SHIPPED,     // đã giao
    COMPLETED,   // hoàn tất
    CANCELLED    // hủy
}
