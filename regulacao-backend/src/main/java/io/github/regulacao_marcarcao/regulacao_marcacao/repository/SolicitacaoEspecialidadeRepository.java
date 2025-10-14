package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.ContagemPainelPorDataLocalDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;

public interface SolicitacaoEspecialidadeRepository extends JpaRepository<SolicitacaoEspecialidade, Long> {

    @Query("SELECT se FROM SolicitacaoEspecialidade se " +
           "WHERE se.agendamentoSolicitacao.dataAgendada = :data " +
           "AND se.especialidadeSolicitada.codigo IN :codigos")
    List<SolicitacaoEspecialidade> findAgendadasPorDataECodigos(
            @Param("data") LocalDate data,
            @Param("codigos") Collection<String> codigos
    );

    @Query("SELECT COUNT(DISTINCT se.solicitacao.id) FROM SolicitacaoEspecialidade se " +
           "WHERE se.agendamentoSolicitacao.dataAgendada = :data " +
           "AND se.especialidadeSolicitada.codigo IN :codigos")
    long countDistinctSolicitacoesPorDataECodigos(@Param("data") LocalDate data, @Param("codigos") Collection<String> codigos);

    @Query("SELECT COUNT(se) FROM SolicitacaoEspecialidade se " +
       "WHERE se.agendamentoSolicitacao.dataAgendada = :data " +
       "AND se.especialidadeSolicitada.codigo IN :codigos")
    long countAgendadasPorDataECodigos(@Param("data") LocalDate data, @Param("codigos") Collection<String> codigos);

    @Query("SELECT se FROM SolicitacaoEspecialidade se " +
           "WHERE se.status = :status " +
           "AND se.especialidadeSolicitada.codigo IN :codigos")
    List<SolicitacaoEspecialidade> findByStatusAndEspecialidadeCodigos(
            @Param("status") StatusDaMarcacao status,
            @Param("codigos") Collection<String> codigos
    );

    @Query("SELECT COUNT(se) FROM SolicitacaoEspecialidade se " +
       "WHERE se.status = :status " +
       "AND se.especialidadeSolicitada.codigo IN :codigos")
    long countByStatusAndEspecialidadeCodigos(
            @Param("status") StatusDaMarcacao status,
            @Param("codigos") Collection<String> codigos
    );

    List<SolicitacaoEspecialidade> findByAgendamentoSolicitacaoId(Long agendamentoId);

    @Modifying
    @Query("UPDATE SolicitacaoEspecialidade se SET se.agendamentoSolicitacao = NULL, se.status = 'AGUARDANDO' WHERE se.agendamentoSolicitacao.id = :agendamentoId")
    void desvincularAgendamento(@Param("agendamentoId") Long agendamentoId);

    @Query("SELECT se FROM SolicitacaoEspecialidade se " +
        "JOIN FETCH se.solicitacao s " +
        "WHERE se.agendamentoSolicitacao.dataAgendada = :data " +
        "AND se.especialidadeSolicitada.codigo IN :codigos " +
        "ORDER BY s.nomePaciente, se.agendamentoSolicitacao.turno")
    List<SolicitacaoEspecialidade> findAgendadasCompletasPorDataECodigos(
        @Param("data") LocalDate data,
        @Param("codigos") Collection<String> codigos
    );

    List<SolicitacaoEspecialidade> findByStatus(StatusDaMarcacao status);

    @Query("SELECT new io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.ContagemPainelPorDataLocalDTO(" +
       "se.especialidadeSolicitada.nome, " +
       "CASE WHEN ag.localAgendamento IS NOT NULL THEN ag.localAgendamento.nomeLocal ELSE CONCAT('', ag.localAgendado) END, " +
       "ag.dataAgendada, " +
       "COUNT(se)) " +
       "FROM SolicitacaoEspecialidade se " +
       "JOIN se.agendamentoSolicitacao ag " +
       "WHERE se.status = 'AGENDADO' " +
       "GROUP BY se.especialidadeSolicitada.nome, ag.localAgendamento.nomeLocal, ag.localAgendado, ag.dataAgendada")
    List<ContagemPainelPorDataLocalDTO> contarAgendamentosAgrupados();
}
