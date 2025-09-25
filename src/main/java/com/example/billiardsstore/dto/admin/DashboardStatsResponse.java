package com.example.billiardsstore.dto.admin;

import java.math.BigDecimal;

public record DashboardStatsResponse(
    Long totalProducts,
    Long totalOrders,
    Long totalCustomers,
    BigDecimal totalRevenue,
    Long pendingOrders,
    Long completedOrders
) {
}