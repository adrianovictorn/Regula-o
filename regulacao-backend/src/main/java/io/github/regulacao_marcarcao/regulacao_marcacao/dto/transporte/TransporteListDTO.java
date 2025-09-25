package io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Transporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TipoVeiculoEnum;

public record TransporteListDTO (
    Long id,
    String nomeVeiculo,
    Long vagas,
    TipoVeiculoEnum tipoVeiculo,
    String modelo
){
    public static TransporteListDTO fromDTO(Transporte transporte){
        return new TransporteListDTO(
            transporte.getId(), 
            transporte.getNomeVeiculo(), 
            transporte.getVagas(), 
            transporte.getTipoVeiculo(),
            transporte.getModelo());
    }
}
