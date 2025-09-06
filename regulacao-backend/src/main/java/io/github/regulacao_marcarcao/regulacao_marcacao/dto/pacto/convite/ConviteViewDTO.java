package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoConvite;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoConviteStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConviteViewDTO(
        Long id,
        UUID token,
        Long pactoIdRemoto,
        String pactoNome,
        UUID convidadoMunicipioId,
        UUID remetenteMunicipioId,
        String remetenteNome,
        String mensagem,
        PactoConviteStatus status,
        LocalDateTime createdAt,
        LocalDateTime respondedAt
) {
    public static ConviteViewDTO from(PactoConvite c) {
        return new ConviteViewDTO(
                c.getId(), c.getToken(), c.getPactoIdRemoto(), c.getPactoNome(),
                c.getConvidadoMunicipioId(), c.getRemetenteMunicipioId(), c.getRemetenteNome(),
                c.getMensagem(), c.getStatus(), c.getCreatedAt(), c.getRespondedAt()
        );
    }
}

