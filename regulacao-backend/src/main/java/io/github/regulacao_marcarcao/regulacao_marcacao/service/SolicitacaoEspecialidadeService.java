package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadeUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.SolicitacaoEspecialidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoEspecialidadeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitacaoEspecialidadeService {
    

    private final SolicitacaoEspecialidadeRepository repository;

    public SolicitacaoEspecialidadeViewDTO atualizarStatusEspecialidade(EspecialidadeUpdateDTO dto, Long id){
        SolicitacaoEspecialidade especialidade = repository.findById(id).orElseThrow(() -> new RuntimeException("Especialidade não encontrada!"));
        
        if (dto.prioridade() != null) {
            especialidade.setPrioridade(dto.prioridade());
        }

        if (dto.status() != null) {
            especialidade.setStatus(dto.status());
        }

        SolicitacaoEspecialidade especialidadeAtualizada = repository.save(especialidade);
        SolicitacaoEspecialidadeViewDTO viewDTO = SolicitacaoEspecialidadeViewDTO.fromSolicitacaoEspecialidade(especialidadeAtualizada);
        return viewDTO;


    }
}
