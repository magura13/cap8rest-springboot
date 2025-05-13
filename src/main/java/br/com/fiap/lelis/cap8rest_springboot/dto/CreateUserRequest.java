package br.com.fiap.lelis.cap8rest_springboot.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @Email    String email,
        @Size(min = 3, max = 60) String username,
        @Size(min = 6, max = 72) String password
) {}
