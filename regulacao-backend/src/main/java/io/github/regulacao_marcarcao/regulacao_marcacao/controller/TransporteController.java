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

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.transporte.TransporteViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.TransporteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transporte")
@RequiredArgsConstructor
public class TransporteController {
    
    private final TransporteService service;

    @PostMapping("/cadastrar")
    public ResponseEntity<TransporteViewDTO> cadastrarTransporte(@RequestBody TransporteCreateDTO dto){
        return ResponseEntity.ok(service.salvarTransporte(dto));
    }

    @GetMapping
    public ResponseEntity<List<TransporteListDTO>> listarTransportes(){
        return ResponseEntity.ok(service.listarTransporte());
    }

    @PutMapping("{id}")
    public ResponseEntity<TransporteViewDTO> atualizarTransporte(@PathVariable Long id, @RequestBody TransporteUpdateDTO dto ){
        return ResponseEntity.ok(service.atualizarTransporte(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarTransporte(@PathVariable Long id){
        service.deletarTransporte(id);
        return ResponseEntity.noContent().build();
    }
}
