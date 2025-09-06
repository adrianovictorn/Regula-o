package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.ClaimResultDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoEventoResumoDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoMembrosDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PublicarSolicitacaoPactoDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.PactoEventoService;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.PactoService;
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
public class PactoController {

    private final PactoEventoService pactoEventoService;
    private final PactoService pactoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<List<PactoViewDTO>> listarPactosDoMunicipio() {
        return ResponseEntity.ok(pactoService.listarMeusPactos());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<PactoViewDTO> criar(@Valid @RequestBody PactoCreateDTO dto) {
        return ResponseEntity.ok(pactoService.criarPacto(dto));
    }

    @PostMapping("/{pactoId}/membros")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<PactoViewDTO> adicionarMembros(@PathVariable Long pactoId, @RequestBody PactoMembrosDTO dto) {
        return ResponseEntity.ok(pactoService.adicionarMembros(pactoId, dto.membros()));
    }

    @DeleteMapping("/{pactoId}/membros/{municipioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<PactoViewDTO> removerMembro(@PathVariable Long pactoId, @PathVariable java.util.UUID municipioId) {
        return ResponseEntity.ok(pactoService.removerMembro(pactoId, municipioId));
    }

    @PostMapping("/{pactoId}/solicitacoes/publicar")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<PactoEventoResumoDTO> publicar(
            @PathVariable Long pactoId,
            @Valid @RequestBody PublicarSolicitacaoPactoDTO dto
    ) {
        PactoEventoResumoDTO resumo = pactoEventoService.publicar(pactoId, dto);
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/{pactoId}/feed")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<List<PactoEventoResumoDTO>> feed(@PathVariable Long pactoId) {
        return ResponseEntity.ok(pactoEventoService.listarFeed(pactoId));
    }

    @PostMapping("/{pactoId}/solicitacoes/{eventoUuid}/claim")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO')")
    public ResponseEntity<ClaimResultDTO> claim(
            @PathVariable Long pactoId,
            @PathVariable UUID eventoUuid
    ) {
        ClaimResultDTO result = pactoEventoService.claim(pactoId, eventoUuid);
        if (!result.claimed()) {
            return ResponseEntity.status(409).body(result);
        }
        return ResponseEntity.ok(result);
    }

}
