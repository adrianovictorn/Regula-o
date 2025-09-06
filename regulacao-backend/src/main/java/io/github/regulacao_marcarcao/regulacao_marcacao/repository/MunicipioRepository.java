package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;

public interface MunicipioRepository extends JpaRepository<Municipio, UUID> {
        Optional<Municipio> findByNome(String nome); 
        Optional<Municipio> findByRabbitQueueName(String rabbitQueueName);
        

}
