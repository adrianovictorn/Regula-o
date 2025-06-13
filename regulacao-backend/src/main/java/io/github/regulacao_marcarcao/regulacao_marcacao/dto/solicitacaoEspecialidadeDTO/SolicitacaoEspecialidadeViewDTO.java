package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;

public record SolicitacaoEspecialidadeViewDTO (
    Long id,
    Long solicitacaoId,
    Long agendamentoId,
    String especialidadeSolicitada,
    String status,
    String prioridade
) {
    
    public static SolicitacaoEspecialidadeViewDTO fromSolicitacaoEspecialidade(SolicitacaoEspecialidade solicitacaoEspecialidade) {
        return new SolicitacaoEspecialidadeViewDTO(
            solicitacaoEspecialidade.getId(),
            solicitacaoEspecialidade.getSolicitacao() != null ? solicitacaoEspecialidade.getSolicitacao().getId() : null,
            // Verifica se o agendamento está nulo antes de acessar seu id
            solicitacaoEspecialidade.getAgendamentoSolicitacao() != null ? solicitacaoEspecialidade.getAgendamentoSolicitacao().getId() : null,
            solicitacaoEspecialidade.getEspecialidadeSolicitada() != null ? solicitacaoEspecialidade.getEspecialidadeSolicitada().name() : null,
            solicitacaoEspecialidade.getStatus() != null ? solicitacaoEspecialidade.getStatus().name() : null,
            solicitacaoEspecialidade.getPrioridade() != null ? solicitacaoEspecialidade.getPrioridade().name() : null
        );
    }
}
