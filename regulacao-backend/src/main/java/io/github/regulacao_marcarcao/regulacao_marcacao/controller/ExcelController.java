package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/exportar")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/planilha")
    public ResponseEntity<InputStreamResource> exportarPlanilha(
            @RequestParam String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) throws IOException {

        ByteArrayInputStream bais = excelService.gerarPlanilhaAgendamentos(tipo, data);

        HttpHeaders headers = new HttpHeaders();
        String filename = "planilha_" + tipo.toLowerCase() + "_" + data.toString() + ".xlsx";
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bais));
    }


    @GetMapping("/verificar-dados")
    public ResponseEntity<Map<String, Boolean>> verificarDados(
            @RequestParam String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        boolean dadosDisponiveis = excelService.haDadosParaRelatorio(tipo, data);
        return ResponseEntity.ok(Map.of("dadosDisponiveis", dadosDisponiveis));
    }

}