package io.github.regulacao_marcarcao.regulacao_marcacao.repository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findTop20ByMunicipioDestinoIdAndLidaOrderByCreatedAtDesc(UUID municipioDestinoId, boolean lida);
    List<Notificacao> findTop50ByMunicipioDestinoIdAndLidaOrderByCreatedAtDesc(UUID municipioDestinoId, boolean lida);
    long countByMunicipioDestinoIdAndLida(UUID municipioDestinoId, boolean lida);
}
