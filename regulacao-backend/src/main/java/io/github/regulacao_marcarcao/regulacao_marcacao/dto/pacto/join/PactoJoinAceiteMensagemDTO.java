package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.join;

import java.util.UUID;

public record PactoJoinAceiteMensagemDTO(
        UUID token,
        Long pactoId,
        UUID solicitanteMunicipioId,
        String solicitanteNome
) {}

