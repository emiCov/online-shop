package com.emi.onlineshop.controllers;

import com.emi.onlineshop.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String sayHelloAdmin() {
        return "Hello from admin";
    }

    @PatchMapping("/roles/{user}/{role}")
    public String addRoleToUser(@PathVariable(name = "user") String email,
                                @PathVariable(name = "role") String role) {

        return userService.addRoleToUser(email, role);
    }

    @PostMapping("/roles/{role}")
    public String addRole(@PathVariable(name = "role") String role) {

        return userService.addRole(role);
    }

}
