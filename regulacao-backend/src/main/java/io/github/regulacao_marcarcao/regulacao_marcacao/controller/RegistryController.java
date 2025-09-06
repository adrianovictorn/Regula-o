package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.municipio.MunicipioViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MunicipioRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registry/municipios")
@RequiredArgsConstructor
public class RegistryController {

    private final MunicipioRepository municipioRepository;

    @PostMapping("/register-public")
    public ResponseEntity<MunicipioViewDTO> registerPublic(@RequestBody Map<String, String> body) {
        String nome = body.getOrDefault("nome", null);
        String cnes = body.getOrDefault("cnes", null);
        String queue = body.getOrDefault("rabbitQueueName", null);
        String baseUrl = body.getOrDefault("baseUrl", null);
        if (nome == null || cnes == null || queue == null) {
            return ResponseEntity.badRequest().build();
        }
        Municipio m = municipioRepository.findByNome(nome).orElse(new Municipio());
        if (m.getId() == null) m.setId(UUID.randomUUID());
        m.setNome(nome);
        m.setCnes(cnes);
        m.setRabbitQueueName(queue);
        m.setBaseUrl(baseUrl);
        m.setDiscoverable(true);
        if (m.getApiKey() == null) m.setApiKey(UUID.randomUUID().toString());
        m = municipioRepository.save(m);
        return ResponseEntity.ok(new MunicipioViewDTO(m.getId(), m.getNome()));
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<MunicipioViewDTO>> disponiveis(@RequestParam(value = "search", required = false) String search) {
        var all = municipioRepository.findAll();
        var filtered = all.stream()
                .filter(m -> m.getDiscoverable() == null || m.getDiscoverable())
                .filter(m -> search == null || m.getNome().toLowerCase().contains(search.toLowerCase()))
                .map(m -> new MunicipioViewDTO(m.getId(), m.getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }
}

