package com.emi.onlineshop.repositories;

import com.emi.onlineshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);

    @Query("""
            SELECT p FROM Product p
            JOIN FETCH p.technicalDetails td
            """)
    List<Product> getAllProductsBetter();

    long deleteByCode(String code);
}