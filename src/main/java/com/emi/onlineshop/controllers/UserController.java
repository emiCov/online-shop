package com.emi.onlineshop.controllers;

import com.emi.onlineshop.dtos.RegisterRequest;
import com.emi.onlineshop.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String addUser(@RequestBody RegisterRequest registerRequest) {
        return userService.addUser(registerRequest);
    }
}
