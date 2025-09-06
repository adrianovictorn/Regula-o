// Versão corrigida e completa
package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusPacto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pactos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

   
    @ManyToOne 
    @JoinColumn(name = "municipio_criador_id")
    private Municipio municipioCriador;

    @Enumerated(EnumType.STRING)
    private StatusPacto status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pacto_membros", 
            joinColumns = @JoinColumn(name = "pacto_id"), 
            inverseJoinColumns = @JoinColumn(name = "municipio_id")
    )
    private Set<Municipio> membros = new HashSet<>();
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = StatusPacto.ATIVO;
        }
    }
}