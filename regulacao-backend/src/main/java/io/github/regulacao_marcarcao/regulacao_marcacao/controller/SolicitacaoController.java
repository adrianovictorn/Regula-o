package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadeAdicionarDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoPublicViewDTO;
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
    @PreAuthorize("hasAnyRole('ADMIN','USER', 'RECEPCAO', 'ENFERMEIRO', 'MEDICO')") // Apenas ADMIN pode criar
    public ResponseEntity<SolicitacaoViewDTO> criarSolicitacao(@Valid @RequestBody SolicitacaoCreateDTO dto){
        SolicitacaoViewDTO viewDTO = service.createSolicitacao(dto);
        return ResponseEntity.ok(viewDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'RECEPCAO', 'ENFERMEIRO', 'MEDICO')") // ADMIN ou USER podem listar todas
    public ResponseEntity<List<SolicitacaoViewDTO>> listarSolicitacoes(){
       List<SolicitacaoViewDTO> lista = service.todasSolicitacoes();
       return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'RECEPCAO', 'ENFERMEIRO', 'MEDICO')") // ADMIN ou USER podem buscar por ID
    public ResponseEntity<SolicitacaoViewDTO> buscarPorId (@PathVariable Long id){
        SolicitacaoViewDTO solicitacao = service.getSolicitacaoById(id);
        return ResponseEntity.ok(solicitacao);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode atualizar
    public ResponseEntity<SolicitacaoViewDTO> atualizarSolicitacao (@PathVariable Long id, @RequestBody SolicitacaoUpdateDTO dto){
        SolicitacaoViewDTO view = service.updateSolicitacao(id, dto);
        return ResponseEntity.ok(view);
    }

     @PostMapping("/{solicitacaoId}/especialidades")
     @PreAuthorize("hasRole('ADMIN', 'USER', 'RECEPCAO', 'ENFERMEIRO', 'MEDICO')") // Apenas ADMIN pode adicionar especialidades
    public ResponseEntity<SolicitacaoViewDTO> adicionarEspecialidadeASolicitacao(
            @PathVariable Long solicitacaoId,
            @RequestBody EspecialidadeAdicionarDTO dto) {
        SolicitacaoViewDTO updatedSolicitacao = service.adicionarEspecialidadeASolicitacao(solicitacaoId, dto);
        return ResponseEntity.ok(updatedSolicitacao);
    }

   @DeleteMapping("especialidades/{id}") // Corrigido: adicionado o PathVariable
    @PreAuthorize("hasRole('ADMIN', 'USER', 'RECEPCAO', 'ENFERMEIRO', 'MEDICO')")
    public ResponseEntity<Void> deletarEspecialidade(@PathVariable Long id){
        service.removerEspecialidade(id); // Chamando o serviço correto
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    @GetMapping("/public/cpf/{cpf}")
    public ResponseEntity<List<SolicitacaoPublicViewDTO>> getPublicSolicitacoesByCpf(@PathVariable String cpf){
        List<SolicitacaoPublicViewDTO> solicitacoes = service.buscarPacientePorCpf(cpf);
        return ResponseEntity.ok(solicitacoes);
    }
}