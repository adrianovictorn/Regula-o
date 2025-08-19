package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO;

import java.util.List;
import java.util.stream.Collectors;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.AgendamentoSolicitacaoSimpleViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.cid.CIDViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.SolicitacaoEspecialidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;

public record SolicitacaoViewDTO(
    Long id,
    String nomePaciente,
    String cpfPaciente,
    String cns,
    String telefone,
    String datanascimento,
    String observacoes,
    String usfOrigem,
    String dataMalote,

    List<CIDViewDTO> cids,
    List<SolicitacaoEspecialidadeViewDTO> especialidades,
    List<AgendamentoSolicitacaoSimpleViewDTO> agendamentos
) {
    public static SolicitacaoViewDTO fromSolicitacao(Solicitacao s) {
        List<SolicitacaoEspecialidadeViewDTO> especs = s.getEspecialidades() != null
            ? s.getEspecialidades().stream()
                  .map(SolicitacaoEspecialidadeViewDTO::fromSolicitacaoEspecialidade)
                  .collect(Collectors.toList())
            : List.of();

        

        List<AgendamentoSolicitacaoSimpleViewDTO> ags = s.getAgendamentos() != null
            ? s.getAgendamentos().stream()
                  .map(AgendamentoSolicitacaoSimpleViewDTO::fromAgendamentoSolicitacao)
                  .collect(Collectors.toList())
            : List.of();

            List<CIDViewDTO> cids = s.getCids() != null
             ? s.getCids().stream()
             .map(CIDViewDTO::fromDTO).collect(Collectors.toList()): List.of();

        return new SolicitacaoViewDTO(
            s.getId(),
            s.getNomePaciente(),
            s.getCpfPaciente(),
            s.getCns(),
            s.getTelefone(),
            s.getDataNascimento() != null ? s.getDataNascimento().toString() : null,
            s.getObservacoes(),
            s.getUsfOrigem() != null ? s.getUsfOrigem().name() : null,
            s.getDataMalote() != null ? s.getDataMalote().toString() : null,
            cids,
            especs,
            ags
        );
    }
}
