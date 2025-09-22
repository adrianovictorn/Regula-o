package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Cidade;

public record CidadeListDTO(
    Long id,
    String nomeCidade,
    String codigoIBGE,
    String cep
) {
    public static CidadeListDTO fromEntity(Cidade cidade) {
        return new CidadeListDTO(
            cidade.getId(),
            cidade.getNomeCidade(),
            cidade.getCodigoIBGE(),
            cidade.getCep()
        );
    }
}
