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

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.LocalAgendamentoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/local/agendamento")
@RequiredArgsConstructor
public class LocalAgendamentoController {
    
    private final LocalAgendamentoService localAgendamentoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<LocalAgendamentoViewDTO> cadastrarLocalAgendamento(@RequestBody LocalAgendamentoCreateDTO dto){
        return ResponseEntity.ok(localAgendamentoService.cadastrarLocalAgendamento(dto));
    }

    @GetMapping
    public ResponseEntity<List<LocalAgendamentoListDTO>> listarAgendamentos(){
        return ResponseEntity.ok(localAgendamentoService.listarAgendamentoListDTOs());
    }

    @PutMapping("{id}")
    public ResponseEntity<LocalAgendamentoViewDTO> atualizarLocalAgendamento(@PathVariable Long id, @RequestBody LocalAgendamentoUpdateDTO dto){
        return ResponseEntity.ok(localAgendamentoService.atualizarLocalAgendamento(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarLocalAgendamento(@PathVariable Long id){
        localAgendamentoService.deletarLocalAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
