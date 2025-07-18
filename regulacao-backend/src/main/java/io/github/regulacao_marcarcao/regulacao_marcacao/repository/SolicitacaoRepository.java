package io.github.regulacao_marcarcao.regulacao_marcacao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;


public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, JpaSpecificationExecutor<Solicitacao> {

                boolean existsByCpfPaciente(String cpf);
  
                List<Solicitacao> findByCpfPaciente(String cpf);

}
