package com.emi.onlineshop.controllers;

import com.emi.onlineshop.aspect.HasAuthorities;
import com.emi.onlineshop.dtos.InventoryResponse;
import com.emi.onlineshop.services.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @HasAuthorities(roles = {"USER"})
    public List<InventoryResponse> findAll() {
        return inventoryService.findAll();
    }

    @GetMapping("/{code}")
    @HasAuthorities(roles = {"USER"})
    public InventoryResponse findByCode(@PathVariable String code) {
        return inventoryService.findByCode(code);
    }

}
