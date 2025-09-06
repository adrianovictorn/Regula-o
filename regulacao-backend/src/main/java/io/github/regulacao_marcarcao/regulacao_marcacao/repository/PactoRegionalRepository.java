package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoRegional;

public interface PactoRegionalRepository extends JpaRepository<PactoRegional, Long> {

@Query("SELECT p.receptor FROM PactoRegional p WHERE p.solicitante = :municipio AND p.status = 'ATIVO' " +
           "UNION " +
           "SELECT p.solicitante FROM PactoRegional p WHERE p.receptor = :municipio AND p.status = 'ATIVO'")
    List<Municipio> findMunicipiosAtivosByMunicipio(@Param("municipio") Municipio municipio);
}    

