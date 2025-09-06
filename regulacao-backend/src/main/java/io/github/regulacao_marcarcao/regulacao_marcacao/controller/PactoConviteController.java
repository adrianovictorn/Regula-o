package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite.CriarConvitesDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite.ConviteViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite.ResponderConviteDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.PactoConviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pactos")
@RequiredArgsConstructor
public class PactoConviteController {

    private final PactoConviteService conviteService;

    @PostMapping("/{pactoId}/convites")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<List<ConviteViewDTO>> convidar(
            @PathVariable Long pactoId,
            @RequestBody @Valid CriarConvitesDTO dto
    ) {
        return ResponseEntity.ok(conviteService.criarConvites(pactoId, dto.convidados(), dto.mensagem()));
    }

    @GetMapping("/{pactoId}/convites")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<List<ConviteViewDTO>> listar(@PathVariable Long pactoId) {
        return ResponseEntity.ok(conviteService.listarPorPacto(pactoId));
    }

    // público por token
    @PostMapping("/convites/{token}/responder")
    public ResponseEntity<ConviteViewDTO> responder(@PathVariable UUID token, @RequestBody ResponderConviteDTO dto) {
        return ResponseEntity.ok(conviteService.responder(token, dto.aceitar()));
    }
}

