package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.AgendamentoSolicitacaoSimpleViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.AgendamentoViewDto;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.MultiAgendamentoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.AgendamentoService; // Mantido AgendamentoService
import io.github.regulacao_marcarcao.regulacao_marcacao.service.SolicitacaoService; // Importar SolicitacaoService também
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {


    private final AgendamentoService agendamentoService;
    private final SolicitacaoService solicitacaoService; // Injetando SolicitacaoService

    /**
     * Lista todas as solicitações pendentes para agendamento.
     */
    @GetMapping("/pendentes")
    public ResponseEntity<List<AgendamentoViewDto>> listarSolicitacoesPendentes() {
        List<AgendamentoViewDto> pendentes = agendamentoService.listarSolicitacoesPendentes();
        return ResponseEntity.ok(pendentes);
    }

    /**
     * Lista todos os agendamentos já criados.
     */
    @GetMapping
    public ResponseEntity<List<AgendamentoSolicitacaoSimpleViewDTO>> listarTodosAgendamentos() {
        List<AgendamentoSolicitacaoSimpleViewDTO> agendamentos = agendamentoService.listAll();
        return ResponseEntity.ok(agendamentos);
    }

    /**
     * Cria um agendamento para uma solicitação específica.
     */
     @PostMapping("/{solicitacaoId}")
    public ResponseEntity<AgendamentoSolicitacaoSimpleViewDTO> criarAgendamento(
            @PathVariable Long solicitacaoId,
            @RequestBody MultiAgendamentoCreateDTO dto) { // Usa o novo DTO
        // Chama o novo método criado no AgendamentoService
        AgendamentoSolicitacaoSimpleViewDTO agendamentoCriado = agendamentoService.criarAgendamentoParaMultiplosExames(solicitacaoId, dto);
        return new ResponseEntity<>(agendamentoCriado, HttpStatus.CREATED);
    }

    /**
     * Lista todos os agendamentos criados para uma solicitação específica.
     */
    @GetMapping("/solicitacao/{solicitacaoId}")
    public ResponseEntity<List<AgendamentoSolicitacaoSimpleViewDTO>> listarAgendamentosPorSolicitacaoId(@PathVariable Long solicitacaoId) {
        List<AgendamentoSolicitacaoSimpleViewDTO> agendamentos = solicitacaoService.listarAgendamentosPorSolicitacaoId(solicitacaoId);
        return ResponseEntity.ok(agendamentos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable Long id){
        agendamentoService.deleteAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}