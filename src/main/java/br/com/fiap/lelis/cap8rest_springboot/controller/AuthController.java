package br.com.fiap.lelis.cap8rest_springboot.controller;

import br.com.fiap.lelis.cap8rest_springboot.dto.LoginRequest;
import br.com.fiap.lelis.cap8rest_springboot.dto.LoginResponse;
import br.com.fiap.lelis.cap8rest_springboot.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }
}