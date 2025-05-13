package br.com.fiap.lelis.cap8rest_springboot.model.repository;

import br.com.fiap.lelis.cap8rest_springboot.model.entity.Residue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidueRepository extends JpaRepository<Residue, Long> {
    Page<Residue> findByOwnerUsername(String username, Pageable pageable);
}