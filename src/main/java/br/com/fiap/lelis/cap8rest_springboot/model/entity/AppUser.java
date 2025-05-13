package br.com.fiap.lelis.cap8rest_springboot.model.entity;

import jakarta.persistence.*;
        import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name = "app_user", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_user_username", columnNames = "username")
})
public class AppUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 60)
    private String username;

    @Column(nullable = false)
    private String passwordHash;
}
