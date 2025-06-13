package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO;

import java.time.LocalDate;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.LocalDeAgendamentoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;

public record AgendamentoSolicitacaoCreateDTO(
    EspecialidadesEnum especialidadeSolicitada,
    LocalDeAgendamentoEnum localAgendado,
    LocalDate dataAgendada,
    String observacoes
) { }