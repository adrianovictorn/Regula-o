package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO;

public record LocalAgendamentoCreateDTO(
    String nomeLocal,
    Long cidadeId,
    String endereco,
    String numero
) {}
