package com.emi.onlineshop.services;

import com.emi.onlineshop.config.security.services.JwtService;
import com.emi.onlineshop.dtos.AuthenticationRequest;
import com.emi.onlineshop.dtos.AuthenticationResponse;
import com.emi.onlineshop.dtos.RegisterRequest;
import com.emi.onlineshop.models.Role;
import com.emi.onlineshop.models.User;
import com.emi.onlineshop.repositories.RoleRepository;
import com.emi.onlineshop.repositories.UserRepository;
import com.emi.onlineshop.security.SecurityUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("User with email: " + request.email() + " already exists");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("No role found"));

        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(new SecurityUser(savedUser));

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findUserByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(new SecurityUser(user));

        return new AuthenticationResponse(token);
    }

}
