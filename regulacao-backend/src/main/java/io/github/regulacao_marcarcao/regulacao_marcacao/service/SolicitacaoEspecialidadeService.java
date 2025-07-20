package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadeUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadesStatusUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.SolicitacaoEspecialidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoEspecialidadeRepository;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void atualizarStatusEspecialidadesEmLote(EspecialidadesStatusUpdateDTO dto) {
        if (dto.especialidadeIds() == null || dto.especialidadeIds().isEmpty()) {
            return; // Nada a fazer
        }
        List<SolicitacaoEspecialidade> especialidades = repository.findAllById(dto.especialidadeIds());

        for (SolicitacaoEspecialidade especialidade : especialidades) {
            especialidade.setStatus(dto.status());
        }

        repository.saveAll(especialidades);
    }
}
