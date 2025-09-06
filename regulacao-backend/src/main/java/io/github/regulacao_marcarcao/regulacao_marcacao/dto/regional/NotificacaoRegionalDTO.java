package io.github.regulacao_marcarcao.regulacao_marcacao.dto.regional;

/**
 * DTO para transportar a notificação de um encaminhamento regional.
 * Este record é imutável e carrega os dados essenciais para que o
 * município de destino possa identificar a solicitação.
 *
 * @param especialidadeId      ID da especialidade/procedimento solicitado.
 * @param especialidadeDescricao Descrição textual da especialidade.
 * @param municipioOrigem      Nome do município que originou a solicitação.
 */
public record NotificacaoRegionalDTO(
    Long especialidadeId, 
    String especialidadeDescricao, 
    String municipioOrigem
) {
}