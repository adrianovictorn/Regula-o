package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoSolicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;

public interface AgendamentoSolicitacaoRepository extends JpaRepository<AgendamentoSolicitacao, Long> {
    

        List<AgendamentoSolicitacao> findBySolicitacaoId(Long solicitacaoId);

 

}
