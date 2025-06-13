package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadeAdicionarDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RequestMapping("/api/solicitacoes")
@RestController
@AllArgsConstructor
public class SolicitacaoController {
    
    private final SolicitacaoService service; 


    @PostMapping
    public ResponseEntity<SolicitacaoViewDTO> criarSolicitacao(@Valid @RequestBody SolicitacaoCreateDTO dto){
        SolicitacaoViewDTO viewDTO = service.createSolicitacao(dto);
        return ResponseEntity.ok(viewDTO);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoViewDTO>> listarSolicitacoes(){
       List<SolicitacaoViewDTO> lista = service.todasSolicitacoes();
       return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoViewDTO> buscarPorId (@PathVariable Long id){
        SolicitacaoViewDTO solicitacao = service.getSolicitacaoById(id);
        return ResponseEntity.ok(solicitacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoViewDTO> atualizarSolicitacao (@PathVariable Long id, @RequestBody SolicitacaoUpdateDTO dto){
        SolicitacaoViewDTO view = service.updateSolicitacao(id, dto);
        return ResponseEntity.ok(view);
    }

     @PostMapping("/{solicitacaoId}/especialidades")
    public ResponseEntity<SolicitacaoViewDTO> adicionarEspecialidadeASolicitacao(
            @PathVariable Long solicitacaoId,
            @RequestBody EspecialidadeAdicionarDTO dto) {
        SolicitacaoViewDTO updatedSolicitacao = service.adicionarEspecialidadeASolicitacao(solicitacaoId, dto);
        return ResponseEntity.ok(updatedSolicitacao);
    }
}


