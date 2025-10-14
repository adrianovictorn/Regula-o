package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusAgendamento;



public record AgendamentoTransporteUpdateDTO(
   List<AgendamentoTransportePacientePayload> pacientes,
   List<Long> localId,
   Long cidadeId,
   Long transporteId,
   LocalDate data,
   StatusAgendamento status,
   Long motoristaId,
   LocalTime horaSaida
) {
    
}
