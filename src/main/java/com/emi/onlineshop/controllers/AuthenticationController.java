package com.emi.onlineshop.controllers;

import com.emi.onlineshop.dtos.AuthenticationRequest;
import com.emi.onlineshop.dtos.KeycloakJwtResponse;
import com.emi.onlineshop.dtos.RegisterRequest;
import com.emi.onlineshop.services.KeycloakService;
import com.emi.onlineshop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;
    private final KeycloakService keycloakService;

    public AuthenticationController(UserService userService, KeycloakService keycloakService) {
        this.userService = userService;
        this.keycloakService = keycloakService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<KeycloakJwtResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(keycloakService.authenticateClient());
    }

    @PostMapping("/authenticate/user")
    public ResponseEntity<KeycloakJwtResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(keycloakService.authenticateUser(request));
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.addUser(registerRequest));
    }
}
