package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadeUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadesStatusUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.SolicitacaoEspecialidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.SolicitacaoEspecialidadeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/especialidades")
@RequiredArgsConstructor
public class SolicitacaoEspecialidadeController {
    

    private final SolicitacaoEspecialidadeService service; 


    @GetMapping
    public ResponseEntity<List<SolicitacaoEspecialidadeViewDTO>> listarTodasEspecialidades(){
        List<SolicitacaoEspecialidadeViewDTO> views = service.listarTodasEspecialidades();
        return ResponseEntity.ok(views);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoEspecialidadeViewDTO> atualizarEspecialidade(@PathVariable Long id, @RequestBody EspecialidadeUpdateDTO dto){
        SolicitacaoEspecialidadeViewDTO view = service.atualizarStatusEspecialidade(dto, id);

        return ResponseEntity.ok(view);
    }

    @PutMapping("/status/batch")
    public ResponseEntity<Void> atualizarStatusEmLote(@RequestBody EspecialidadesStatusUpdateDTO dto) {
        service.atualizarStatusEspecialidadesEmLote(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/aguardando")
    public ResponseEntity<List<SolicitacaoEspecialidadeViewDTO>> listarStatusAguardando(){
        return ResponseEntity.ok(service.listarStatusAguardando());
    }

    @GetMapping("/status/agendado")
    public ResponseEntity<List<SolicitacaoEspecialidadeViewDTO>> listarStatusAgendado(){
        return ResponseEntity.ok(service.listarStatusAgendado());
    }

    @GetMapping("/status/realizado")
    public ResponseEntity<List<SolicitacaoEspecialidadeViewDTO>> listarStatusRealizado(){
        return ResponseEntity.ok(service.listarStatusRealizado());
    }

    @GetMapping("/status/retorno")
    public ResponseEntity<List<SolicitacaoEspecialidadeViewDTO>> listarStatusRetorno(){
        return ResponseEntity.ok(service.listarStatusRetorno());
    }

    @GetMapping("/status/retorno/policlinica")
    public ResponseEntity<List<SolicitacaoEspecialidadeViewDTO>> listarStatusRetornoPoliclinica(){
        return ResponseEntity.ok(service.listarStatusRetornoPoliClinica());
    }

     @GetMapping("/status/cancelado")
    public ResponseEntity<List<SolicitacaoEspecialidadeViewDTO>> listarStatusCancelado(){
        return ResponseEntity.ok(service.listarStatusCancelado());
    }
}
