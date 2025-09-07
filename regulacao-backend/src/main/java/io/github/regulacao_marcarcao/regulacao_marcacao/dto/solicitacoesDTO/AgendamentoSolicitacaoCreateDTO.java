package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO;

import java.time.LocalDate;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.LocalDeAgendamentoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TurnoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;

// Compatível: aceita especialidadeId (catálogo) OU Enum legacy
public record AgendamentoSolicitacaoCreateDTO(
    Long especialidadeId,
    EspecialidadesEnum especialidadeSolicitada,
    LocalDeAgendamentoEnum localAgendado,
    LocalDate dataAgendada,
    TurnoEnum turno,
    String observacoes
) { }
