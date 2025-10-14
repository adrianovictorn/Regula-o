package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;

public record SolicitacaoSummaryDTO(Long id, String nomePaciente, String cpf, String telefone, String usfOrigem) {
    public static SolicitacaoSummaryDTO fromEntity(Solicitacao solicitacao){
        return new SolicitacaoSummaryDTO(
            solicitacao.getId(),
            solicitacao.getNomePaciente(),
            solicitacao.getCpfPaciente(),
            solicitacao.getTelefone(),
            solicitacao.getUsfOrigem() != null ? solicitacao.getUsfOrigem().name() : null
        );
    }
}
