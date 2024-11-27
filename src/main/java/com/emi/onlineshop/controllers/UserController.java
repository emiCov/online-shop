package com.emi.onlineshop.controllers;

import com.emi.onlineshop.dtos.KeycloakUser;
import com.emi.onlineshop.services.KeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final KeycloakService keycloakService;

    public UserController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<KeycloakUser>> getKeycloakUsers() {
        return ResponseEntity.ok(keycloakService.getKeyCloakUsers());
    }
}
