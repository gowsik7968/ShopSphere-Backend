package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.security.LoginResponse;
import com.shopsphere.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody User user) {

        userService.registerUser(user);

        return new ResponseEntity<>(
                "Registration successful",
                HttpStatus.CREATED
        );
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody User user) {

        LoginResponse response =
                userService.loginUser(
                        user.getEmail(),
                        user.getPassword()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}