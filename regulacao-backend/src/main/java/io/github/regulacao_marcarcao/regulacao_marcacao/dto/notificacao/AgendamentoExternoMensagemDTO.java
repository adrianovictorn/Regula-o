package io.github.regulacao_marcarcao.regulacao_marcacao.dto.notificacao;

import java.time.LocalDate;

public record AgendamentoExternoMensagemDTO(
        Long solicitacaoIdDestino,
        String cpfMascarado,
        String nomePaciente,
        String municipioExecutor,
        LocalDate dataAgendada
) {}

