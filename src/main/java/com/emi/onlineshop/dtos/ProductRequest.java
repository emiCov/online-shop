package com.emi.onlineshop.dtos;

import com.emi.onlineshop.models.TechnicalDetails;
import org.springframework.lang.NonNull;

import java.util.Set;

public record ProductRequest(
        @NonNull String name,
        @NonNull String code,
        String description,
        @NonNull Double price,
        Set<TechnicalDetailsRequest> technicalDetails) {
}
