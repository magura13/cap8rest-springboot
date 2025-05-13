package br.com.fiap.lelis.cap8rest_springboot.controller;

import br.com.fiap.lelis.cap8rest_springboot.dto.CreateUserRequest;
import br.com.fiap.lelis.cap8rest_springboot.dto.UserResponse;
import br.com.fiap.lelis.cap8rest_springboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/users") @RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return service.create(request);
    }
}
