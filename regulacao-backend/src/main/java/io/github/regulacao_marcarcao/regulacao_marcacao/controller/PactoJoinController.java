package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.join.CriarJoinRequestDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.join.JoinRequestViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.PactoJoinService;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.PactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/registry/pactos")
@RequiredArgsConstructor
public class PactoJoinController {

    private final PactoJoinService joinService;
    private final PactoService pactoService;

    @PostMapping("/{pactoId}/join-requests")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<JoinRequestViewDTO> solicitar(@PathVariable Long pactoId, @RequestBody @Valid CriarJoinRequestDTO dto) {
        return ResponseEntity.ok(joinService.solicitar(pactoId, dto.mensagem()));
    }

    @GetMapping("/{pactoId}/join-requests")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<List<JoinRequestViewDTO>> listar(@PathVariable Long pactoId) {
        return ResponseEntity.ok(joinService.listarPorPacto(pactoId));
    }

    @GetMapping("/join-requests/meus")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<List<JoinRequestViewDTO>> listarMinhas(@RequestParam(value = "status", required = false) io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoConviteStatus status) {
        return ResponseEntity.ok(joinService.listarMinhasSolicitacoes(status));
    }

    @PostMapping("/join-requests/{token}/responder")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<JoinRequestViewDTO> responder(@PathVariable UUID token, @RequestBody io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite.ResponderConviteDTO dto) {
        return ResponseEntity.ok(joinService.responder(token, dto.aceitar()));
    }

    // Lista pública de pactos disponíveis para ingresso (exclui os que já participo/criei)
    @GetMapping
    public ResponseEntity<java.util.List<PactoViewDTO>> listarPublicos() {
        return ResponseEntity.ok(pactoService.listarPublicosDisponiveisParaIngresso());
    }
}
