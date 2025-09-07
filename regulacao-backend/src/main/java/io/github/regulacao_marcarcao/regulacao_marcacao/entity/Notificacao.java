package io.github.regulacao_marcarcao.regulacao_marcacao.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "municipio_destino_id", nullable = false)
    private UUID municipioDestinoId;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "resumo", nullable = false)
    private String resumo;

    @Column(name = "link_path")
    private String linkPath;

    @Column(name = "payload", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @Column(name = "lida", nullable = false)
    private boolean lida;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
