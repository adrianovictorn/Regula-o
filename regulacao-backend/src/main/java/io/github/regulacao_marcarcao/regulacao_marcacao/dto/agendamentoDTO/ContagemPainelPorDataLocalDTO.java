package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO;

import java.time.LocalDate;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.LocalDeAgendamentoEnum;

public record ContagemPainelPorDataLocalDTO(
     String especialidades,
     LocalDeAgendamentoEnum localAgendamento,
     LocalDate data,
     long contagem
) {
}
