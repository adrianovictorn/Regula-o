package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.cid.CIDCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.cid.CIDListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.cid.CIDUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.cid.CIDViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.CID;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.CidRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CidService {

    private final CidRepository repository;

    public CIDViewDTO cadastrarCID (CIDCreateDTO dto){
        CID novoCID = new CID();
        novoCID.setCodigo(dto.codigo());
        novoCID.setDescricao(dto.descricao());

        repository.save(novoCID);

        return CIDViewDTO.fromDTO(novoCID);
    }


    public List<CIDListDTO> listarCIDS(){
        List<CID> listaCids = repository.findAll();
        List<CIDListDTO> listDTOs = listaCids.stream().map(CIDListDTO::fromDTO).toList();
        return listDTOs;
    }

    public CIDViewDTO atualizarCid (Long id, CIDUpdateDTO dto){
        CID cidExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("CID não encontrado"));
        cidExistente.setCodigo(dto.codigo());
        cidExistente.setDescricao(dto.descricao());

        repository.save(cidExistente);
        return CIDViewDTO.fromDTO(cidExistente);
    }

    public void deletarCid(Long id){
        CID cidExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("CID não encontrado"));
        repository.delete(cidExistente);
    }
    

    public List<CIDListDTO> listarCIDsPorCodigo(String codigo){
        List<CID> listar = repository.findByCodigoContainingIgnoreCase(codigo);
        List<CIDListDTO> listDTOs = listar.stream().map(CIDListDTO::fromDTO).toList();

        return listDTOs;
    }
}
