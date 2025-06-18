package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO;



import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;

public record SolicitacaoEspecialidadePublicViewDTO(
    String especialidadeSolicitada,
    String status,
    String prioridade,
    String localAgendado, // Se houver agendamento, mostra o local
    String dataAgendada // Se houver agendamento, mostra a data
) {
    public static SolicitacaoEspecialidadePublicViewDTO fromSolicitacaoEspecialidade(SolicitacaoEspecialidade se) {
        String local = se.getAgendamentoSolicitacao() != null ? se.getAgendamentoSolicitacao().getLocalAgendado().name() : null;
        String data = se.getAgendamentoSolicitacao() != null ? se.getAgendamentoSolicitacao().getDataAgendada().toString() : null;

        return new SolicitacaoEspecialidadePublicViewDTO(
            se.getEspecialidadeSolicitada() != null ? se.getEspecialidadeSolicitada().getDescricao() : null,
            se.getStatus() != null ? se.getStatus().name() : null,
            se.getPrioridade() != null ? se.getPrioridade().name() : null,
            local,
            data
        );
    }
}