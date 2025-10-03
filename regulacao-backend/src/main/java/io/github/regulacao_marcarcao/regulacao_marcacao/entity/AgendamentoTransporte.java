package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusAgendamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table (name = "agendamento_transporte")
public class AgendamentoTransporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "agendamento_transporte_solicitacao",
        joinColumns = @JoinColumn(name = "agendamento_transporte_id", nullable = false),
        inverseJoinColumns  = @JoinColumn(name = "solicitacao_id")
    )
    private Set<Solicitacao> solicitacoes;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transporte_id", nullable = false)
    private Transporte transporte;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(
        name = "agendamento_transporte_local_agendamento",
        joinColumns = @JoinColumn(name = "agendamento_transporte_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "local_agendamento_id")
    )
    private List<LocalAgendamento> locaisAgendamento;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

    @Column(name = "data_agendamento_transporte", nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento", nullable = false)
    private StatusAgendamento status;
}
