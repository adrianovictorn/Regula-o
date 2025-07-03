package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.AgendamentoSolicitacaoSimpleViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.AgendamentoViewDto;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.MultiAgendamentoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.AgendamentoSolicitacaoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoSolicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.AgendamentoSolicitacaoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final AgendamentoSolicitacaoRepository agendamentoRepository;

    /**
     * Retorna todas as solicitações com ao menos uma especialidade com status AGUARDANDO
     */
    @Transactional(readOnly = true)
    public List<AgendamentoViewDto> listarSolicitacoesPendentes() {
        return solicitacaoRepository.findAll().stream()
            .filter(s -> s.getEspecialidades().stream()
                .anyMatch(e -> e.getStatus() == StatusDaMarcacao.AGUARDANDO))
            .map(AgendamentoViewDto::fromSolicitacao)
            .collect(Collectors.toList());
    }

    /**
     * Cria um agendamento para a solicitação e especialidade informadas, atualiza status e retorna DTO simples
     */
    @Transactional
    public AgendamentoSolicitacaoSimpleViewDTO create(Long solicitacaoId, AgendamentoSolicitacaoCreateDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
            .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));

        // encontra especialidade pendente
        SolicitacaoEspecialidade especialidade = solicitacao.getEspecialidades().stream()
            .filter(e -> e.getEspecialidadeSolicitada() == dto.especialidadeSolicitada()
                && e.getStatus() == StatusDaMarcacao.AGUARDANDO)
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Especialidade não disponível para agendamento."));

        // salva agendamento
        AgendamentoSolicitacao ag = new AgendamentoSolicitacao();
        ag.setSolicitacao(solicitacao);
        ag.setLocalAgendado(dto.localAgendado());
        ag.setDataAgendada(dto.dataAgendada());
        ag.setObservacoes(dto.observacoes());
        ag.setTurno(dto.turno());
        ag = agendamentoRepository.save(ag);

        // atualiza status da especialidade
        especialidade.setStatus(StatusDaMarcacao.AGENDADO);
        especialidade.setAgendamentoSolicitacao(ag);
        solicitacaoRepository.save(solicitacao);

        return AgendamentoSolicitacaoSimpleViewDTO.fromAgendamentoSolicitacao(ag);
    }

    /**
     * Lista todos os agendamentos realizados
     */
    @Transactional(readOnly = true)
    public List<AgendamentoSolicitacaoSimpleViewDTO> listAll() {
        return agendamentoRepository.findAll().stream()
            .map(AgendamentoSolicitacaoSimpleViewDTO::fromAgendamentoSolicitacao)
            .collect(Collectors.toList());
    }

    /**
     * Busca agendamento por ID
     */
    @Transactional(readOnly = true)
    public AgendamentoSolicitacaoSimpleViewDTO getById(Long id) {
        AgendamentoSolicitacao ag = agendamentoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));
        return AgendamentoSolicitacaoSimpleViewDTO.fromAgendamentoSolicitacao(ag);
    }

     @Transactional
    public AgendamentoSolicitacaoSimpleViewDTO criarAgendamentoParaMultiplosExames(Long solicitacaoId, MultiAgendamentoCreateDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada com o ID: " + solicitacaoId));

        // 1. Cria a entidade de agendamento.
        AgendamentoSolicitacao novoAgendamento = new AgendamentoSolicitacao();
        novoAgendamento.setSolicitacao(solicitacao);
        novoAgendamento.setLocalAgendado(dto.localAgendado());
        novoAgendamento.setDataAgendada(dto.dataAgendada());
        novoAgendamento.setObservacoes(dto.observacoes());
        novoAgendamento.setTurno(dto.turno());
        
        // Salva para obter um ID.
        AgendamentoSolicitacao agendamentoSalvo = agendamentoRepository.save(novoAgendamento);

        // 2. Itera sobre os exames selecionados.
        for (String nomeExame : dto.examesSelecionados()) {
            SolicitacaoEspecialidade especialidadeParaAgendar = solicitacao.getEspecialidades().stream()
                    .filter(e -> {
                        // --- CORREÇÃO APLICADA AQUI ---
                        // Compara o nome do enum (String) com a String recebida, ignorando maiúsculas/minúsculas.
                        return e.getEspecialidadeSolicitada().name().equalsIgnoreCase(nomeExame)
                                && e.getStatus() == StatusDaMarcacao.AGUARDANDO;
                    })
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Exame pendente '" + nomeExame + "' não encontrado na solicitação."));

            // 3. Atualiza o status e associa o agendamento.
            especialidadeParaAgendar.setStatus(StatusDaMarcacao.AGENDADO);
            especialidadeParaAgendar.setAgendamentoSolicitacao(agendamentoSalvo);
        }

        // 4. Salva a solicitação para persistir as alterações nas especialidades.
        solicitacaoRepository.save(solicitacao);

        return AgendamentoSolicitacaoSimpleViewDTO.fromAgendamentoSolicitacao(agendamentoSalvo);
    }


    @Transactional
    public void deleteAgendamento(Long id){
       AgendamentoSolicitacao agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
       agendamentoRepository.delete(agendamento);
    }

  
}

