package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.RegisterRequest;
import com.emi.onlineshop.models.Role;
import com.emi.onlineshop.models.User;
import com.emi.onlineshop.repositories.RoleRepository;
import com.emi.onlineshop.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String addUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmailIgnoreCase(registerRequest.email())) {
            throw new IllegalArgumentException("User with email: " + registerRequest.email() + " already exists");
        }

        User user = new User();
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("No role found"));

        user.setRoles(Set.of(role));

        userRepository.save(user);

        return "User created successfully";
    }

    public String addRoleToUser(String email, String role) {
        if (role.equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("'ADMIN' role cannot be granted this way");
        }

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found"));

        Role roleDB = roleRepository.findByName(role.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Role " + role + " does not exist"));

        Set<Role> userRoles = user.getRoles();
        if (userRoles.contains(roleDB)) {
            throw new IllegalArgumentException("Role " + role + " already exists for the user " + email);
        }

        userRoles.add(roleDB);
        user.setRoles(userRoles);

        userRepository.save(user);

        return "Role " + role + " added to the user " + email;
    }

    public String addRole(String role) {
        if (roleRepository.existsByName(role)) {
            throw new IllegalArgumentException("Role " + role + " already exists");
        }

        Role newRole = new Role();
        newRole.setName(role.toUpperCase());

        roleRepository.save(newRole);

        return "Role " + role + " created successfully";
    }
}
