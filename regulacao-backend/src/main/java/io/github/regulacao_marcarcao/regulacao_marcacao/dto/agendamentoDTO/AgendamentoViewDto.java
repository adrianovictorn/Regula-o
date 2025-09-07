package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO;

import java.util.List;
import java.util.stream.Collectors;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.UsfEnum;

public record AgendamentoViewDto(
    Long solicitacaoId,
    String nomePaciente,
    String cpfPaciente,
    UsfEnum usfOrigem,
    List<String> especialidadesPendentes
) {

    public static AgendamentoViewDto fromSolicitacao(Solicitacao solicitacao) {
        List<String> pendentes = solicitacao.getEspecialidades().stream()
            .filter(e -> e.getStatus() == StatusDaMarcacao.AGUARDANDO || e.getStatus() == StatusDaMarcacao.RETORNO || e.getStatus() == StatusDaMarcacao.RETORNO_POLICLINICA)
            .map(e -> e.getEspecialidadeSolicitada() != null ? e.getEspecialidadeSolicitada().getCodigo() : e.getEspecialidadeCodigoLegacy())
            .collect(Collectors.toList());

        return new AgendamentoViewDto(
            solicitacao.getId(),
            solicitacao.getNomePaciente(),
            solicitacao.getCpfPaciente(),
            solicitacao.getUsfOrigem(),
            pendentes
        );
    }
}
