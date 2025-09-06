package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.adapter.FhirAdapterService;
import io.github.regulacao_marcarcao.regulacao_marcacao.config.InstanceContext;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.regional.EncaminhamentoRegionalDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.regional.NotificacaoRegionalDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.FilaRegionalService;

@RestController
@RequestMapping("/interoperabilidade")
public class InteroperabilidadeController {

    private final FilaRegionalService filaRegionalService;
    private final FhirAdapterService fhirAdapterService;
    private final String nomeMunicipio; // Campo final para armazenar o valor

    // Injeção via construtor
    public InteroperabilidadeController(FilaRegionalService filaRegionalService,
                                        FhirAdapterService fhirAdapterService,
                                        @Value("${app.municipio.nome-identificador}") String nomeMunicipio,
                                        InstanceContext instanceContext) {
        this.filaRegionalService = filaRegionalService;
        this.fhirAdapterService = fhirAdapterService;
        this.nomeMunicipio = nomeMunicipio; // Valor é recebido e atribuído aqui
    }

    @PostMapping("/encaminhar/{municipioDestino}")
    public ResponseEntity<Void> encaminharParaRegional(
            @PathVariable String municipioDestino,
            @RequestBody EncaminhamentoRegionalDTO dto) {

        filaRegionalService.enviarParaFilaRegional(municipioDestino, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/receber-encaminhamento")
    public ResponseEntity<String> receberEncaminhamento(@RequestBody String fhirMessage) {
        try {
            // Log para verificar a mensagem recebida
            System.out.println("Recebida mensagem FHIR: " + fhirMessage);

            // Obtenha o nome do município de forma segura
            String municipioAtual = this.nomeMunicipio;
            System.out.println("Processando para o município: " + municipioAtual);

            // Converte o payload FHIR para o DTO interno de notificação
            NotificacaoRegionalDTO notificacao = fhirAdapterService.converterFhirParaNotificacao(fhirMessage, municipioAtual);

            // Processa a notificação no município atual (persistência/ação de domínio)
            filaRegionalService.processarNotificacao(notificacao, municipioAtual);

            return ResponseEntity.ok("Mensagem recebida e processada com sucesso por " + municipioAtual);
        } catch (Exception e) {
            // Log do erro
            System.err.println("Erro ao processar mensagem FHIR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao processar a mensagem: " + e.getMessage());
        }
    }
}
