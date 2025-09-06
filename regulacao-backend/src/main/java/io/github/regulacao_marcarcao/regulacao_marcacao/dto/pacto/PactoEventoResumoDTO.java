package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PactoEventoResumoDTO(
        UUID eventoUuid,
        Long pactoId,
        String municipioOrigem,
        java.util.UUID municipioOrigemId,
        String label,
        LocalDateTime publishedAt
) {}
