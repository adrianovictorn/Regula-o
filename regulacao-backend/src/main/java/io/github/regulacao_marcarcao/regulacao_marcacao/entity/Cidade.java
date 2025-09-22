package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cidade")
public class Cidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo")
    private String codigoIBGE;

    @Column(name = "nome")
    private String nomeCidade;

    @Column(name= "cep")
    private String cep;

    @OneToMany(mappedBy = "cidade")
    private List<LocalAgendamento> localAgendamentos;


   

}
