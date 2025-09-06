package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite;

import java.time.LocalDateTime;
import java.util.UUID;

public record PactoConviteMensagemDTO(
        UUID token,
        Long pactoId,
        String pactoNome,
        UUID convidadoMunicipioId,
        UUID remetenteMunicipioId,
        String remetenteNome,
        String mensagem,
        LocalDateTime createdAt
) {}

