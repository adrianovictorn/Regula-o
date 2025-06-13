package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PrioridadeDaMarcacaoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;
import jakarta.validation.constraints.NotNull; // Importe para validação

public record EspecialidadeAdicionarDTO(
    @NotNull // Garante que a especialidade não seja nula
    EspecialidadesEnum especialidadeSolicitada,
     @NotNull StatusDaMarcacao status,   // Adicionado
    @NotNull PrioridadeDaMarcacaoEnum  prioridade
) {
}