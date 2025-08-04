package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import java.time.LocalDate;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.LocalDeAgendamentoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TurnoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agendamento_solicitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoSolicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "local_agendado", nullable = false)
    private LocalDeAgendamentoEnum localAgendado;
    
    @Column(name = "data_agendada", nullable = false)
    private LocalDate dataAgendada;
    
    @Column(name = "observacoes", length = 500)
    private String observacoes;
    

    @ManyToOne()
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    @Column(name = "turno")
    @Enumerated(EnumType.STRING)
    private TurnoEnum turno;

}
