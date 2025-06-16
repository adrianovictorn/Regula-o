package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao; // Importar Solicitacao
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
import java.util.List;
import java.util.Map; // Importar Map
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final SolicitacaoEspecialidadeRepository especialidadeRepository;

    /**
     * MÉTODO ATUALIZADO PARA AGRUPAR EXAMES POR PACIENTE
     */
    public ByteArrayInputStream gerarPlanilhaAgendamentos(List<EspecialidadesEnum> tipos, LocalDate data) throws IOException {
        
        if (tipos == null || tipos.isEmpty()) {
            return new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray());
        }

        List<SolicitacaoEspecialidade> especialidadesFiltradas = especialidadeRepository.findAgendadasPorDataEEnums(data, tipos);

        // --- INÍCIO DA NOVA LÓGICA DE AGRUPAMENTO ---
        // 1. Agrupamos a lista de especialidades pelo objeto 'Solicitacao' pai.
        // O resultado é um Mapa onde a chave é a Solicitação (paciente) e o valor é a lista de seus exames.
        Map<Solicitacao, List<SolicitacaoEspecialidade>> agrupadoPorPaciente = especialidadesFiltradas.stream()
                .collect(Collectors.groupingBy(SolicitacaoEspecialidade::getSolicitacao));
        // --- FIM DA NOVA LÓGICA DE AGRUPAMENTO ---


        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = tipos.get(0).getDescricao();
        Sheet sheet = workbook.createSheet(sheetName);

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
        // 2. Iteramos sobre o Mapa agrupado, não mais sobre a lista original.
        for (Map.Entry<Solicitacao, List<SolicitacaoEspecialidade>> entry : agrupadoPorPaciente.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            
            Solicitacao paciente = entry.getKey();
            List<SolicitacaoEspecialidade> examesDoPaciente = entry.getValue();

            // 3. Pegamos os dados do paciente (que agora são únicos por linha)
            row.createCell(0).setCellValue(paciente.getNomePaciente());
            LocalDate dataNasc = paciente.getDataNascimento();
            row.createCell(1).setCellValue(dataNasc != null ? dataNasc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
            row.createCell(2).setCellValue(paciente.getUsfOrigem().name());

            // 4. Mapeamos a lista de exames para seus nomes e juntamos com ", "
            String examesAgrupados = examesDoPaciente.stream()
                    .map(exame -> exame.getEspecialidadeSolicitada().getDescricao())
                    .collect(Collectors.joining(", "));

            row.createCell(3).setCellValue(examesAgrupados);
        }


        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    // O método de verificação não precisa ser alterado.
    public boolean haDadosParaRelatorio(List<EspecialidadesEnum> tipos, LocalDate data) {
        if (tipos == null || tipos.isEmpty()) {
            return false;
        }
        long count = especialidadeRepository.countAgendadasPorDataEEnums(data, tipos);
        return count > 0;
    }
}