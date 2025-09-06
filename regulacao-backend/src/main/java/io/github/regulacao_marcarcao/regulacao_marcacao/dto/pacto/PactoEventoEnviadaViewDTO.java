package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoEvento;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoEventoStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PactoEventoEnviadaViewDTO(
        UUID eventoUuid,
        Long pactoId,
        String label,
        LocalDateTime publishedAt,
        PactoEventoStatus status,
        String consumidoPorMunicipio,
        LocalDateTime consumidoAt
) {
    public static PactoEventoEnviadaViewDTO from(PactoEvento e) {
        return new PactoEventoEnviadaViewDTO(
                e.getUuid(),
                e.getPacto().getId(),
                e.getLabel(),
                e.getPublishedAt(),
                e.getStatus(),
                e.getConsumidoPorMunicipio(),
                e.getConsumidoAt()
        );
    }
}

