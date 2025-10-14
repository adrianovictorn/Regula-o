package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO;

import java.time.LocalDate;

public record ContagemPainelPorDataLocalDTO(
     String especialidades,
     String localAgendamento,
     LocalDate data,
     long contagem
) {
}
