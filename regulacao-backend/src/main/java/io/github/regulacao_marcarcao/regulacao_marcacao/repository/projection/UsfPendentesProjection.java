package io.github.regulacao_marcarcao.regulacao_marcacao.repository.projection;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.UsfEnum;

public interface UsfPendentesProjection {
    UsfEnum getUsf();
    long getTotal();
}
