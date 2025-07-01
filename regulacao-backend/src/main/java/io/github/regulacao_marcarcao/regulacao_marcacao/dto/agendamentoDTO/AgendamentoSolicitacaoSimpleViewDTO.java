package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoSolicitacao;

public record AgendamentoSolicitacaoSimpleViewDTO(
    Long id,
    String localAgendado,
    String dataAgendada,
    String observacoes,
    String turno
) {
    public static AgendamentoSolicitacaoSimpleViewDTO fromAgendamentoSolicitacao(AgendamentoSolicitacao agendamento) {
        return new AgendamentoSolicitacaoSimpleViewDTO(
            agendamento.getId(),
            agendamento.getLocalAgendado() != null ? agendamento.getLocalAgendado().name() : null,
            agendamento.getDataAgendada() != null ? agendamento.getDataAgendada().toString() : null,
            agendamento.getObservacoes(),
            agendamento.getTurno() != null ? agendamento.getTurno().toString() : null
        );
    }
}
