package edu.esprit.utils;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import edu.esprit.entities.Program;
import edu.esprit.services.ServiceProgram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfGenerator {
    public static Document createPDF(String dest) {
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(10, 10, 10, 10);
            return document;
        } catch (IOException e) {
            System.out.println("Error creating PDF: " + e.getMessage());
            return null;
        }
    }

    public static Table generateProgramsTablePdf() {
        PdfFont bold;
        try {
            bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Table table = new Table(new float[]{2, 3, 2, 3});
        table.setWidth(UnitValue.createPercentValue(100));

        // Header
        Cell header = new Cell(1, 5)
                .add(new Paragraph("List of Programs"))
                .setFont(bold)
                .setFontSize(14)
                .setPadding(5)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(140, 221, 8));
        table.addCell(header);

        // Column Titles
        String[] columns = {"Coach Name", "Type", "Duration", "Start Date"};
        for (String column : columns) {
            table.addCell(new Cell().add(new Paragraph(column).setFont(bold)).setBackgroundColor(new DeviceRgb(233, 249, 245)));  // very light green color
        }

        // Data from service
        ServiceProgram serviceProgram = new ServiceProgram();
        List<Program> programs = new ArrayList<>(serviceProgram.getAll());
        for (Program program : programs) {
            table.addCell(program.getCoach().getName());
            table.addCell(program.getType());
            table.addCell(program.getDuree());
            table.addCell(program.getStartDate().toString());
        }

        return table;
    }
}