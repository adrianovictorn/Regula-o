package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.LocalAgendamento;

public record LocalAgendamentoViewDTO(
    Long id,
    String nomeLocal,
    String endereco,
    String numero,
    CidadeViewDTO cidade,
    String enumValue
) {
    public static LocalAgendamentoViewDTO fromEntity(LocalAgendamento localAgendamento) {
        return new LocalAgendamentoViewDTO(
            localAgendamento.getId(),
            localAgendamento.getNomeLocal(),
            localAgendamento.getEndereco(),
            localAgendamento.getNumero(),
            localAgendamento.getCidade() != null ? CidadeViewDTO.fromEntity(localAgendamento.getCidade()) : null,
            localAgendamento.getEnumValue()
        );
    }
}
