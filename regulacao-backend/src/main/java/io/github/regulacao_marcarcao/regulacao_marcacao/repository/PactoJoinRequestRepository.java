package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoJoinRequest;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoConviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PactoJoinRequestRepository extends JpaRepository<PactoJoinRequest, Long> {
    Optional<PactoJoinRequest> findByToken(UUID token);
    List<PactoJoinRequest> findByPactoIdRemoto(Long pactoIdRemoto);
    List<PactoJoinRequest> findBySolicitanteMunicipioIdAndStatus(UUID solicitanteMunicipioId, PactoConviteStatus status);
    List<PactoJoinRequest> findBySolicitanteMunicipioId(UUID solicitanteMunicipioId);
}
