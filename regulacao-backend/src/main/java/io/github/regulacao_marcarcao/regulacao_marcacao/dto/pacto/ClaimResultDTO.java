package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoViewDTO;

public record ClaimResultDTO(
        boolean claimed,
        String status,
        String message,
        SolicitacaoViewDTO detalhes
) {}

