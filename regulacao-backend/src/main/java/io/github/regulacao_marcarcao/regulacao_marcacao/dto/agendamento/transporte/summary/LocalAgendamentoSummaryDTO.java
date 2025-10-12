package io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.summary;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.LocalAgendamento;

public record LocalAgendamentoSummaryDTO(
    Long id,
    String nomeLocal,
    String endereco,
    String numero
) {
    public static LocalAgendamentoSummaryDTO fromEntity(LocalAgendamento localAgendamento){
        return new LocalAgendamentoSummaryDTO(localAgendamento.getId(), localAgendamento.getNomeLocal(), 
        localAgendamento.getEndereco(), localAgendamento.getNumero());
    }
}



 

    
   
    
    
    
    

    