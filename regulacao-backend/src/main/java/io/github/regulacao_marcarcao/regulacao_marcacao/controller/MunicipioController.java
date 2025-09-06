package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.municipio.MunicipioViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/municipios")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioRepository municipioRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<List<MunicipioViewDTO>> listar() {
        var lista = municipioRepository.findAll().stream()
                .map(m -> new MunicipioViewDTO(m.getId(), m.getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}
