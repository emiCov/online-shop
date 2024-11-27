package com.emi.onlineshop.dtos;

public record Credentials(
        String type,
        String value,
        boolean temporary
) {
}
