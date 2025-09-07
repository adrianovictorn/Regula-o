package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum; // Importar o Enum
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
import java.util.List; // Importar List
import java.util.Map;

@RestController
@RequestMapping("/api/exportar")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/planilha")
    public ResponseEntity<InputStreamResource> exportarPlanilha(
            @RequestParam List<EspecialidadesEnum> tipos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam String label) throws IOException {

        ByteArrayInputStream bais = excelService.gerarPlanilhaAgendamentos(tipos, data);

        HttpHeaders headers = new HttpHeaders();
        String filename = "planilha_" + label.replaceAll("\\s+", "_").toLowerCase() + "_" + data.toString() + ".xlsx";
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bais));
    }

    @GetMapping("/planilha-aguardando")
    public ResponseEntity<InputStreamResource> exportarPlanilhaAguardando(
            @RequestParam List<EspecialidadesEnum> tipos,
            @RequestParam String label) throws IOException {

        ByteArrayInputStream bais = excelService.gerarPlanilhaAguardando(tipos);

        HttpHeaders headers = new HttpHeaders();
        String filename = "planilha_pendentes_" + label.replaceAll("\\s+", "_").toLowerCase() + ".xlsx";
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bais));
    }

    @GetMapping("/verificar-dados")
    public ResponseEntity<Map<String, Boolean>> verificarDados(
            @RequestParam List<EspecialidadesEnum> tipos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) String label) {
        
        boolean dadosDisponiveis = excelService.haDadosParaRelatorio(tipos, data);
        return ResponseEntity.ok(Map.of("dadosDisponiveis", dadosDisponiveis));
    }

    @GetMapping("/verificar-dados-aguardando")
    public ResponseEntity<Map<String, Boolean>> verificarDadosAguardando(
            @RequestParam List<EspecialidadesEnum> tipos,
            @RequestParam(required = false) String label) {
        
        boolean dadosDisponiveis = excelService.haDadosParaRelatorioAguardando(tipos);
        return ResponseEntity.ok(Map.of("dadosDisponiveis", dadosDisponiveis));
    }
}
