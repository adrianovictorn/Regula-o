package io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TipoVeiculoEnum;

public record TransporteCreateDTO(
    String nomeVeiculo,
    Long vagas,
    TipoVeiculoEnum tipoVeiculo,
    String modelo
) {
    
}
