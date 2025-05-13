package br.com.fiap.lelis.cap8rest_springboot.config;

import br.com.fiap.lelis.cap8rest_springboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil(@Value("${jwt.secret}") String secret) {
        long expirationSeconds = 600;
        return new JwtUtil(secret, expirationSeconds);
    }
}
