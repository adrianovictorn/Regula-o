package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO;

public record LocalAgendamentoUpdateDTO(
    String nomeLocal,
    Long cidadeId,
    String endereco,
    String numero
) {}
