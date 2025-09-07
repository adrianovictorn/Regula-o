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
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoEspecialidadeRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final AgendamentoSolicitacaoRepository agendamentoRepository;
    private final SolicitacaoEspecialidadeRepository especialidadeRepository;
    private final io.github.regulacao_marcarcao.regulacao_marcacao.config.InstanceContext instanceContext;
    private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    /**
     * Retorna todas as solicitações com ao menos uma especialidade com status AGUARDANDO
     */
    @Transactional(readOnly = true)
    public List<AgendamentoViewDto> listarSolicitacoesPendentes() {
        return solicitacaoRepository.findAll().stream()
            .filter(s -> s.getEspecialidades().stream()
                .anyMatch(e -> e.getStatus() == StatusDaMarcacao.AGUARDANDO || e.getStatus() == StatusDaMarcacao.RETORNO || e.getStatus() == StatusDaMarcacao.RETORNO_POLICLINICA))
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
            .filter(e -> {
                if (dto.especialidadeId() != null) {
                    return e.getEspecialidadeSolicitada() != null && dto.especialidadeId().equals(e.getEspecialidadeSolicitada().getId())
                        && (e.getStatus() == StatusDaMarcacao.AGUARDANDO || e.getStatus() == StatusDaMarcacao.RETORNO || e.getStatus() == StatusDaMarcacao.RETORNO_POLICLINICA);
                }
                String codigo = dto.especialidadeSolicitada() != null ? dto.especialidadeSolicitada().name() : null;
                String atual = e.getEspecialidadeSolicitada() != null ? e.getEspecialidadeSolicitada().getCodigo() : e.getEspecialidadeCodigoLegacy();
                return (codigo != null && codigo.equalsIgnoreCase(atual))
                    && (e.getStatus() == StatusDaMarcacao.AGUARDANDO || e.getStatus() == StatusDaMarcacao.RETORNO || e.getStatus() == StatusDaMarcacao.RETORNO_POLICLINICA);
            })
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

        notificarAgendamentoExternoSeAplicavel(solicitacao, ag);
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
                        // Compara o código (enum name) ou legado com a String recebida, ignorando maiúsculas/minúsculas.
                        String atual = e.getEspecialidadeSolicitada() != null ? e.getEspecialidadeSolicitada().getCodigo() : e.getEspecialidadeCodigoLegacy();
                        return atual != null && atual.equalsIgnoreCase(nomeExame)
                                && (
                                    e.getStatus() == StatusDaMarcacao.AGUARDANDO
                                    || e.getStatus() == StatusDaMarcacao.RETORNO 
                                    || e.getStatus() == StatusDaMarcacao.RETORNO_POLICLINICA);
                    })
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Exame pendente '" + nomeExame + "' não encontrado na solicitação."));

            // 3. Atualiza o status e associa o agendamento.
            especialidadeParaAgendar.setStatus(StatusDaMarcacao.AGENDADO);
            especialidadeParaAgendar.setAgendamentoSolicitacao(agendamentoSalvo);
        }

        // 4. Salva a solicitação para persistir as alterações nas especialidades.
        solicitacaoRepository.save(solicitacao);

        notificarAgendamentoExternoSeAplicavel(solicitacao, agendamentoSalvo);
        return AgendamentoSolicitacaoSimpleViewDTO.fromAgendamentoSolicitacao(agendamentoSalvo);
    }

    


    @Transactional
    public void deleteAgendamento(Long id){
       AgendamentoSolicitacao agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
       agendamentoRepository.delete(agendamento);
       
        // Salva as alterações nas especialidades
        especialidadeRepository.desvincularAgendamento(id);

        // Agora, deleta o agendamento
        agendamentoRepository.delete(agendamento);
    }

    

    private void notificarAgendamentoExternoSeAplicavel(Solicitacao solicitacao, AgendamentoSolicitacao ag) {
        try {
            var local = instanceContext.getMunicipioLocal();
            // Só notifica se a solicitação tiver origem externa (campos preenchidos) e a origem for diferente do local
            if (solicitacao.getOrigemMunicipioId() != null && !solicitacao.getOrigemMunicipioId().equals(local.getId())) {
                String cpf = solicitacao.getCpfPaciente();
                String cpfMask = cpf != null && cpf.length() >= 4 ?
                        ("***.***.***-" + cpf.substring(cpf.length()-2)) : "***";

                var msg = new io.github.regulacao_marcarcao.regulacao_marcacao.dto.notificacao.AgendamentoExternoMensagemDTO(
                        solicitacao.getId(),
                        cpfMask,
                        solicitacao.getNomePaciente(),
                        local.getNome(),
                        ag.getDataAgendada()
                );

                String destinoNome = (solicitacao.getOrigemMunicipioNome() != null ? solicitacao.getOrigemMunicipioNome() : solicitacao.getOrigemMunicipioId().toString()).toUpperCase();
                String routingKey = String.format("agendamento-externo.%s", destinoNome);
                rabbitTemplate.convertAndSend(io.github.regulacao_marcarcao.regulacao_marcacao.config.RabbitMQConfig.EXCHANGE_NAME, routingKey, msg);
            } else {
                // Fallback: origem desconhecida → broadcast para assinantes de broadcast
                String routingKey = "agendamento-externo.BROADCAST";
                var msg = new io.github.regulacao_marcarcao.regulacao_marcacao.dto.notificacao.AgendamentoExternoMensagemDTO(
                        solicitacao.getId(),
                        "***",
                        solicitacao.getNomePaciente(),
                        local.getNome(),
                        ag.getDataAgendada()
                );
                rabbitTemplate.convertAndSend(io.github.regulacao_marcarcao.regulacao_marcacao.config.RabbitMQConfig.EXCHANGE_NAME, routingKey, msg);
            }
        } catch (Exception ignore) {}
    }
}

