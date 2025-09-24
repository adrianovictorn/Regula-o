package io.github.regulacao_marcarcao.regulacao_marcacao.repository.projection;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;

public interface StatusCountProjection {
    StatusDaMarcacao getStatus();
    long getTotal();
}
