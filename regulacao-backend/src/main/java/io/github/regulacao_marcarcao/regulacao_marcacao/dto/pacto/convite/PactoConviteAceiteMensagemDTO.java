package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite;

import java.util.UUID;

public record PactoConviteAceiteMensagemDTO(
        java.util.UUID token,
        Long pactoId,
        UUID convidadoMunicipioId,
        String convidadoNome
) {}

