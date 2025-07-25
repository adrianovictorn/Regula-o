package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PrioridadeDaMarcacaoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;
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
@Table(name = "solicitacao_especialidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoEspecialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;
    
    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    private AgendamentoSolicitacao agendamentoSolicitacao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "especialidade_solicitada", nullable = false)
    private EspecialidadesEnum especialidadeSolicitada;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private StatusDaMarcacao status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade")
    private PrioridadeDaMarcacaoEnum prioridade;
}
