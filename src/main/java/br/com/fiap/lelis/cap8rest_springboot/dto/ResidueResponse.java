package br.com.fiap.lelis.cap8rest_springboot.dto;

import java.time.Instant;

public record ResidueResponse(Long id, String type, Double volumeKg, Instant timestamp) {}
