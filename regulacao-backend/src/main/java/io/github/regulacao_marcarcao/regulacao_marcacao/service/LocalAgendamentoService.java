package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.localAgendamentoDTO.LocalAgendamentoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.LocalAgendamento;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.CidadeRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.LocalAgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalAgendamentoService {

    private final LocalAgendamentoRepository localAgendamentoRepository;
    private final CidadeRepository cidadeRepository;
    
    @Transactional
    public LocalAgendamentoViewDTO cadastrarLocalAgendamento(LocalAgendamentoCreateDTO dto){
        LocalAgendamento novoLocalAgendamento = new LocalAgendamento();
        novoLocalAgendamento.setNomeLocal(dto.nomeLocal());
        novoLocalAgendamento.setCidade(
            dto.cidadeId() != null ? cidadeRepository.findById(dto.cidadeId())
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada.")) : null
        );
        novoLocalAgendamento.setNumero(dto.numero());
        novoLocalAgendamento.setEndereco(dto.endereco());
        return LocalAgendamentoViewDTO.fromEntity(localAgendamentoRepository.save(novoLocalAgendamento));
    }

    public List<LocalAgendamentoListDTO> listarAgendamentoListDTOs(Long cidadeId){
        List<LocalAgendamento> localAgendamentoExistente = cidadeId != null
            ? localAgendamentoRepository.findByCidade_Id(cidadeId)
            : localAgendamentoRepository.findAll();
        List<LocalAgendamentoListDTO> listaLocalAgendamentos = localAgendamentoExistente.stream().map(LocalAgendamentoListDTO::fromEntity).toList();
        return listaLocalAgendamentos;
    }

    @Transactional
    public LocalAgendamentoViewDTO atualizarLocalAgendamento(Long id, LocalAgendamentoUpdateDTO dto){
        LocalAgendamento localAgendamentoExistente = localAgendamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Local de agendamento não encontrado."));
        localAgendamentoExistente.setCidade(
            dto.cidadeId() != null ? cidadeRepository.findById(dto.cidadeId())
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada.")) : null
        );
        localAgendamentoExistente.setNomeLocal(dto.nomeLocal());
        localAgendamentoExistente.setEndereco(dto.endereco());
        localAgendamentoExistente.setNumero(dto.numero());

        return LocalAgendamentoViewDTO.fromEntity(localAgendamentoRepository.save(localAgendamentoExistente));
    }

    @Transactional
    public void deletarLocalAgendamento(Long id){
        LocalAgendamento localAgendamentoExistente = localAgendamentoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Local de agendamento não encontrado."));
        localAgendamentoRepository.delete(localAgendamentoExistente);
    }
}
