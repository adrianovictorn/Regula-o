package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;

public record SolicitacaoEspecialidadePublicViewDTO(
    String especialidadeSolicitada,
    String status,
    String prioridade,
    String localAgendado,
    String dataAgendada
) {
    public static SolicitacaoEspecialidadePublicViewDTO fromSolicitacaoEspecialidade(SolicitacaoEspecialidade se) {
        String local = null;
        if (se.getAgendamentoSolicitacao() != null) {
            if (se.getAgendamentoSolicitacao().getLocalAgendamento() != null) {
                local = se.getAgendamentoSolicitacao().getLocalAgendamento().getNomeLocal();
            } else if (se.getAgendamentoSolicitacao().getLocalAgendado() != null) {
                local = se.getAgendamentoSolicitacao().getLocalAgendado().name().replace('_', ' ');
            }
        }
        String data = se.getAgendamentoSolicitacao() != null && se.getAgendamentoSolicitacao().getDataAgendada() != null
            ? se.getAgendamentoSolicitacao().getDataAgendada().toString()
            : null;

        return new SolicitacaoEspecialidadePublicViewDTO(
            se.getEspecialidadeSolicitada() != null ? se.getEspecialidadeSolicitada().getNome() : se.getEspecialidadeCodigoLegacy(),
            se.getStatus() != null ? se.getStatus().name() : null,
            se.getPrioridade() != null ? se.getPrioridade().name() : null,
            local,
            data
        );
    }
}
