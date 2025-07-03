package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PrioridadeDaMarcacaoEnum;

public record EspecialidadeUpdateDTO(
    PrioridadeDaMarcacaoEnum prioridade
) {
    
}
