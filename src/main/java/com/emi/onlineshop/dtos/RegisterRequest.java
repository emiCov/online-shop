package com.emi.onlineshop.dtos;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String role,
        String password
) {
}
