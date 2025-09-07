package io.github.regulacao_marcarcao.regulacao_marcacao.dto.especialidade;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Especialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.ItemCategoria;

public record EspecialidadeDTO(
        Long id,
        String codigo,
        String nome,
        ItemCategoria categoria,
        Boolean ativo
) {
    public static EspecialidadeDTO fromEntity(Especialidade e) {
        return new EspecialidadeDTO(e.getId(), e.getCodigo(), e.getNome(), e.getCategoria(), e.getAtivo());
    }
}

