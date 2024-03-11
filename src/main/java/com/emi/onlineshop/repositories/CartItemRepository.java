package com.emi.onlineshop.repositories;

import com.emi.onlineshop.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUser_EmailAndProduct_Code(String email, String code);


    List<CartItem> findByUser_Email(String email);

    long deleteByUser_EmailAndProduct_Code(String email, String code);
}
