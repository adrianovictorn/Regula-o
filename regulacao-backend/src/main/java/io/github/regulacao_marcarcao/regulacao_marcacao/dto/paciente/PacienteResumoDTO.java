package io.github.regulacao_marcarcao.regulacao_marcacao.dto.paciente;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.UsfEnum;

public record PacienteResumoDTO(
    Long solicitacaoId,
    String nomePaciente,
    String cpfPaciente,
    String usfOrigem
) {
    public PacienteResumoDTO(Long solicitacaoId, String nomePaciente, String cpfPaciente, UsfEnum usfOrigem) {
        this(
            solicitacaoId,
            nomePaciente,
            cpfPaciente,
            usfOrigem != null ? usfOrigem.name() : null
        );
    }
}
