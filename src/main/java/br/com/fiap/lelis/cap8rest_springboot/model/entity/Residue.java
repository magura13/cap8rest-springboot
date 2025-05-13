package br.com.fiap.lelis.cap8rest_springboot.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "residue")
@Getter @Setter @NoArgsConstructor
public class Residue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Type type;

    @Column(nullable = false)
    private Double volumeKg;

    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUser owner;

    public enum Type { PLASTIC, GLASS, PAPER, METAL, ORGANIC }
}
