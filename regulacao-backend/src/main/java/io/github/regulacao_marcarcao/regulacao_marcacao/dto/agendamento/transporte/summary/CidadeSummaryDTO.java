package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Cidade;

public record CidadeSummaryDTO(
    Long id, String nome
) {
    
    public static CidadeSummaryDTO fromEntity (Cidade cidade){
        return new CidadeSummaryDTO(cidade.getId(), cidade.getNomeCidade());
    }
}
