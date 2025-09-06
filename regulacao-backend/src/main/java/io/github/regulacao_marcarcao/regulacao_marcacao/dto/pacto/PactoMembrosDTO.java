package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto;

import java.util.List;
import java.util.UUID;

public record PactoMembrosDTO(
        List<UUID> membros
) {}

