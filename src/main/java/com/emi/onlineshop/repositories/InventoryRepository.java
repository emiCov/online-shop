package com.emi.onlineshop.repositories;

import com.emi.onlineshop.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByCode(String code);
}
