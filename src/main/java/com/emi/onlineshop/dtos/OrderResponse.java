package com.emi.onlineshop.dtos;

import com.emi.onlineshop.models.OrderStatus;

import java.time.LocalDate;
import java.util.Set;

public record OrderResponse(
        LocalDate localDate,
        Double total,
        OrderStatus orderStatus,
        UserResponse user,
        Set<OrderDetailsResponse> orderDetails
) {
}
