package br.com.fiap.lelis.cap8rest_springboot.service;

import br.com.fiap.lelis.cap8rest_springboot.dto.CreateUserRequest;
import br.com.fiap.lelis.cap8rest_springboot.dto.UserResponse;
import br.com.fiap.lelis.cap8rest_springboot.model.entity.AppUser;
import br.com.fiap.lelis.cap8rest_springboot.model.repository.AppUserRepository;
import br.com.fiap.lelis.cap8rest_springboot.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    @Transactional
    public UserResponse create(CreateUserRequest req) {
        if (repo.existsByEmail(req.email()))
            throw new BusinessException("EMAIL_DUPLICATE");

        if (repo.existsByUsername(req.username()))
            throw new BusinessException("USERNAME_DUPLICATE");

        AppUser user = new AppUser();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setPasswordHash(encoder.encode(req.password()));

        AppUser saved = repo.save(user);
        return new UserResponse(saved.getId(), saved.getEmail(), saved.getUsername());
    }
}
