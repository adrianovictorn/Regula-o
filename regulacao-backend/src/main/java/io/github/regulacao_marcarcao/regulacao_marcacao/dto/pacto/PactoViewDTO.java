package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto;

import java.time.LocalDateTime;
import java.util.List;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusPacto;

public record PactoViewDTO(
    Long id,
    String nome,
    String descricao,
    Municipio municipioCriador,
    StatusPacto status, 
    LocalDateTime createdAt,
    List<Municipio> membros
    
) {
    
}
