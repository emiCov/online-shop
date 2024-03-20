package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.InventoryResponse;
import com.emi.onlineshop.models.Inventory;
import com.emi.onlineshop.repositories.InventoryRepository;
import com.emi.onlineshop.utils.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean isEnoughStockForProduct(String productCode, short quantity) {
        Inventory productInventory = getInventoryForProduct(productCode);

        return productInventory.getQuantity() > quantity;
    }

    @Transactional
    public void removeQuantityForProduct(String productCode, long quantity) {
        Inventory productInventory = getInventoryForProduct(productCode);
        if (productInventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("There aren't enough products for: " + productCode);
        }

        updateQuantityForProduct(productInventory, -quantity);
    }

    @Transactional
    public void addQuantityForProduct(String productCode, long quantity) {
        Inventory productInventory = getInventoryForProduct(productCode);
        updateQuantityForProduct(productInventory, quantity);
    }

    private void updateQuantityForProduct(Inventory inventory, long quantity) {
        long updatedQuantity = inventory.getQuantity() + quantity;
        inventory.setQuantity(updatedQuantity);

        inventoryRepository.save(inventory);
    }

    private Inventory getInventoryForProduct(String productCode) {
        return inventoryRepository.findByCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("No product found in inventory"));
    }
}
