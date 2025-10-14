package io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Motorista;

public record MotoristaViewDTO(
    Long id,
    String nome,
    String telefone,
    String observacoes
) {
    public static MotoristaViewDTO fromEntity(Motorista motorista) {
        if (motorista == null) {
            return null;
        }
        return new MotoristaViewDTO(
            motorista.getId(),
            motorista.getNome(),
            motorista.getTelefone(),
            motorista.getObservacoes()
        );
    }
}
