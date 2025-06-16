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
            // ATUALIZADO: Recebe uma lista de tipos (enums)
            @RequestParam List<EspecialidadesEnum> tipos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            // ATUALIZADO: Recebe o label para o nome do arquivo
            @RequestParam String label) throws IOException {

        ByteArrayInputStream bais = excelService.gerarPlanilhaAgendamentos(tipos, data);

        HttpHeaders headers = new HttpHeaders();
        // Nome do arquivo dinâmico com base no label recebido
        String filename = "planilha_" + label.replaceAll("\\s+", "_").toLowerCase() + "_" + data.toString() + ".xlsx";
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bais));
    }


    @GetMapping("/verificar-dados")
    public ResponseEntity<Map<String, Boolean>> verificarDados(
             // ATUALIZADO: Recebe uma lista de tipos (enums)
            @RequestParam List<EspecialidadesEnum> tipos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            // O label não é necessário aqui, mas o frontend o envia, então podemos ignorá-lo
            @RequestParam(required = false) String label) {
        
        boolean dadosDisponiveis = excelService.haDadosParaRelatorio(tipos, data);
        return ResponseEntity.ok(Map.of("dadosDisponiveis", dadosDisponiveis));
    }
}