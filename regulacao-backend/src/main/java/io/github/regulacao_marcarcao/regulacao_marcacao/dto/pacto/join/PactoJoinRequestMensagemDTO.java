package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.join;

import java.time.LocalDateTime;
import java.util.UUID;

public record PactoJoinRequestMensagemDTO(
        UUID token,
        Long pactoId,
        UUID solicitanteMunicipioId,
        String solicitanteNome,
        String mensagem,
        LocalDateTime createdAt
) {}

