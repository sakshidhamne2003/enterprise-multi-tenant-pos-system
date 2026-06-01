package com.pos.multitanantpos.controller;

import com.pos.multitanantpos.dto.AuthResponse;
import com.pos.multitanantpos.dto.LoginRequest;
import com.pos.multitanantpos.dto.RegisterRequest;
import com.pos.multitanantpos.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Open endpoint — logic inside AuthService decides who can register what role
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
