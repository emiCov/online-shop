package com.emi.onlineshop.dtos;

import com.emi.onlineshop.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record CartItemResponse(
        ProductResponse product,
        Short quantity,
        UserResponse user,
        @JsonIgnore Double subTotal
) {
}
