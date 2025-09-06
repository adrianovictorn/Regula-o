package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.PactoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Pacto;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusPacto;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MunicipioRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.PactoRepository;
import jakarta.transaction.Transactional;
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

    @Value("${app.municipio.nome-identificador}")
    private String nomeMunicipioLocal;

    @Transactional
    public PactoViewDTO criarPacto(PactoCreateDTO dto) {
        Municipio criador = getMunicipioLocal();

        Pacto pacto = new Pacto();
        pacto.setNome(dto.nome());
        pacto.setDescricao(dto.descricao());
        pacto.setMunicipioCriador(criador);
        pacto.setStatus(StatusPacto.ATIVO);

        var membros = new HashSet<Municipio>();
        // Garante que o criador é membro
        membros.add(criador);
        if (dto.membros() != null && !dto.membros().isEmpty()) {
            List<Municipio> outros = municipioRepository.findAllById(dto.membros());
            membros.addAll(outros);
        }
        pacto.setMembros(membros);

        Pacto salvo = pactoRepository.save(pacto);
        return toView(salvo);
    }

    @Transactional
    public PactoViewDTO adicionarMembros(Long pactoId, List<UUID> membrosIds) {
        Pacto pacto = pactoRepository.findById(pactoId)
                .orElseThrow(() -> new IllegalArgumentException("Pacto não encontrado"));
        List<Municipio> novos = municipioRepository.findAllById(membrosIds);
        pacto.getMembros().addAll(novos);
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
}

