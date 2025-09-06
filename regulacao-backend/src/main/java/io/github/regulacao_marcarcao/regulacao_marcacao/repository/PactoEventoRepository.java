package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoEvento;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoEventoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PactoEventoRepository extends JpaRepository<PactoEvento, Long> {

    Optional<PactoEvento> findByUuid(UUID uuid);

    List<PactoEvento> findByPactoIdAndStatusOrderByPublishedAtDesc(Long pactoId, PactoEventoStatus status);

    // Enviadas pelo município local
    List<PactoEvento> findByPactoIdAndMunicipioOrigemIdOrderByPublishedAtDesc(Long pactoId, java.util.UUID municipioOrigemId);
    List<PactoEvento> findByPactoIdAndMunicipioOrigemOrderByPublishedAtDesc(Long pactoId, String municipioOrigem);

    @Modifying
    @Query("UPDATE PactoEvento e SET e.status = 'CONSUMIDO', e.consumidoPorMunicipio = :municipio, e.consumidoAt = :agora " +
            "WHERE e.uuid = :uuid AND e.status = 'PUBLICADO'")
    int tryClaim(@Param("uuid") UUID uuid,
                 @Param("municipio") String municipio,
                 @Param("agora") LocalDateTime agora);
}
