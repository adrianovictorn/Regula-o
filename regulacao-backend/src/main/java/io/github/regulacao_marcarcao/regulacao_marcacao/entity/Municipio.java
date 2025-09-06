package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "municipio")
@Data
public class Municipio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String rabbitQueueName;

    @Column(nullable = false, unique = true)
    private String cnes;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "api_key", unique = true)
    private String apiKey;

    @Column
    private Boolean discoverable;
}
