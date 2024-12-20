package com.example.gestionplatos.service;

import com.example.gestionplatos.model.Plato;
import com.example.gestionplatos.repository.PlatoRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PlatoService {

    private final PlatoRepository repository;

    public PlatoService(PlatoRepository repository) {
        this.repository = repository;
    }

    // Métodos CRUD
    public List<Plato> listarPlatos() {
        return repository.findAll();
    }

    public Optional<Plato> buscarPlatoPorId(Long id) {
        return repository.findById(id);
    }

    public Plato guardarPlato(Plato plato) {
        return repository.save(plato);
    }

    public void eliminarPlato(Long id) {
        repository.deleteById(id);
    }

    // Generar Reporte PDF
    public void generarReportePdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=platos_reporte.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Reporte de Platos").setBold().setFontSize(18));

        Table table = new Table(4); // Ajusta las columnas según el modelo
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Categoría");
        table.addCell("Precio");

        List<Plato> platos = listarPlatos();
        for (Plato plato : platos) {
            table.addCell(plato.getId_plato().toString());
            table.addCell(plato.getNombre());
            table.addCell(plato.getCategoria());
            table.addCell(String.valueOf(plato.getPrecio())); // Convierte el precio a String
        }

        document.add(table);
        document.close();
    }

    // Generar Reporte Excel
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=platos_reporte.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Platos");

        // Crear encabezado
        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID", "Nombre", "Categoría", "Precio"};
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Llenar datos
        List<Plato> platos = listarPlatos();
        int rowIndex = 1;
        for (Plato plato : platos) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(plato.getId_plato());
            row.createCell(1).setCellValue(plato.getNombre());
            row.createCell(2).setCellValue(plato.getCategoria());
            row.createCell(3).setCellValue(plato.getPrecio());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
