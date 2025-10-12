package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import java.time.LocalDate;
import java.util.List;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.CidadeSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.LocalAgendamentoSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.SolicitacaoSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.TransporteSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoTransporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusAgendamento;

public record AgendamentoTransporteListDTO(
    List<LocalAgendamentoSummaryDTO> localAgendamento,
    CidadeSummaryDTO cidade,
    TransporteSummaryDTO transporte,
    StatusAgendamento status,
    List<SolicitacaoSummaryDTO> solicitacao,
    LocalDate data

) {
    public static AgendamentoTransporteListDTO fromDTO(AgendamentoTransporte agendamentoTransporte){

        var solicitacoesExistentes = agendamentoTransporte.getSolicitacoes().stream().map(SolicitacaoSummaryDTO::fromEntity).toList();

        return new AgendamentoTransporteListDTO(
            agendamentoTransporte.getLocaisAgendamento().stream().map(LocalAgendamentoSummaryDTO::fromEntity).toList(),
            CidadeSummaryDTO.fromEntity(agendamentoTransporte.getCidade()),
            TransporteSummaryDTO.fromEntity(agendamentoTransporte.getTransporte()),
            agendamentoTransporte.getStatus(),
            solicitacoesExistentes,
            agendamentoTransporte.getData());
    }
}
