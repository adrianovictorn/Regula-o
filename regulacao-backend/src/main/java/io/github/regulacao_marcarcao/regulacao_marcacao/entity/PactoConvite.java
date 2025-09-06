package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoConviteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pacto_convite")
@Getter
@Setter
@NoArgsConstructor
public class PactoConvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID token;

    @Column(name = "pacto_id_remoto", nullable = false)
    private Long pactoIdRemoto;

    @Column(name = "pacto_nome", nullable = false)
    private String pactoNome;

    @Column(name = "convidado_municipio_id", nullable = false)
    private UUID convidadoMunicipioId;

    @Column(name = "remetente_municipio_id", nullable = false)
    private UUID remetenteMunicipioId;

    @Column(name = "remetente_nome", nullable = false)
    private String remetenteNome;

    @Column
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PactoConviteStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @PrePersist
    void onCreate() {
        if (token == null) token = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = PactoConviteStatus.PENDENTE;
    }
}

