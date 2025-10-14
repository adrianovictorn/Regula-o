package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransportePacientePayload;

public record AgendarTransporteDiaDTO(
    Long transporteId,
    Long cidadeId,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate data,
    List<AgendamentoTransportePacientePayload> pacientes,
    List<Long> locaisAgendamento,
    Long motoristaId,
    @JsonFormat(pattern = "HH:mm") LocalTime horaSaida
) {}
