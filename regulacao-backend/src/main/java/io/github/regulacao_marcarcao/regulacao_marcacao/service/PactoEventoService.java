package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.ClaimResultDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoEventoResumoDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PublicarSolicitacaoPactoDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.config.InstanceContext;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Pacto;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoEvento;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoEventoStatus;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.PactoEventoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.PactoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PactoEventoService {

    private final PactoEventoRepository eventoRepository;
    private final PactoRepository pactoRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;
    private final InstanceContext instanceContext;

    @Value("${app.municipio.nome-identificador}")
    private String nomeMunicipioLocal;

    @Transactional
    public PactoEventoResumoDTO publicar(Long pactoId, PublicarSolicitacaoPactoDTO dto) {
        Pacto pacto = pactoRepository.findById(pactoId)
                .orElseThrow(() -> new IllegalArgumentException("Pacto não encontrado"));

        // Registra evento localmente
        PactoEvento evento = new PactoEvento();
        evento.setPacto(pacto);
        evento.setSolicitacaoLocalId(dto.solicitacaoLocalId());
        var local = instanceContext.getMunicipioLocal();
        evento.setMunicipioOrigem(local.getNome());
        try {
            java.lang.reflect.Field f = io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoEvento.class.getDeclaredField("municipioOrigemId");
            // Se o campo existir (após migração), atribui o UUID
            f.setAccessible(true);
            f.set(evento, local.getId());
        } catch (NoSuchFieldException | IllegalAccessException ignore) {}
        evento.setLabel(dto.label());
        evento.setStatus(PactoEventoStatus.PUBLICADO);
        evento.setPublishedAt(LocalDateTime.now());

        evento = eventoRepository.save(evento);

        PactoEventoResumoDTO resumo = toResumo(evento);

        // Envia notificação para todos os membros do pacto (exceto o próprio, que já possui o registro local)
        pacto.getMembros().stream()
                .map(m -> m.getNome())
                .filter(nome -> !nome.equalsIgnoreCase(local.getNome()))
                .forEach(destino -> {
                    String routingKey = String.format("encaminhamento.%s.pacto.%d.nova", destino.toUpperCase(), pactoId);
                    rabbitTemplate.convertAndSend(io.github.regulacao_marcarcao.regulacao_marcacao.config.RabbitMQConfig.EXCHANGE_NAME, routingKey, resumo);
                });

        return resumo;
    }

    @Transactional
    public void registrarEventoRecebido(PactoEventoResumoDTO resumo) {
        // Idempotência: ignora se já existe
        if (eventoRepository.findByUuid(resumo.eventoUuid()).isPresent()) {
            return;
        }
        Pacto pacto = pactoRepository.findById(resumo.pactoId())
                .orElseThrow(() -> new IllegalArgumentException("Pacto não encontrado para evento"));

        PactoEvento evento = new PactoEvento();
        evento.setUuid(resumo.eventoUuid());
        evento.setPacto(pacto);
        evento.setSolicitacaoLocalId(null); // desconhecido no destino
        evento.setMunicipioOrigem(resumo.municipioOrigem());
        evento.setLabel(resumo.label());
        evento.setStatus(PactoEventoStatus.PUBLICADO);
        evento.setPublishedAt(resumo.publishedAt());
        eventoRepository.save(evento);
    }

    @Transactional
    public List<PactoEventoResumoDTO> listarFeed(Long pactoId) {
        return eventoRepository.findByPactoIdAndStatusOrderByPublishedAtDesc(pactoId, PactoEventoStatus.PUBLICADO)
                .stream()
                .map(this::toResumo)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaimResultDTO claim(Long pactoId, UUID eventoUuid) {
        // Tentativa de claim atômica
        int updated = eventoRepository.tryClaim(eventoUuid, instanceContext.getNomeIdentificador(), LocalDateTime.now());
        if (updated == 0) {
            return new ClaimResultDTO(false, "CONFLITO", "Evento já consumido por outro município ou inexistente", null);
        }

        // Caso o evento tenha sido originado localmente, podemos retornar o detalhe imediatamente
        var opt = eventoRepository.findByUuid(eventoUuid);
        if (opt.isPresent()) {
            PactoEvento evento = opt.get();
            if (instanceContext.getNomeIdentificador().equalsIgnoreCase(evento.getMunicipioOrigem()) && evento.getSolicitacaoLocalId() != null) {
                Solicitacao solicitacao = solicitacaoRepository.findById(evento.getSolicitacaoLocalId())
                        .orElse(null);
                if (solicitacao != null) {
                    return new ClaimResultDTO(true, "CONSUMIDO", "Detalhes retornados pelo município de origem", SolicitacaoViewDTO.fromSolicitacao(solicitacao));
                }
            }
        }

        // Caso contrário, o detalhe será compartilhado de forma assíncrona pelo município de origem
        return new ClaimResultDTO(true, "CONSUMIDO", "Claim aceito. Detalhes serão compartilhados de forma assíncrona", null);
    }

    private PactoEventoResumoDTO toResumo(PactoEvento e) {
        return new PactoEventoResumoDTO(
                e.getUuid(),
                e.getPacto().getId(),
                e.getMunicipioOrigem(),
                getMunicipioOrigemIdSafe(e),
                e.getLabel(),
                e.getPublishedAt()
        );
    }

    private java.util.UUID getMunicipioOrigemIdSafe(PactoEvento e) {
        try {
            var f = PactoEvento.class.getDeclaredField("municipioOrigemId");
            f.setAccessible(true);
            Object val = f.get(e);
            return (java.util.UUID) val;
        } catch (Exception ex) {
            return null;
        }
    }
}
