package br.com.fiap.lelis.cap8rest_springboot.dto;

public record LoginResponse(String token, long expiresAt) {}