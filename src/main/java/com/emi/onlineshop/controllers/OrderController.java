package com.emi.onlineshop.controllers;

import com.emi.onlineshop.dtos.OrderResponse;
import com.emi.onlineshop.services.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@PreAuthorize("hasAuthority('USER')")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> findOrdersByUserId() {
        return orderService.findOrdersByUserId();
    }

    @PostMapping
    public String createOrder() {
        return orderService.createOrder();
    }

    @DeleteMapping("/{id}")
    public String deleteOrderById(@PathVariable long id) {
        return orderService.deleteOrderById(id);
    }

}
