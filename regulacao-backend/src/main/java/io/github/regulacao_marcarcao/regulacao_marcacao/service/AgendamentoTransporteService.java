package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.TransporteSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.AgendamentoViewDto;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoTransporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Cidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.LocalAgendamento;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Transporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusAgendamento;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.AgendamentoTransporteRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.CidadeRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.LocalAgendamentoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.TransporteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoTransporteService {

    private final CidadeRepository cidadeRepository;
    private final TransporteRepository transporteRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final AgendamentoTransporteRepository agendamentoTransporteRepository;
    private final LocalAgendamentoRepository localAgendamentoRepository;

   @Transactional
    public AgendamentoTransporteViewDTO criarAgendamentoTransporte(AgendamentoTransporteCreateDTO dto){
        Cidade cidade = cidadeRepository.findById(dto.cidadeId())
            .orElseThrow(() -> new IllegalArgumentException("Cidade inexistente"));
        Transporte transporte = transporteRepository.findById(dto.transporteId())
            .orElseThrow(() -> new IllegalArgumentException("Transporte inexistente"));

        agendamentoTransporteRepository.findByTransporteIdAndData(transporte.getId(), dto.data())
            .ifPresent(x -> { throw new IllegalStateException("Já existe agendamento para esse transporte nesta data"); });

        AgendamentoTransporte ag = new AgendamentoTransporte();
        ag.setCidade(cidade);
        ag.setTransporte(transporte);
        ag.setData(dto.data());
        ag.setStatus(StatusAgendamento.PENDENTE);
        ag.setSolicitacoes(new HashSet<>());           // evita null
        ag.setLocaisAgendamento(new java.util.ArrayList<>());

        ag = agendamentoTransporteRepository.save(ag);
        return AgendamentoTransporteViewDTO.fromDTO(ag);
    }
    @Transactional
    public List<AgendamentoTransporteListDTO> listarAgendamentoTransporte (){
        List<AgendamentoTransporte> listarAgendamentos = agendamentoTransporteRepository.findAll();
        List<AgendamentoTransporteListDTO> listViewsDTO = listarAgendamentos.stream().map(AgendamentoTransporteListDTO::fromDTO).toList();
        return listViewsDTO;
    }

    @Transactional
    public AgendamentoTransporteViewDTO atualizarAgendamentoTransporte(AgendamentoTransporteUpdateDTO dto, Long id){
        AgendamentoTransporte ag = agendamentoTransporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        Cidade cidade = cidadeRepository.findById(dto.cidadeId())
            .orElseThrow(() -> new RuntimeException("Cidade não existente!"));
        Transporte transporte = transporteRepository.findById(dto.transporteId())
            .orElseThrow(() -> new RuntimeException("Transporte não existe!"));

        Set<Long> solicitacaoIds = dto.solicitacaoId() != null ? dto.solicitacaoId() : Collections.emptySet();
        List<Long> localIds      = dto.localId()       != null ? dto.localId()       : Collections.emptyList();

        Set<Solicitacao> novasSolicitacoes = new HashSet<>(solicitacaoRepository.findAllById(solicitacaoIds));
        List<LocalAgendamento> novosLocais = localAgendamentoRepository.findAllById(localIds);

        agendamentoTransporteRepository.findByTransporteIdAndData(transporte.getId(), dto.data())
            .filter(outro -> !outro.getId().equals(ag.getId()))
            .ifPresent(x -> { throw new IllegalStateException("Já existe agendamento para esse transporte nesta data"); });

        int capacidade = transporte.getVagas() == null ? Integer.MAX_VALUE : transporte.getVagas().intValue();
        if (novasSolicitacoes.size() > capacidade) {
            throw new IllegalStateException("Número de solicitações excede o número de vagas!");
        }

        ag.setCidade(cidade);
        ag.setTransporte(transporte);
        ag.setData(dto.data());
        ag.setStatus(dto.status());

        ag.getSolicitacoes().clear();
        ag.getSolicitacoes().addAll(novasSolicitacoes);

        ag.getLocaisAgendamento().clear();
        ag.getLocaisAgendamento().addAll(novosLocais);

        agendamentoTransporteRepository.save(ag);
        return AgendamentoTransporteViewDTO.fromDTO(ag);
    }

    @Transactional
    public void deletarAgendamentoTransporte(Long id){
      AgendamentoTransporte agendamentoTransporte = agendamentoTransporteRepository.findById(id).orElseThrow(() -> new RuntimeException("Agendamento de Transporte não encontrado !"));
      agendamentoTransporteRepository.delete(agendamentoTransporte);
    }

    @Transactional
    public AgendamentoTransporteViewDTO agendarTransporteDia(Long transporteId, Long cidadeId, LocalDate data, Set<Long> novasSolicitacoes, List<Long> locaisAgendamento){
        if (transporteId == null || cidadeId == null || data == null) {
            throw new IllegalArgumentException("Transporte, cidade e data sao obrigatorios para o agendamento.");
        }

        var solicitacaoIds = novasSolicitacoes == null ? Collections.<Long>emptySet() : novasSolicitacoes;
        var locaisIds = locaisAgendamento == null ? Collections.<Long>emptyList() : locaisAgendamento;

        var agendamento = agendamentoTransporteRepository.findByTransporteIdAndData(transporteId, data)
            .orElseGet(() -> {
                var cidade = cidadeRepository.findById(cidadeId)
                    .orElseThrow(() -> new IllegalArgumentException("Cidade inexistente."));
                var transporte = transporteRepository.findById(transporteId)
                    .orElseThrow(() -> new IllegalArgumentException("Transporte inexistente."));

                var novo = new AgendamentoTransporte();
                novo.setCidade(cidade);
                novo.setTransporte(transporte);
                novo.setData(data);
                novo.setStatus(StatusAgendamento.PENDENTE);
                novo.setSolicitacoes(new HashSet<>());
                novo.setLocaisAgendamento(new java.util.ArrayList<>());
                return agendamentoTransporteRepository.save(novo);
            });

        if (agendamento.getSolicitacoes() == null) {
            agendamento.setSolicitacoes(new HashSet<>());
        }
        if (agendamento.getLocaisAgendamento() == null) {
            agendamento.setLocaisAgendamento(new java.util.ArrayList<>());
        }

        var locaisNovos = localAgendamentoRepository.findAllById(locaisIds);
        if (!locaisNovos.isEmpty()) {
            var idsAtuais = agendamento.getLocaisAgendamento().stream()
                .map(LocalAgendamento::getId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

            locaisNovos.stream()
                .filter(local -> local.getId() != null && !idsAtuais.contains(local.getId()))
                .forEach(agendamento.getLocaisAgendamento()::add);
        }

        var novasSolicitacoesEntidades = solicitacaoRepository.findAllById(solicitacaoIds);
        if (novasSolicitacoesEntidades.isEmpty()) {
            return AgendamentoTransporteViewDTO.fromDTO(agendamento);
        }

        var idsSolicitacoesAtuais = agendamento.getSolicitacoes().stream()
            .map(Solicitacao::getId)
            .filter(java.util.Objects::nonNull)
            .collect(java.util.stream.Collectors.toSet());

        var novas = novasSolicitacoesEntidades.stream()
            .filter(s -> s.getId() != null && !idsSolicitacoesAtuais.contains(s.getId()))
            .collect(java.util.stream.Collectors.toCollection(HashSet::new));

        if (novas.isEmpty()) {
            return AgendamentoTransporteViewDTO.fromDTO(agendamento);
        }

        var transporte = agendamento.getTransporte();
        if (transporte == null) {
            throw new IllegalStateException("Agendamento sem transporte associado.");
        }

        int capacidade = transporte.getVagas() == null ? Integer.MAX_VALUE : transporte.getVagas().intValue();
        int consumido = agendamento.getSolicitacoes().size();
        int precisa = novas.size();

        if ((long) consumido + precisa > capacidade) {
            throw new IllegalStateException("Nao ha vagas suficientes para esta data.");
        }

        agendamento.getSolicitacoes().addAll(novas);

        try {
            agendamento = agendamentoTransporteRepository.saveAndFlush(agendamento);
        } catch (jakarta.persistence.OptimisticLockException | org.hibernate.StaleObjectStateException e) {
            throw new IllegalStateException("Conflito de atualizacao: tente novamente.", e);
        }

        return AgendamentoTransporteViewDTO.fromDTO(agendamento);
    }
}
