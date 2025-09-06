package io.github.regulacao_marcarcao.regulacao_marcacao.config;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.text.Normalizer;
import java.util.Locale;

@Component
public class InstanceContext {

    private final MunicipioRepository municipioRepository;
    private final String nomeIdentificador;
    private final String queueName;
    private Municipio cachedMunicipio;

    public InstanceContext(MunicipioRepository municipioRepository,
                           @Value("${app.municipio.nome-identificador}") String nomeIdentificador,
                           @Value("${app.municipio.queue-name}") String queueName) {
        this.municipioRepository = municipioRepository;
        this.nomeIdentificador = nomeIdentificador;
        this.queueName = queueName;
    }

    public synchronized Municipio getMunicipioLocal() {
        if (cachedMunicipio == null) {
            // 1) Por queue-name (estável)
            cachedMunicipio = municipioRepository.findByRabbitQueueName(queueName).orElse(null);
        }
        if (cachedMunicipio == null) {
            // 2) Por nome exato (config)
            cachedMunicipio = municipioRepository.findByNome(nomeIdentificador).orElse(null);
        }
        if (cachedMunicipio == null) {
            // 3) Por nome normalizado (remove acentos, compara case-insensitive)
            String alvo = normalize(nomeIdentificador);
            cachedMunicipio = municipioRepository.findAll().stream()
                    .filter(m -> normalize(m.getNome()).equalsIgnoreCase(alvo))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Município local não encontrado: nomeIdentificador='" + nomeIdentificador + "', queue='" + queueName + "'"));
        }
        return cachedMunicipio;
    }

    public String getNomeIdentificador() {
        return nomeIdentificador;
    }

    public String getQueueName() {
        return queueName;
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}", "").toUpperCase(Locale.ROOT).trim();
    }
}
