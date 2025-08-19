package io.github.regulacao_marcarcao.regulacao_marcacao.dto.cid;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.CID;

public record CIDListDTO(
    Long id,
    String codigo,
    String descricao
) {
    public static CIDListDTO fromDTO(CID cid){
        return new CIDListDTO(cid.getIdCid(),cid.getCodigo(),
         cid.getDescricao());
    }
}
