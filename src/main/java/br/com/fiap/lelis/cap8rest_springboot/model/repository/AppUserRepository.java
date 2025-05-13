package br.com.fiap.lelis.cap8rest_springboot.model.repository;

import br.com.fiap.lelis.cap8rest_springboot.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
}
