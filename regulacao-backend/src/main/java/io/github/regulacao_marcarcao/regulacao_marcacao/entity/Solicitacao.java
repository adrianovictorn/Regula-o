package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import java.time.LocalDate;
import java.util.List;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.UsfEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "usf_origem", nullable = false)
    private UsfEnum usfOrigem;
    
    @Column(name = "nome_paciente", nullable = false, length = 150)
    private String nomePaciente;

    @Column(name = "cpfPaciente", nullable = false, unique = true,length = 15)
    private String cpfPaciente;

    @ManyToMany
    @JoinTable(name = "solicitacao_cid", 
            joinColumns = @JoinColumn(name = "solicitacao_id"),
            inverseJoinColumns = @JoinColumn(name = "cid_id")
    )
    private List<CID> cids;

    @Column(name = "cns", nullable = false, length = 15)
    private String cns;

    @Column(name = "telefone", nullable = true, length = 15)
    private String telefone;


    @Column(name = "datanascimento")
    private LocalDate dataNascimento;

    @Column(name = "observacoes", length = 500)
    private String observacoes;
    
    @Column(name = "data_malote")
    private LocalDate dataMalote;
    
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    private List<SolicitacaoEspecialidade> especialidades;

    @OneToMany(mappedBy = "solicitacao")
    private List<AgendamentoSolicitacao> agendamentos;
}
