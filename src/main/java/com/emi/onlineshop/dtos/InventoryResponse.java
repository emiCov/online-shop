package com.emi.onlineshop.dtos;

public record InventoryResponse(
        String code,
        long quantity
) {
}
