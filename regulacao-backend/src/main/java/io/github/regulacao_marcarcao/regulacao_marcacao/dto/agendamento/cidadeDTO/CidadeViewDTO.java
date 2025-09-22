package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Cidade;

public record CidadeViewDTO(
    Long id,
    String nomeCidade,
    String codigoIBGE,
    String cep
) {
    public static CidadeViewDTO fromEntity(Cidade cidade) {
        return new CidadeViewDTO(
            cidade.getId(),
            cidade.getNomeCidade(),
            cidade.getCodigoIBGE(),
            cidade.getCep()
        );
    }
}
