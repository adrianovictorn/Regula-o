package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Cidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.CidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CidadeService {
    
    private final CidadeRepository cidadeRepository;

    @Transactional
    public CidadeViewDTO cadastrarCidade (CidadeCreateDTO dto){
        Cidade novaCidade = new Cidade();
        novaCidade.setNomeCidade(dto.nomeCidade());
        novaCidade.setCodigoIBGE(dto.codigoIBGE());
        novaCidade.setCep(dto.cep());
        return CidadeViewDTO.fromEntity(cidadeRepository.save(novaCidade));
    }


    public List<CidadeListDTO> listarCidades(){
        List<Cidade> cidadesExistentes  = cidadeRepository.findAll();
        List<CidadeListDTO> listaCidades = cidadesExistentes.stream().map(CidadeListDTO::fromEntity).toList();

        return listaCidades;
    }

    @Transactional
    public CidadeViewDTO atualizarCidade(Long id, CidadeUpdateDTO dto){
        Cidade cidadeExistente = cidadeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada."));
        cidadeExistente.setCodigoIBGE(dto.codigoIBGE());
        cidadeExistente.setNomeCidade(dto.nomeCidade());
        cidadeExistente.setCep(dto.cep());
        return CidadeViewDTO.fromEntity(cidadeRepository.save(cidadeExistente));
    }

    @Transactional
    public void deletarCidade(Long id){
        Cidade cidadeExistente = cidadeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada."));
        cidadeRepository.delete(cidadeExistente);
    }


}

