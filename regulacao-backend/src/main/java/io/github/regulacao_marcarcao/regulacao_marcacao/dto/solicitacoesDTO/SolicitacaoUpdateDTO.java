package io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO;

import java.time.LocalDate;
import java.util.List;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoSolicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.UsfEnum;

public record SolicitacaoUpdateDTO(
    UsfEnum usfOrigem,
    String nomePaciente,
    String observacoes,
    String cns,
    String telefone,
    LocalDate datanascimento,
    LocalDate dataMalote,
    List<AgendamentoSolicitacao> agendamentoSolicitacaos,
    List<SolicitacaoEspecialidade> solicitacoesEspecialidade
) { }
