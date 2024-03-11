package com.emi.onlineshop.dtos;

public record OrderDetailsResponse(
        short quantity,
        double unitPrice,
        double subtotal,
        ProductResponse product
) {
}
