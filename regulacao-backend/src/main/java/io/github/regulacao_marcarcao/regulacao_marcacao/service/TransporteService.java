package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Transporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.TransporteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransporteService {
    
    private final TransporteRepository repository;

    public TransporteViewDTO salvarTransporte(TransporteCreateDTO dto){
        Transporte novoTransporte = new Transporte();

        novoTransporte.setModelo(dto.modelo());
        novoTransporte.setNomeVeiculo(dto.nomeVeiculo());
        novoTransporte.setTipoVeiculo(dto.tipoVeiculo());
        novoTransporte.setVagas(dto.vagas());

        return TransporteViewDTO.fromDTO(repository.save(novoTransporte));
    }

    public List<TransporteListDTO> listarTransporte(){
        List<Transporte> listarTransportes = repository.findAll();
        List<TransporteListDTO> listaDtos = listarTransportes.stream().map(TransporteListDTO::fromDTO).toList();
        return listaDtos;
    }

    public TransporteViewDTO atualizarTransporte(Long id, TransporteUpdateDTO dto){
        Transporte transporteExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("Erro ao encontrar transporte"));
        transporteExistente.setModelo(dto.modelo());
        transporteExistente.setNomeVeiculo(dto.nomeVeiculo());
        transporteExistente.setTipoVeiculo(dto.tipoVeiculo());
        transporteExistente.setVagas(dto.vagas());

        return TransporteViewDTO.fromDTO(repository.save(transporteExistente));
    }

    public void deletarTransporte(Long id){
        Transporte transporteExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("Erro ao encontrar ID"));
        repository.delete(transporteExistente);
    }
}
