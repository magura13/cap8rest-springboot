package br.com.fiap.lelis.cap8rest_springboot.controller;

import br.com.fiap.lelis.cap8rest_springboot.dto.ResidueRequest;
import br.com.fiap.lelis.cap8rest_springboot.dto.ResidueResponse;
import br.com.fiap.lelis.cap8rest_springboot.service.ResidueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/residues")
@RequiredArgsConstructor
public class ResidueController {

    private final ResidueService service;

    private String user() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResidueResponse create(@RequestBody ResidueRequest body) {
        return service.create(body, user());
    }

    @GetMapping
    public Page<ResidueResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.list(user(), PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public ResidueResponse update(@PathVariable Long id,
                                  @RequestBody ResidueRequest body) {
        return service.update(id, body, user());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id, user());
    }
}
