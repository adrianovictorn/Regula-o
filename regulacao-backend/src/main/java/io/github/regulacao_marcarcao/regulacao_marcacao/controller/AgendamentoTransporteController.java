package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.AgendarTransporteDiaDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.AgendamentoTransporteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendamento/transporte")
@RequiredArgsConstructor
public class AgendamentoTransporteController {

    private final AgendamentoTransporteService agendamentoTransporteService;

    @PostMapping("/cadastrar")
    public ResponseEntity<AgendamentoTransporteViewDTO> cadastrarAgendamentoTransporte(@RequestBody AgendamentoTransporteCreateDTO dto){
        return ResponseEntity.ok(agendamentoTransporteService.criarAgendamentoTransporte(dto));
    }

    @PostMapping("/agendar/solicitacoes")
    public ResponseEntity<AgendamentoTransporteViewDTO> cadastrarAgendaDoDia(@RequestBody AgendarTransporteDiaDTO dto){
        return ResponseEntity.ok(agendamentoTransporteService.agendarTransporteDia(
            dto.transporteId(),
            dto.cidadeId(),
            dto.data(),
            dto.pacientes(),
            dto.locaisAgendamento(),
            dto.motoristaId(),
            dto.horaSaida()
        ));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoTransporteListDTO>> listarAgendamentoTransporte(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long transporteId,
            @RequestParam(required = false) Long motoristaId){
        return ResponseEntity.ok(agendamentoTransporteService.listarAgendamentoTransporte(data, transporteId, motoristaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoTransporteViewDTO> buscarAgendamentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoTransporteService.buscarAgendamentoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoTransporteViewDTO> atualizarAgendamentoTransporte(@RequestBody AgendamentoTransporteUpdateDTO dto, @PathVariable Long id){
        return ResponseEntity.ok(agendamentoTransporteService.atualizarAgendamentoTransporte(dto, id));
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoTransporteViewDTO> confirmarAgendamento(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoTransporteService.confirmarAgendamento(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamentoTransporte(@PathVariable Long id){
        agendamentoTransporteService.deletarAgendamentoTransporte(id);
        return ResponseEntity.noContent().build();
    }
    
}
