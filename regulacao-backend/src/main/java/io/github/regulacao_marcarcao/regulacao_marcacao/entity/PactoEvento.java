package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoEventoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pacto_evento")
@Getter
@Setter
@NoArgsConstructor
public class PactoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pacto_id")
    private Pacto pacto;

    // ID da solicitação no município de origem (pode ser nulo para eventos recebidos)
    @Column(name = "solicitacao_local_id")
    private Long solicitacaoLocalId;

    @Column(name = "municipio_origem", nullable = false, length = 120)
    private String municipioOrigem;

    // Opcional: referência ao UUID do município de origem (se disponível)
    @Column(name = "municipio_origem_id")
    private java.util.UUID municipioOrigemId;

    @Column(name = "label", nullable = false, length = 120)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PactoEventoStatus status;

    @Column(name = "consumido_por_municipio", length = 120)
    private String consumidoPorMunicipio;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @Column(name = "consumido_at")
    private LocalDateTime consumidoAt;

    @PrePersist
    protected void onCreate() {
        if (publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = PactoEventoStatus.PUBLICADO;
        }
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
