package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.join;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoJoinRequest;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoConviteStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record JoinRequestViewDTO(
        Long id,
        UUID token,
        Long pactoIdRemoto,
        UUID solicitanteMunicipioId,
        String solicitanteNome,
        String mensagem,
        PactoConviteStatus status,
        LocalDateTime createdAt,
        LocalDateTime respondedAt
) {
    public static JoinRequestViewDTO from(PactoJoinRequest r) {
        return new JoinRequestViewDTO(
                r.getId(), r.getToken(), r.getPactoIdRemoto(), r.getSolicitanteMunicipioId(),
                r.getSolicitanteNome(), r.getMensagem(), r.getStatus(), r.getCreatedAt(), r.getRespondedAt()
        );
    }
}

