package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return ResponseEntity.ok(agendamentoTransporteService.agendarTransporteDia(dto.transporteId(), dto.cidadeId(), dto.data(), dto.novasSolicitacoes(), dto.locaisAgendamento()));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoTransporteListDTO>> listarAgendamentoTransporte(){
        return ResponseEntity.ok(agendamentoTransporteService.listarAgendamentoTransporte());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoTransporteViewDTO> atualizarAgendamentoTransporte(@RequestBody AgendamentoTransporteUpdateDTO dto, @PathVariable Long id){
        return ResponseEntity.ok(agendamentoTransporteService.atualizarAgendamentoTransporte(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamentoTransporte(@PathVariable Long id){
        agendamentoTransporteService.deletarAgendamentoTransporte(id);
        return ResponseEntity.noContent().build();
    }
    
}
