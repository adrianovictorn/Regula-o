package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping("/unread")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<List<Map<String, Object>>> listarNaoLidas() {
        try {
            return ResponseEntity.ok(notificacaoService.listarNaoLidas());
        } catch (Exception e) {
            // Evita 500 na UI e permite diagnosticar pelo log do backend
            e.printStackTrace();
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }
    }

    @PostMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<Void> marcarTodasComoLidas() {
        notificacaoService.marcarTodasComoLidas();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('ADMIN','USER','RECEPCAO','ENFERMEIRO','MEDICO','COORD_TRANSPORTE')")
    public ResponseEntity<List<Map<String, Object>>> listarLidas() {
        try {
            return ResponseEntity.ok(notificacaoService.listarLidas());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }
    }

    public record CriarNotificacaoRequest(
            String tipo,
            String resumo,
            String linkPath,
            Map<String, Object> payload
    ) {}

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> criarLocal(@RequestBody CriarNotificacaoRequest req) {
        notificacaoService.criar(
                req.tipo() != null ? req.tipo() : "LOCAL",
                req.resumo(),
                req.linkPath(),
                req.payload()
        );
        return ResponseEntity.ok().build();
    }
}
