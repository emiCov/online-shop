package com.emi.onlineshop.repositories;

import com.emi.onlineshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

}
