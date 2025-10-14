package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.motorista.MotoristaViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Motorista;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MotoristaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MotoristaService {

    private final MotoristaRepository motoristaRepository;

    @Transactional
    public MotoristaViewDTO criar(MotoristaCreateDTO dto) {
        Motorista motorista = new Motorista();
        motorista.setNome(dto.nome());
        motorista.setTelefone(dto.telefone());
        motorista.setObservacoes(dto.observacoes());
        return MotoristaViewDTO.fromEntity(motoristaRepository.save(motorista));
    }

    @Transactional
    public MotoristaViewDTO atualizar(Long id, MotoristaUpdateDTO dto) {
        Motorista motorista = motoristaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado."));
        motorista.setNome(dto.nome());
        motorista.setTelefone(dto.telefone());
        motorista.setObservacoes(dto.observacoes());
        return MotoristaViewDTO.fromEntity(motoristaRepository.save(motorista));
    }

    @Transactional
    public void remover(Long id) {
        Motorista motorista = motoristaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado."));
        motoristaRepository.delete(motorista);
    }

    @Transactional
    public MotoristaViewDTO buscarPorId(Long id) {
        return motoristaRepository.findById(id)
            .map(MotoristaViewDTO::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado."));
    }

    @Transactional
    public List<MotoristaListDTO> listar() {
        return motoristaRepository.findAll().stream()
            .map(MotoristaListDTO::fromEntity)
            .toList();
    }
}
