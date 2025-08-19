package io.github.regulacao_marcarcao.regulacao_marcacao.dto.cid;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.CID;

public record CIDViewDTO(
    Long id,
    String codigo,
    String descricao
) {
    public static CIDViewDTO fromDTO(CID cid){
        return new CIDViewDTO(
            cid.getIdCid(),
            cid.getCodigo(),
            cid.getDescricao());
    }
}
