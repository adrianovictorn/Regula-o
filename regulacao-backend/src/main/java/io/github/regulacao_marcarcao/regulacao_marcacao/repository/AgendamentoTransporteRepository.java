package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoTransporte;

public interface AgendamentoTransporteRepository extends JpaRepository<AgendamentoTransporte, Long> {

    Optional<AgendamentoTransporte> findByTransporteIdAndData(Long id, LocalDate date);

    List<AgendamentoTransporte> findByData(LocalDate date);

    List<AgendamentoTransporte> findByTransporteId(Long transporteId);
    
} 
