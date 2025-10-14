package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.MotoristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/motoristas")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaService motoristaService;

    @PostMapping
    public ResponseEntity<MotoristaViewDTO> criar(@Valid @RequestBody MotoristaCreateDTO dto) {
        return ResponseEntity.ok(motoristaService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoristaViewDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MotoristaUpdateDTO dto) {
        return ResponseEntity.ok(motoristaService.atualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<MotoristaListDTO>> listar() {
        return ResponseEntity.ok(motoristaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoristaViewDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(motoristaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        motoristaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
