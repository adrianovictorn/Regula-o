package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoEspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final SolicitacaoEspecialidadeRepository especialidadeRepository;

    public ByteArrayInputStream gerarPlanilhaAgendamentos(String tipo, LocalDate data) throws IOException {
        List<EspecialidadesEnum> enumsCorrespondentes = Arrays.stream(EspecialidadesEnum.values())
                .filter(e -> e.getDescricao().toUpperCase().contains(tipo.toUpperCase()))
                .collect(Collectors.toList());

        if (enumsCorrespondentes.isEmpty()) {
            return new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray());
        }

        List<SolicitacaoEspecialidade> especialidadesFiltradas = especialidadeRepository.findAgendadasPorDataEEnums(data, enumsCorrespondentes);

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tipo + " - " + data.toString());

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] columns = {"NOME", "DATA DE NASCIMENTO", "USF DE ORIGEM", "ESPECIALIDADE/EXAME"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for (SolicitacaoEspecialidade esp : especialidadesFiltradas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(esp.getSolicitacao().getNomePaciente());
            LocalDate dataNasc = esp.getSolicitacao().getDataNascimento();
            row.createCell(1).setCellValue(dataNasc != null ? dataNasc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
            row.createCell(2).setCellValue(esp.getSolicitacao().getUsfOrigem().name());
            row.createCell(3).setCellValue(esp.getEspecialidadeSolicitada().getDescricao());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- NOVO MÉTODO PARA VERIFICAÇÃO ---
    public boolean haDadosParaRelatorio(String tipo, LocalDate data) {
        List<EspecialidadesEnum> enumsCorrespondentes = Arrays.stream(EspecialidadesEnum.values())
                .filter(e -> e.getDescricao().toUpperCase().contains(tipo.toUpperCase()))
                .collect(Collectors.toList());

        if (enumsCorrespondentes.isEmpty()) {
            return false;
        }
        
        long count = especialidadeRepository.countAgendadasPorDataEEnums(data, enumsCorrespondentes);
        return count > 0;
    }
}