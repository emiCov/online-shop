package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.Credentials;
import com.emi.onlineshop.dtos.KeycloakUser;
import com.emi.onlineshop.dtos.RegisterRequest;
import com.emi.onlineshop.models.Role;
import com.emi.onlineshop.models.User;
import com.emi.onlineshop.repositories.RoleRepository;
import com.emi.onlineshop.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final KeycloakService keycloakService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.keycloakService = keycloakService;
    }

    public String addUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmailIgnoreCase(registerRequest.email())) {
            throw new IllegalArgumentException("User with email: " + registerRequest.email() + " already exists");
        }

        String userRole = (registerRequest.role() == null || registerRequest.role().isEmpty()) ?
                "USER" : registerRequest.role();

        Credentials credentials = new Credentials(
                "password", registerRequest.password(), false
        );

        List<String> roles = List.of(userRole);

        KeycloakUser keycloakUser = new KeycloakUser(
                null, registerRequest.email(), registerRequest.firstName(), registerRequest.lastName(), List.of(credentials),
                registerRequest.email(), false, true, roles);

        String keycloakUserId = keycloakService.createKeycloakUser(keycloakUser);

        Role role = getDBRole(userRole);

        User user = new User();
        user.setFirstName(keycloakUser.firstName());
        user.setLastName(keycloakUser.lastName());
        user.setEmail(keycloakUser.email());
        user.setRoles(Set.of(role));
        user.setKeycloakId(keycloakUserId);

        userRepository.save(user);

        return "User created successfully";
    }

    private Role getDBRole(String userRole) {
        Optional<Role> dbRole = roleRepository.findByName(userRole);
        if (dbRole.isPresent()) {
            return dbRole.get();
        }
        Role newRole = new Role();
        newRole.setName(userRole);
        return roleRepository.save(newRole);
    }
}
