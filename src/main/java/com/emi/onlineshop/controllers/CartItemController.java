package com.emi.onlineshop.controllers;

import com.emi.onlineshop.aspect.HasAuthorities;
import com.emi.onlineshop.dtos.CartItemResponse;
import com.emi.onlineshop.services.CartItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/{productCode}/{quantity}")
    @HasAuthorities(roles = {"USER"})
    public String addProductToCart(@PathVariable String productCode, @PathVariable short quantity) {
        return cartItemService.addProductToCart(productCode, quantity);
    }

    @DeleteMapping("/{productCode}")
    @HasAuthorities(roles = {"USER"})
    public String deleteProductFromCart(@PathVariable String productCode) {
        return cartItemService.deleteProductFromCart(productCode);
    }

    @GetMapping("/user")
    @HasAuthorities(roles = {"USER"})
    public List<CartItemResponse> getCartForUser() {
        return cartItemService.getMappedCartForUser();
    }

    @DeleteMapping("/user")
    @HasAuthorities(roles = {"USER"})
    public String deleteCartForUser() {
        return cartItemService.deleteCartForUser();
    }
}
