package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;
import java.util.List;

public record EspecialidadesStatusUpdateDTO(
    List<Long> especialidadeIds,
    StatusDaMarcacao status
) {
}