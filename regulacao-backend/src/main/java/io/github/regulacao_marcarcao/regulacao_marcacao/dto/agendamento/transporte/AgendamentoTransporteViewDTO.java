package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.CidadeSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.LocalAgendamentoSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.SolicitacaoSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.TransporteSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoTransporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusAgendamento;

public record AgendamentoTransporteViewDTO (
    Long id,
    Set<SolicitacaoSummaryDTO> solicitacao,
    List<LocalAgendamentoSummaryDTO> local,
    CidadeSummaryDTO cidade,
    TransporteSummaryDTO transporte,
    LocalDate data,
    StatusAgendamento status
) {

    public static AgendamentoTransporteViewDTO fromDTO(AgendamentoTransporte agendamentoTransporte){
        if (agendamentoTransporte == null) {
            return null;
        }

        var solicitacoes = agendamentoTransporte.getSolicitacoes() == null
            ? Collections.<SolicitacaoSummaryDTO>emptySet()
            : agendamentoTransporte.getSolicitacoes().stream()
                .map(SolicitacaoSummaryDTO::fromEntity)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var locais = agendamentoTransporte.getLocaisAgendamento() == null
            ? Collections.<LocalAgendamentoSummaryDTO>emptyList()
            : agendamentoTransporte.getLocaisAgendamento().stream()
                .map(LocalAgendamentoSummaryDTO::fromEntity)
                .toList();

        var cidade = agendamentoTransporte.getCidade() == null ? null : CidadeSummaryDTO.fromEntity(agendamentoTransporte.getCidade());
        var transporte = agendamentoTransporte.getTransporte() == null ? null : TransporteSummaryDTO.fromEntity(agendamentoTransporte.getTransporte());

        return new AgendamentoTransporteViewDTO(
            agendamentoTransporte.getId(),
            solicitacoes,
            locais,
            cidade,
            transporte,
            agendamentoTransporte.getData(),
            agendamentoTransporte.getStatus());
    }
}