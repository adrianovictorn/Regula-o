package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO;

import java.time.LocalDate;
import java.util.List;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.*;

public record SolicitacaoListFiltersDTO(
        String nomePaciente, 
        List<UsfEnum> usfOrigem,
        List<EspecialidadesEnum> especialidadeSolicitada, 
        StatusDaMarcacao status,
        List<PrioridadeDaMarcacaoEnum> prioridade,
        LocalDate datainicio, 
        LocalDate dataFim) {
}
