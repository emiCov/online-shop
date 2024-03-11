package com.emi.onlineshop.dtos;

import java.util.Set;

public record ProductResponse(
        String name,
        String code,
        String description,
        Double price,
        Set<TechnicalDetailsResponse> technicalDetails) {
}