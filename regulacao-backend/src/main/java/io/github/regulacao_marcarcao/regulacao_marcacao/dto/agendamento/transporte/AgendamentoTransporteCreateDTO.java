package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import java.time.LocalDate;



import jakarta.validation.constraints.NotNull;

public record AgendamentoTransporteCreateDTO(
   @NotNull Long transporteId,
   @NotNull Long cidadeId,
   @NotNull LocalDate data
) {
    
}
