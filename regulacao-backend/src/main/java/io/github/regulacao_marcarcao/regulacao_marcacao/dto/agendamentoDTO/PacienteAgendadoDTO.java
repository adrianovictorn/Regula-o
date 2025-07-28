package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO;


public record PacienteAgendadoDTO(
    String nomePaciente,
    String procedimento,
    String status,
    Long solicitacaoId // ID para possíveis links/ações
) {
}