package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TipoVeiculoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "transporte")
@Data
public class Transporte {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nomeVeiculo;

    @Column(name = "vagas")
    private Long vagas;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoVeiculoEnum TipoVeiculo;

    @Column(name = "modelo")
    private String modelo;
}
