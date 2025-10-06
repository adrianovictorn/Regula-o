package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Transporte;

public record TransporteSummaryDTO(Long id,  Long vagas, String nome

) {
    public static TransporteSummaryDTO fromEntity (Transporte transporte){
        return new TransporteSummaryDTO(transporte.getId(),transporte.getVagas(), transporte.getNomeVeiculo());
    }
}
