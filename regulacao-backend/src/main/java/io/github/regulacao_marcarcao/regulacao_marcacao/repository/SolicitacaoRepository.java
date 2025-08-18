package io.github.regulacao_marcarcao.regulacao_marcacao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;


public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, JpaSpecificationExecutor<Solicitacao> {

                boolean existsByCpfPaciente(String cpf);
  
                  @Query(value = "SELECT * FROM solicitacao s WHERE regexp_replace(s.cpf_paciente, '[^0-9]', '', 'g') = :cpf", nativeQuery = true)
    List<Solicitacao> findByCpfPacienteSemPonto(@Param("cpf") String cpf);
  
}
