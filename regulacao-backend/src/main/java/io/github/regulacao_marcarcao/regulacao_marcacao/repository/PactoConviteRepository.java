package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoConvite;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoConviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PactoConviteRepository extends JpaRepository<PactoConvite, Long> {
    Optional<PactoConvite> findByToken(UUID token);
    List<PactoConvite> findByPactoIdRemoto(Long pactoIdRemoto);
    List<PactoConvite> findByConvidadoMunicipioIdAndStatus(UUID convidadoMunicipioId, PactoConviteStatus status);
}

