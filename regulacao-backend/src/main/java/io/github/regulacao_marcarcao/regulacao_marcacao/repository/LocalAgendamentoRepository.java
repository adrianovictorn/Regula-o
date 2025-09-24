package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.LocalAgendamento;

public interface LocalAgendamentoRepository extends JpaRepository<LocalAgendamento, Long> {

    Optional<LocalAgendamento> findByEnumValue(String enumValue);
}
