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

    // Dentro da classe SolicitacaoEspecialidadeController

    @PutMapping("/status/batch")
    public ResponseEntity<Void> atualizarStatusEmLote(@RequestBody EspecialidadesStatusUpdateDTO dto) {
        service.atualizarStatusEspecialidadesEmLote(dto);
        return ResponseEntity.noContent().build();
    }
}
