package com.emi.onlineshop.services;

import com.emi.onlineshop.models.User;
import com.emi.onlineshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("isAuthenticated")
    public User getAuthenticatedUser() {
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Jwt principal = (Jwt) token.getPrincipal();
        Map<String, Object> claims = principal.getClaims();
        String email = (String) claims.get("email");

        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found"));
    }

}
