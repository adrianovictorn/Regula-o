package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadeUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadesStatusUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.SolicitacaoEspecialidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoSolicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.AgendamentoSolicitacaoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoEspecialidadeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitacaoEspecialidadeService {
    
    private final AgendamentoSolicitacaoRepository agendamentoRepository;
    private final SolicitacaoEspecialidadeRepository especialidadeRepository;

    public SolicitacaoEspecialidadeViewDTO atualizarStatusEspecialidade(EspecialidadeUpdateDTO dto, Long id){
        SolicitacaoEspecialidade especialidade = especialidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Especialidade não encontrada!"));
        
        if (dto.prioridade() != null) {
            especialidade.setPrioridade(dto.prioridade());
        }

        if (dto.status() != null) {
            especialidade.setStatus(dto.status());
        }

        SolicitacaoEspecialidade especialidadeAtualizada = especialidadeRepository.save(especialidade);
        SolicitacaoEspecialidadeViewDTO viewDTO = SolicitacaoEspecialidadeViewDTO.fromSolicitacaoEspecialidade(especialidadeAtualizada);
        return viewDTO;


    }

   @Transactional // Garante que ou tudo funciona, ou nada é salvo no banco
    public void atualizarStatusEspecialidadesEmLote(EspecialidadesStatusUpdateDTO dto) {
        // Parte 1: Atualizar o status das especialidades
        if (dto.especialidadeIds() != null && !dto.especialidadeIds().isEmpty()) {
            List<SolicitacaoEspecialidade> especialidades = especialidadeRepository.findAllById(dto.especialidadeIds());
            for (SolicitacaoEspecialidade especialidade : especialidades) {
                especialidade.setStatus(dto.status());
            }
            especialidadeRepository.saveAll(especialidades);
        }

        // Parte 2: Atualizar a observação no agendamento
        // Verificamos se foi passado um agendamentoId e uma observação
        if (dto.agendamentoId() != null && dto.observacao() != null) {
            AgendamentoSolicitacao agendamento = agendamentoRepository.findById(dto.agendamentoId())
                .orElseThrow(() -> new RuntimeException("Agendamento com ID " + dto.agendamentoId() + " não encontrado!"));
            
            agendamento.setObservacoes(dto.observacao());
            agendamentoRepository.save(agendamento);
        }
    }
}
