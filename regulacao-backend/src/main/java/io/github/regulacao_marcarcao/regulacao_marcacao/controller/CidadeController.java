package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.cidadeDTO.CidadeViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.CidadeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cidades")
@RequiredArgsConstructor
public class CidadeController {
    
    private final CidadeService cidadeService;

    @PostMapping("/cadastrar")
    public ResponseEntity<CidadeViewDTO> cadastrarCidade(@RequestBody CidadeCreateDTO dto){
        return ResponseEntity.ok(cidadeService.cadastrarCidade(dto));
    }


    @GetMapping
    public ResponseEntity<List<CidadeListDTO>> listarCidades(){
        return ResponseEntity.ok(cidadeService.listarCidades());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeViewDTO> atualizarCidade(@PathVariable Long id, @RequestBody CidadeUpdateDTO dto){
        return ResponseEntity.ok(cidadeService.atualizarCidade(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCidade(@PathVariable Long id){
        cidadeService.deletarCidade(id);
        return ResponseEntity.noContent().build();
    }


}
