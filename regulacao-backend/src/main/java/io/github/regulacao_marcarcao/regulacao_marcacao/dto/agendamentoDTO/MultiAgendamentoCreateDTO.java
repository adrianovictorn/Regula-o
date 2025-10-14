package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO;

import java.time.LocalDate;
import java.util.List;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.LocalDeAgendamentoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TurnoEnum;

public record MultiAgendamentoCreateDTO(
    List<String> examesSelecionados,
    LocalDate dataAgendada,
    LocalDeAgendamentoEnum localAgendado,
    Long localAgendamentoId,
    TurnoEnum turno,
    String observacoes
) {}
