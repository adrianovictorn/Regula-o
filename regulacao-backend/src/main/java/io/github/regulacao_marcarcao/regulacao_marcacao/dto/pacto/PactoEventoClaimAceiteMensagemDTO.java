package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PactoEventoClaimAceiteMensagemDTO(
        UUID eventoUuid,
        Long pactoId,
        String consumidorMunicipioNome,
        java.util.UUID consumidorMunicipioId,
        LocalDateTime consumidoAt
) {}

