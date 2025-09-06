package io.github.regulacao_marcarcao.regulacao_marcacao.config;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Municipio;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InstanceContext {

    private final MunicipioRepository municipioRepository;
    private final String nomeIdentificador;
    private Municipio cachedMunicipio;

    public InstanceContext(MunicipioRepository municipioRepository,
                           @Value("${app.municipio.nome-identificador}") String nomeIdentificador) {
        this.municipioRepository = municipioRepository;
        this.nomeIdentificador = nomeIdentificador;
    }

    public synchronized Municipio getMunicipioLocal() {
        if (cachedMunicipio == null) {
            cachedMunicipio = municipioRepository.findByNome(nomeIdentificador)
                    .orElseThrow(() -> new RuntimeException("Município local não encontrado no banco para o identificador: " + nomeIdentificador));
        }
        return cachedMunicipio;
    }

    public String getNomeIdentificador() {
        return nomeIdentificador;
    }
}

