package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusAgendamento;
import jakarta.validation.constraints.NotNull;



public record AgendamentoTransporteUpdateDTO(
   Set<@NotNull Long> solicitacaoId,
   List<Long> localId,
   Long cidadeId,
   Long transporteId,
   LocalDate data,
   StatusAgendamento status
) {
    
}
