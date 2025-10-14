package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TurnoEnum;
import jakarta.validation.constraints.NotNull;

public record AgendamentoTransportePacientePayload(
    @NotNull Long solicitacaoId,
    @NotNull Long localAgendamentoId,
    TurnoEnum turno,
    Boolean retornaMesmoDia
) {
}
