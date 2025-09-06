package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cid")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CID {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idCid;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "descricao", length = 100)
    private String descricao;

    @ManyToMany(mappedBy = "cids") 
    private List<Solicitacao> solicitacoes;
}
