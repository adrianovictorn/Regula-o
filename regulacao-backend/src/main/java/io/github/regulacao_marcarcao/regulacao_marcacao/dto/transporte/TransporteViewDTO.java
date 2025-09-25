package io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Transporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TipoVeiculoEnum;

public record TransporteViewDTO(
    Long id,
    String nomeVeiculo,
    Long vagas,
    TipoVeiculoEnum tipoVeiculo,
    String modelo
) {
    public static TransporteViewDTO fromDTO (Transporte transporte){
        return new TransporteViewDTO(
            transporte.getId(),
            transporte.getNomeVeiculo(), 
            transporte.getVagas(), 
            transporte.getTipoVeiculo(), 
            transporte.getModelo());

    }
}
