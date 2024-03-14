package com.emi.onlineshop.controllers;

import com.emi.onlineshop.dtos.ProductRequest;
import com.emi.onlineshop.dtos.ProductResponse;
import com.emi.onlineshop.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final String PRODUCTS_PER_PAGE = "3";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/better")
    public List<ProductResponse> getAllProductsBetter() {
        return productService.getAllProductsBetter();
    }

    // filter: key1,operation1,value1;key2,operation2,value2
    @GetMapping("/filter")
    public List<ProductResponse> getAllByFilter(@RequestParam String filter) {
        return productService.getAllByFilter(filter);
    }

    @GetMapping("/page/{pageNo}")
    public List<ProductResponse> getAllProductsByPage(
            @PathVariable int pageNo,
            @RequestParam(defaultValue = PRODUCTS_PER_PAGE) int pageSize,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return productService.getAllProductsByPage(pageNo, pageSize, sortField, sortDir);
    }

    @PostMapping
    public String addProduct(@Valid @RequestBody ProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @DeleteMapping("/{productCode}/{tdName}")
    public String deleteTechnicalDetailFromProduct(@PathVariable String productCode, @PathVariable String tdName) {
        return productService.deleteTechnicalDetailFromProduct(productCode, tdName);
    }

    @DeleteMapping("/{productCode}")
    public String deleteProductByCode(@PathVariable String productCode) {
        return productService.deleteProductByCode(productCode);
    }

}
