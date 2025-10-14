package io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista;

import jakarta.validation.constraints.NotBlank;

public record MotoristaCreateDTO(
    @NotBlank String nome,
    String telefone,
    String observacoes
) {}
