package io.github.regulacao_marcarcao.regulacao_marcacao.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;


public interface SolicitacaoEspecialidadeRepository extends JpaRepository<SolicitacaoEspecialidade, Long> {
  @Query("SELECT se FROM SolicitacaoEspecialidade se " +
           "WHERE se.agendamentoSolicitacao.dataAgendada = :data " +
           "AND se.especialidadeSolicitada IN :enums")
    List<SolicitacaoEspecialidade> findAgendadasPorDataEEnums(
            @Param("data") LocalDate data, 
            @Param("enums") List<EspecialidadesEnum> enums
    );

    @Query("SELECT COUNT(se) FROM SolicitacaoEspecialidade se " +
       "WHERE se.agendamentoSolicitacao.dataAgendada = :data " +
       "AND se.especialidadeSolicitada IN :enums")
    long countAgendadasPorDataEEnums(@Param("data") LocalDate data, @Param("enums") List<EspecialidadesEnum> enums);

    @Query("SELECT se FROM SolicitacaoEspecialidade se " +
           "WHERE se.status = :status " +
           "AND se.especialidadeSolicitada IN :enums")
    List<SolicitacaoEspecialidade> findByStatusAndEspecialidadeIn(
            @Param("status") StatusDaMarcacao status,
            @Param("enums") List<EspecialidadesEnum> enums
    );

    @Query("SELECT COUNT(se) FROM SolicitacaoEspecialidade se " +
       "WHERE se.status = :status " +
       "AND se.especialidadeSolicitada IN :enums")
    long countByStatusAndEspecialidadeIn(
            @Param("status") StatusDaMarcacao status,
            @Param("enums") List<EspecialidadesEnum> enums
    );


    List<SolicitacaoEspecialidade> findByAgendamentoSolicitacaoId(Long agendamentoId);

    @Modifying
    @Query("UPDATE SolicitacaoEspecialidade se SET se.agendamentoSolicitacao = NULL, se.status = 'AGUARDANDO' WHERE se.agendamentoSolicitacao.id = :agendamentoId")
    void desvincularAgendamento(@Param("agendamentoId") Long agendamentoId);
}