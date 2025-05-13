package br.com.fiap.lelis.cap8rest_springboot.service;

import br.com.fiap.lelis.cap8rest_springboot.dto.LoginRequest;
import br.com.fiap.lelis.cap8rest_springboot.dto.LoginResponse;
import br.com.fiap.lelis.cap8rest_springboot.exception.BusinessException;
import br.com.fiap.lelis.cap8rest_springboot.model.entity.AppUser;
import br.com.fiap.lelis.cap8rest_springboot.model.repository.AppUserRepository;
import br.com.fiap.lelis.cap8rest_springboot.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest req) {
        AppUser user = repo.findByUsername(req.identifier())
                .or(() -> repo.findByEmail(req.identifier()))
                .orElseThrow(() -> new BusinessException("BAD_CREDENTIALS"));

        if (!encoder.matches(req.password(), user.getPasswordHash()))
            throw new BusinessException("BAD_CREDENTIALS");

        String token = jwtUtil.generate(user.getUsername());
        long expiresAt = System.currentTimeMillis() + 600_000;
        return new LoginResponse(token, expiresAt);
    }
}