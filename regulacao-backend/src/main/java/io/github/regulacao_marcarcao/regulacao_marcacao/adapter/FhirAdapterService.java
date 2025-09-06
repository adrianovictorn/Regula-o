package io.github.regulacao_marcarcao.regulacao_marcacao.adapter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.regional.NotificacaoRegionalDTO;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.springframework.stereotype.Service;

@Service
public class FhirAdapterService {

    private final FhirContext fhirContext = FhirContext.forR4();

    /**
     * Converte um payload JSON no padrão HL7 FHIR para o DTO de notificação interna.
     * @param fhirPayload String contendo a solicitação em formato FHIR JSON.
     * @param municipioOrigem Nome do município de origem (informado externamente ou por autenticação).
     * @return Um NotificacaoRegionalDTO preenchido.
     */
    public NotificacaoRegionalDTO converterFhirParaNotificacao(String fhirPayload, String municipioOrigem) {
        IParser parser = fhirContext.newJsonParser();
        ServiceRequest serviceRequest = parser.parseResource(ServiceRequest.class, fhirPayload);

        // Extração de dados do FHIR (exemplo)
        // O ideal é usar códigos padronizados (System e Code) em vez de apenas o display text.
        String especialidadeDescricao = serviceRequest.getCategoryFirstRep().getCodingFirstRep().getDisplay();
        
        // Simulação de obtenção de um ID interno para a especialidade
        Long especialidadeId = mapearEspecialidadeParaId(especialidadeDescricao);

        // Mapeia para o novo DTO
        return new NotificacaoRegionalDTO(
            especialidadeId,
            especialidadeDescricao,
            municipioOrigem
        );
    }
    
    /**
     * Método de exemplo para mapear a descrição de uma especialidade para um ID interno.
     * Em um cenário real, isso poderia consultar o banco de dados.
     */
    private Long mapearEspecialidadeParaId(String descricao) {
        // Lógica de mapeamento (ex: switch, consulta ao banco, etc.)
        // Este é um valor fictício.
        if ("CARDIOLOGIA".equalsIgnoreCase(descricao)) {
            return 1L;
        }
        return 0L; // Ou lançar uma exceção se não encontrar
    }
}