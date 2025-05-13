package br.com.fiap.lelis.cap8rest_springboot.service;

import br.com.fiap.lelis.cap8rest_springboot.dto.ResidueRequest;
import br.com.fiap.lelis.cap8rest_springboot.dto.ResidueResponse;
import br.com.fiap.lelis.cap8rest_springboot.exception.BusinessException;
import br.com.fiap.lelis.cap8rest_springboot.model.entity.AppUser;
import br.com.fiap.lelis.cap8rest_springboot.model.entity.Residue;
import br.com.fiap.lelis.cap8rest_springboot.model.repository.AppUserRepository;
import br.com.fiap.lelis.cap8rest_springboot.model.repository.ResidueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResidueService {

    private final ResidueRepository repo;
    private final AppUserRepository userRepo;

    private ResidueResponse map(Residue r) {
        return new ResidueResponse(r.getId(), r.getType().name(), r.getVolumeKg(), r.getTimestamp());
    }

    @Transactional
    public ResidueResponse create(ResidueRequest req, String username) {
        Residue r = new Residue();
        r.setType(Residue.Type.valueOf(req.type().toUpperCase()));
        r.setVolumeKg(req.volumeKg());
        r.setTimestamp(Instant.now());

        AppUser owner = userRepo.findByUsername(username).orElseThrow();
        r.setOwner(owner);
        return map(repo.save(r));
    }

    public Page<ResidueResponse> list(String username, Pageable pageable) {
        return repo.findByOwnerUsername(username, pageable)
                .map(this::map);
    }

    @Transactional
    public ResidueResponse update(Long id, ResidueRequest req, String username) {
        Residue r = repo.findById(id).orElseThrow();
        if (!r.getOwner().getUsername().equals(username))
            throw new BusinessException("FORBIDDEN");

        r.setType(Residue.Type.valueOf(req.type().toUpperCase()));
        r.setVolumeKg(req.volumeKg());
        return map(repo.save(r));
    }

    @Transactional
    public void delete(Long id, String username) {
        Residue r = repo.findById(id).orElseThrow();
        if (!r.getOwner().getUsername().equals(username))
            throw new BusinessException("FORBIDDEN");

        repo.delete(r);
    }
}
