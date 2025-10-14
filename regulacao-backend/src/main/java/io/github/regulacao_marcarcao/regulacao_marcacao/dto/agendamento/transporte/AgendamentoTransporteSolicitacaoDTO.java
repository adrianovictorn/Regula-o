package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.SolicitacaoSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary.LocalAgendamentoSummaryDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TurnoEnum;

public record AgendamentoTransporteSolicitacaoDTO(
    Long id,
    String nomePaciente,
    String cpf,
    String telefone,
    String usfOrigem,
    TurnoEnum turno,
    Boolean retornaMesmoDia,
    LocalAgendamentoSummaryDTO localAgendamento
) {

    public static AgendamentoTransporteSolicitacaoDTO fromSummary(
        SolicitacaoSummaryDTO resumo,
        TurnoEnum turno,
        Boolean retornaMesmoDia,
        LocalAgendamentoSummaryDTO localResumo
    ) {
        return new AgendamentoTransporteSolicitacaoDTO(
            resumo.id(),
            resumo.nomePaciente(),
            resumo.cpf(),
            resumo.telefone(),
            resumo.usfOrigem(),
            turno,
            retornaMesmoDia,
            localResumo
        );
    }
}
