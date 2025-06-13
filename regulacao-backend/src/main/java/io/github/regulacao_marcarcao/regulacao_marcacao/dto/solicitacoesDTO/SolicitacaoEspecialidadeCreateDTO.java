package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PrioridadeDaMarcacaoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;

public record SolicitacaoEspecialidadeCreateDTO(
    EspecialidadesEnum especialidadeSolicitada,
    StatusDaMarcacao status,
    PrioridadeDaMarcacaoEnum prioridade
) { }
