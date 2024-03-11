package com.emi.onlineshop.utils;

import com.emi.onlineshop.dtos.*;
import com.emi.onlineshop.models.Inventory;
import com.emi.onlineshop.models.Product;
import com.emi.onlineshop.models.TechnicalDetails;
import com.emi.onlineshop.models.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public ProductResponse mapProductResponse(Product product) {
        Set<TechnicalDetailsResponse> technicalDetails = product.getTechnicalDetails()
                .stream()
                .map(this::mapTechnicalDetailsResponse)
                .collect(Collectors.toSet());
        return new ProductResponse(product.getName(), product.getCode(), product.getDescription(), product.getPrice(), technicalDetails);
    }

    public UserResponse mapUserResponse(User user) {
        return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public Product mapProductRequest(ProductRequest productRequest) {
        Product product = new Product();
        product.setCode(productRequest.code());
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        productRequest.technicalDetails().forEach(td -> product.addTechnicalDetail(getTechnicalDetail(td)));

        return product;
    }

    public InventoryResponse mapInventoryResponse(Inventory inventory) {
        return new InventoryResponse(inventory.getCode(), inventory.getQuantity());
    }

    private TechnicalDetails getTechnicalDetail(TechnicalDetailsRequest technicalDetailsRequest) {
        TechnicalDetails td = new TechnicalDetails();
        td.setName(technicalDetailsRequest.name());
        td.setValue(technicalDetailsRequest.value());
        return td;
    }

    private TechnicalDetailsResponse mapTechnicalDetailsResponse(TechnicalDetails technicalDetails) {
        return new TechnicalDetailsResponse(technicalDetails.getName(), technicalDetails.getValue());
    }
}
