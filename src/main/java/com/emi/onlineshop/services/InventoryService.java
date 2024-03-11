package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.InventoryResponse;
import com.emi.onlineshop.models.Inventory;
import com.emi.onlineshop.repositories.InventoryRepository;
import com.emi.onlineshop.utils.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final Mapper mapper;

    public InventoryService(InventoryRepository inventoryRepository, Mapper mapper) {
        this.inventoryRepository = inventoryRepository;
        this.mapper = mapper;
    }


    public List<InventoryResponse> findAll() {
        return inventoryRepository.findAll()
                .stream()
                .map(mapper::mapInventoryResponse)
                .toList();
    }

    public InventoryResponse findByCode(String code) {
        Inventory inventory = inventoryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("No inventory found for code " + code));
        return mapper.mapInventoryResponse(inventory);
    }
}
