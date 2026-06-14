package com.shopflow.shopflow_api.dto;

import java.math.BigDecimal;
import java.util.Set;

public record ProductRequest(String name, String description,
                             BigDecimal price, Integer stock,
                             Set<Long> categoryIds) {
}