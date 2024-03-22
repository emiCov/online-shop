package com.emi.onlineshop.controllers;


import com.emi.onlineshop.aspect.HasAuthorities;
import com.emi.onlineshop.services.CheckoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    @HasAuthorities(roles = {"USER"})
    public String placeOrder() {
        return checkoutService.placeOrder();
    }
}
