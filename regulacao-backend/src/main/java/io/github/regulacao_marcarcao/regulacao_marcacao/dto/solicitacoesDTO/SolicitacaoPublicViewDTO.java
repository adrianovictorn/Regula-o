package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.SolicitacaoEspecialidadePublicViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;

public record SolicitacaoPublicViewDTO(
    String nomePaciente,
    String usfOrigem,
    String  dataMalote,
    List <SolicitacaoEspecialidadePublicViewDTO> especialidades
) { public static SolicitacaoPublicViewDTO fromSolicitacao(Solicitacao s) {
        List<SolicitacaoEspecialidadePublicViewDTO> especs = s.getEspecialidades() != null
            ? s.getEspecialidades().stream()
                  .map(SolicitacaoEspecialidadePublicViewDTO::fromSolicitacaoEspecialidade)
                  .collect(Collectors.toList())
            : List.of();

        return new SolicitacaoPublicViewDTO(
            s.getNomePaciente(),
            s.getUsfOrigem() != null ? s.getUsfOrigem().name() : null,
            s.getDataMalote() != null ? s.getDataMalote().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null,
            especs
        );
    }
}
    

