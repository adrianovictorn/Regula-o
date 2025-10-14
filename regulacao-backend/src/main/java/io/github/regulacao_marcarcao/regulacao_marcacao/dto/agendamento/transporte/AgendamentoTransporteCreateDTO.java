package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record AgendamentoTransporteCreateDTO(
   @NotNull Long transporteId,
   @NotNull Long cidadeId,
   @NotNull LocalDate data,
   Long motoristaId,
   LocalTime horaSaida
) {
}
