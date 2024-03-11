package com.emi.onlineshop.controllers;

import com.emi.onlineshop.dtos.CartItemResponse;
import com.emi.onlineshop.services.CartItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@PreAuthorize("hasAuthority('USER')")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/{productCode}/{quantity}")
    public String addProductToCart(@PathVariable String productCode, @PathVariable short quantity) {
        return cartItemService.addProductToCart(productCode, quantity);
    }

    @DeleteMapping("/{productCode}")
    public String deleteProductFromCart(@PathVariable String productCode) {
        return cartItemService.deleteProductFromCart(productCode);
    }

    @GetMapping("/user")
    public List<CartItemResponse> getCartForUser() {
        return cartItemService.getMappedCartForUser();
    }

    @DeleteMapping("/user")
    public String deleteCartForUser() {
        return cartItemService.deleteCartForUser();
    }
}
