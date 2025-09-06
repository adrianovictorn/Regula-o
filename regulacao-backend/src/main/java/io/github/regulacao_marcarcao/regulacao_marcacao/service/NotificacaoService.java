package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.regulacao_marcarcao.regulacao_marcacao.config.InstanceContext;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Notificacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository repo;
    private final InstanceContext instanceContext;
    private final ObjectMapper objectMapper;

    @Transactional
    public Notificacao criar(String tipo, String resumo, String linkPath, Map<String, Object> payload) {
        Municipio local = instanceContext.getMunicipioLocal();
        Notificacao n = new Notificacao();
        n.setMunicipioDestinoId(local.getId());
        n.setTipo(tipo);
        n.setResumo(resumo);
        n.setLinkPath(linkPath);
        try {
            n.setPayload(payload != null ? objectMapper.writeValueAsString(payload) : null);
        } catch (Exception e) {
            n.setPayload(null);
        }
        n.setLida(false);
        return repo.save(n);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarNaoLidas() {
        Municipio local = instanceContext.getMunicipioLocal();
        return repo.findTop20ByMunicipioDestinoIdAndLidaOrderByCreatedAtDesc(local.getId(), false)
                .stream().map(n -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", n.getId());
                    m.put("tipo", n.getTipo());
                    m.put("resumo", n.getResumo());
                    m.put("linkPath", n.getLinkPath());
                    m.put("createdAt", n.getCreatedAt());
                    return m;
                }).collect(Collectors.toList());
    }

    @Transactional
    public void marcarComoLida(Long id) {
        repo.findById(id).ifPresent(n -> { n.setLida(true); repo.save(n); });
    }

    @Transactional
    public void marcarTodasComoLidas() {
        listarNaoLidas().forEach(m -> marcarComoLida(((Number)m.get("id")).longValue()));
    }
}

