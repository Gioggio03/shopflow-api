package com.shopflow.shopflow_api.dto;

import com.shopflow.shopflow_api.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        BigDecimal total,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}