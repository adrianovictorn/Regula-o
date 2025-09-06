package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;
// Removido java.util.UUID que não é mais necessário
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value; // Importado para ler application.properties
import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.PactoRegional;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusPacto;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MunicipioRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.PactoRegionalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PactoRegionalService {

    private final PactoRegionalRepository pactoRepository;
    private final MunicipioRepository municipioRepository;

    // Injeta o nome do município local a partir do application.properties
    @Value("${app.municipio.nome-identificador}")
    private String nomeMunicipioLocal;

    @Transactional
    public void solicitarPacto(UUID idMunicipioReceptor) { // Corrigido de UUID para Long
        Municipio solicitante = getMunicipioLocal(); 
        Municipio receptor = municipioRepository.findById(idMunicipioReceptor)
            .orElseThrow(() -> new RuntimeException("Município receptor não encontrado"));

        PactoRegional pacto = new PactoRegional();
        pacto.setSolicitante(solicitante);
        pacto.setReceptor(receptor);
        pacto.setStatus(StatusPacto.PENDENTE);

        pactoRepository.save(pacto);
    }
    
    @Transactional
    public void responderPacto(Long pactoId, boolean aceito) {
        PactoRegional pacto = pactoRepository.findById(pactoId)
            .orElseThrow(() -> new RuntimeException("Pacto não encontrado"));

        if (!pacto.getReceptor().equals(getMunicipioLocal())) {
            throw new SecurityException("Ação não permitida");
        }

        pacto.setStatus(aceito ? StatusPacto.ATIVO : StatusPacto.RECUSADO);
        pactoRepository.save(pacto);
    }
    
    public List<Municipio> listarMunicipiosPactuados() {
        Municipio local = getMunicipioLocal();
        return pactoRepository.findMunicipiosAtivosByMunicipio(local);
    }

    // Método melhorado para buscar o município local de forma dinâmica
    private Municipio getMunicipioLocal() {
        return municipioRepository.findByNome(nomeMunicipioLocal)
            .orElseThrow(() -> new RuntimeException("Configuração de município local inválida: " + nomeMunicipioLocal));
    }
}