package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Motorista;

public record MotoristaSummaryDTO(Long id, String nome, String telefone) {
    public static MotoristaSummaryDTO fromEntity(Motorista motorista) {
        if (motorista == null) {
            return null;
        }
        return new MotoristaSummaryDTO(motorista.getId(), motorista.getNome(), motorista.getTelefone());
    }
}
