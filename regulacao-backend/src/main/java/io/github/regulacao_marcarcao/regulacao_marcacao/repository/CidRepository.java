package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.CID;

public interface CidRepository extends JpaRepository<CID, Long> {
    
    List<CID> findByCodigoContainingIgnoreCase(String codigo); 
}
