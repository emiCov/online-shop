package com.emi.onlineshop.controllers;

import com.emi.onlineshop.dtos.OrderResponse;
import com.emi.onlineshop.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<OrderResponse> findOrdersByUserId() {
        return orderService.findOrdersByUserId();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteOrderById(@PathVariable long id) {
        return orderService.deleteOrderById(id);
    }

}
