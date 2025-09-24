package io.github.regulacao_marcarcao.regulacao_marcacao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PrioridadeDaMarcacaoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.projection.StatusCountProjection;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.projection.UsfPendentesProjection;


public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, JpaSpecificationExecutor<Solicitacao> {

    boolean existsByCpfPaciente(String cpf);

    @Query(value = "SELECT * FROM solicitacao s WHERE regexp_replace(s.cpf_paciente, '[^0-9]', '', 'g') = :cpf", nativeQuery = true)
    List<Solicitacao> findByCpfPacienteSemPonto(@Param("cpf") String cpf);

    @Query("SELECT se.status AS status, COUNT(se) AS total FROM SolicitacaoEspecialidade se GROUP BY se.status")
    List<StatusCountProjection> contarPorStatus();

    @Query("SELECT COUNT(se) FROM SolicitacaoEspecialidade se WHERE se.status = :status AND se.prioridade IN :prioridades")
    long contarPorStatusPrioridades(@Param("status") StatusDaMarcacao status,
                                    @Param("prioridades") List<PrioridadeDaMarcacaoEnum> prioridades);

    @Query("SELECT s.usfOrigem AS usf, COUNT(se) AS total FROM SolicitacaoEspecialidade se JOIN se.solicitacao s "
         + "WHERE se.status = :status GROUP BY s.usfOrigem")
    List<UsfPendentesProjection> contarPorUsfEStatus(@Param("status") StatusDaMarcacao status);
}
