package io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Motorista;

public record MotoristaListDTO(
    Long id,
    String nome,
    String telefone
) {
    public static MotoristaListDTO fromEntity(Motorista motorista) {
        if (motorista == null) {
            return null;
        }
        return new MotoristaListDTO(
            motorista.getId(),
            motorista.getNome(),
            motorista.getTelefone()
        );
    }
}
