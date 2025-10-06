package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonFormat;

public record AgendarTransporteDiaDTO(
    Long transporteId,
    Long cidadeId,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate data,
    Set<Long> novasSolicitacoes,
    List<Long> locaisAgendamento
) {}
