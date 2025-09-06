package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Pacto;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusPacto;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MunicipioRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.PactoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.PactoConviteRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.PactoJoinRequestRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.PactoConviteStatus;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PactoService {

    private final PactoRepository pactoRepository;
    private final MunicipioRepository municipioRepository;
    private final PactoConviteRepository conviteRepository;
    private final PactoJoinRequestRepository joinRequestRepository;

    @Value("${app.municipio.nome-identificador}")
    private String nomeMunicipioLocal;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public PactoViewDTO criarPacto(PactoCreateDTO dto) {
        Municipio criador = getMunicipioLocal();

        Pacto pacto = new Pacto();
        pacto.setNome(dto.nome());
        pacto.setDescricao(dto.descricao());
        pacto.setMunicipioCriador(criador);
        pacto.setStatus(StatusPacto.ATIVO);

        // Regra: na criação, apenas o criador é membro inicial.
        var membros = new HashSet<Municipio>();
        membros.add(criador);
        pacto.setMembros(membros);

        Pacto salvo = pactoRepository.save(pacto);
        return toView(salvo);
    }

    @Transactional
    public PactoViewDTO adicionarMembros(Long pactoId, List<UUID> membrosIds) {
        Pacto pacto = pactoRepository.findById(pactoId)
                .orElseThrow(() -> new IllegalArgumentException("Pacto não encontrado"));
        List<Municipio> novos = municipioRepository.findAllById(membrosIds);

        // Regra: só pode adicionar ao pacto após aceite (Convite ACEITO ou JoinRequest ACEITO)
        for (Municipio m : novos) {
            boolean already = pacto.getMembros().stream().anyMatch(x -> x.getId().equals(m.getId()));
            if (already) continue;

            boolean aceitoPorConvite = conviteRepository
                    .findByConvidadoMunicipioIdAndStatus(m.getId(), PactoConviteStatus.ACEITO)
                    .stream()
                    .anyMatch(c -> c.getPactoIdRemoto().equals(pactoId));

            boolean aceitoPorJoin = joinRequestRepository
                    .findBySolicitanteMunicipioIdAndStatus(m.getId(), PactoConviteStatus.ACEITO)
                    .stream()
                    .anyMatch(r -> r.getPactoIdRemoto().equals(pactoId));

            if (!(aceitoPorConvite || aceitoPorJoin)) {
                throw new IllegalStateException("Município " + m.getNome() + " só pode ser adicionado após aceite de convite/solicitação.");
            }
            pacto.getMembros().add(m);
        }
        return toView(pactoRepository.save(pacto));
    }

    @Transactional
    public PactoViewDTO removerMembro(Long pactoId, UUID municipioId) {
        Pacto pacto = pactoRepository.findById(pactoId)
                .orElseThrow(() -> new IllegalArgumentException("Pacto não encontrado"));
        pacto.getMembros().removeIf(m -> m.getId().equals(municipioId));
        return toView(pactoRepository.save(pacto));
    }

    @Transactional
    public List<PactoViewDTO> listarMeusPactos() {
        Municipio local = getMunicipioLocal();
        return pactoRepository.findAll().stream()
                .filter(p -> p.getMembros().contains(local) || (p.getMunicipioCriador() != null && p.getMunicipioCriador().equals(local)))
                .map(this::toView)
                .collect(Collectors.toList());
    }

    // Lista todos os pactos locais como catálogo público para discovery
    @Transactional
    public List<PactoViewDTO> listarPublicos() {
        return pactoRepository.findAll().stream().map(this::toView).collect(Collectors.toList());
    }

    // Lista de pactos públicos disponíveis para ingresso (exclui os que já participo ou criei)
    @Transactional
    public List<PactoViewDTO> listarPublicosDisponiveisParaIngresso() {
        Municipio local = getMunicipioLocal();
        return pactoRepository.findAll().stream()
                .filter(p -> (p.getMunicipioCriador() == null || !p.getMunicipioCriador().equals(local)))
                .filter(p -> p.getMembros() == null || !p.getMembros().contains(local))
                .map(this::toView)
                .collect(Collectors.toList());
    }

    private PactoViewDTO toView(Pacto p) {
        return new PactoViewDTO(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getMunicipioCriador(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getMembros().stream().collect(Collectors.toList())
        );
    }

    private Municipio getMunicipioLocal() {
        return municipioRepository.findByNome(nomeMunicipioLocal)
                .orElseThrow(() -> new RuntimeException("Configuração de município local inválida: " + nomeMunicipioLocal));
    }

    /**
     * Cria (se não existir) um espelho local de um pacto remoto com o mesmo ID
     * e garante que o criador e o município local estejam como membros locais.
     */
    @Transactional
    public void espelharPactoRemoto(Long pactoIdRemoto, String pactoNome, UUID criadorMunicipioId, UUID municipioLocalId) {
        if (pactoIdRemoto == null || municipioLocalId == null) return;
        try {
            em.createNativeQuery("INSERT INTO pactos (id, nome, descricao, municipio_criador_id, status, created_at) " +
                    "VALUES (:id, :nome, NULL, :criadorId, :status, NOW()) " +
                    "ON CONFLICT (id) DO NOTHING")
                    .setParameter("id", pactoIdRemoto)
                    .setParameter("nome", pactoNome != null ? pactoNome : ("PACTO-" + pactoIdRemoto))
                    .setParameter("criadorId", criadorMunicipioId)
                    .setParameter("status", StatusPacto.ATIVO.name())
                    .executeUpdate();
        } catch (Exception ignore) {}

        // Garante membros: criador e local
        try {
            em.createNativeQuery("INSERT INTO pacto_membros (pacto_id, municipio_id) " +
                    "SELECT :pid, :mid WHERE NOT EXISTS (SELECT 1 FROM pacto_membros WHERE pacto_id=:pid AND municipio_id=:mid)")
                    .setParameter("pid", pactoIdRemoto)
                    .setParameter("mid", municipioLocalId)
                    .executeUpdate();
        } catch (Exception ignore) {}

        if (criadorMunicipioId != null) {
            try {
                em.createNativeQuery("INSERT INTO pacto_membros (pacto_id, municipio_id) " +
                        "SELECT :pid, :mid WHERE NOT EXISTS (SELECT 1 FROM pacto_membros WHERE pacto_id=:pid AND municipio_id=:mid)")
                        .setParameter("pid", pactoIdRemoto)
                        .setParameter("mid", criadorMunicipioId)
                        .executeUpdate();
            } catch (Exception ignore) {}
        }
    }
}
