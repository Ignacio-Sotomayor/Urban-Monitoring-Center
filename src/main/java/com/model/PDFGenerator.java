package com.model;

import com.itextpdf.text.pdf.draw.LineSeparator;
import com.model.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.model.Devices.Device;
import com.model.Fines.ExcessiveSpeedFine;
import com.model.Fines.Fine;

public class PDFGenerator {
    private static final String AGENCY_NAME = "Dirección de Tránsito - Central de Monitoreo Urbano";


    private String generateBarcode(int fineNumber, BigDecimal amount) {
        String finePart = String.format("%06d", fineNumber);

        BigDecimal scaled = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        String amountStr = scaled.toPlainString().replace(".", "");
        amountStr = String.format("%012d", Long.parseLong(amountStr));

        return finePart + amountStr;
    }

    public void generatePdf(Fine fine, int fineNumber) {
        Path outputDir = Paths.get("fines");
        try {
            if (!Files.exists(outputDir)) Files.createDirectories(outputDir);
        } catch (IOException e) {
            System.err.println("No se pudo crear carpeta fines: " + e.getMessage());
            return;
        }

        String filename = String.format("Multa_%06d.pdf", fineNumber);
        Path outputFile = outputDir.resolve(filename);

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile.toFile()));
            document.open();

            Paragraph header = new Paragraph(AGENCY_NAME);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Número de multa: " + String.format("%06d", fineNumber)));
            document.add(new Paragraph("Fecha de emisión: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Titular: " + fine.getAutomobile().getOwner().getFullName()));
            document.add(new Paragraph("DNI: " + fine.getAutomobile().getOwner().getLegalIid()));
            document.add(new Paragraph("Domicilio: " + fine.getAutomobile().getOwner().getAddress()));
            document.add(new Paragraph("Automóvil: " + fine.getAutomobile().getBrand().getName() + " " + fine.getAutomobile().getModel().getName()));
            document.add(new Paragraph("Patente: " + fine.getAutomobile().getLicensePlate()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Tipo de infracción: " + fine.getInfractionType().getDescription()));
            if(fine.getClass().getSimpleName().equals(ExcessiveSpeedFine.class.getSimpleName())){
                document.add(new Paragraph("Velocidad Maxima permitida"+ ((ExcessiveSpeedFine)fine).getSpeedLimit()));
                document.add(new Paragraph("Velocidad del automobile"+ ((ExcessiveSpeedFine)fine).getAutomobileSpeed()));
            }
            document.add(new Paragraph("Lugar: " + fine.getEventGeolocation().getAddress()));
            document.add(new Paragraph("Fecha/Hora: " + fine.getEventGeolocation().getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(new Paragraph("Valor a pagar: $" + fine.getAmount()));
            document.add(new Paragraph("Puntos a reducir: "+ fine.getScoring()));
            document.add(new Paragraph(" "));

            String barcodeValue = generateBarcode(fineNumber, fine.getAmount());
            document.add(new Paragraph("Código de barras: " + barcodeValue));

            Barcode128 barcode = new Barcode128();
            barcode.setCode(barcodeValue);
            Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
            barcodeImage.scalePercent(150);
            barcodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(barcodeImage);

            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generatePdf(HashMap<UUID, Device> devices, String outputPath) throws IOException, DocumentException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(outputPath));
        doc.open();

        // Header
        doc.add(new Paragraph("REPORTE DE DISPOSITIVOS"));
        doc.add(new Paragraph("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        doc.add(new Paragraph(" "));

        int total = devices.size();
        int normal = 0;
        int faulty = 0;

        HashMap<String, Integer> countByType = new HashMap<>();
        // Listado de dispositivos
        for (Map.Entry<UUID, Device> entry : devices.entrySet()) {
            Device d = entry.getValue();
            doc.add(new Paragraph(d.toString()));
            doc.add(new Paragraph("\n"));

            // Contar según el estado
            if (d.getState()) normal++;
            else faulty++;

            String type = d.getClass().getSimpleName();
            countByType.put(type,countByType.getOrDefault(type, 0) + 1);

        }

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Totales:"));

        // Cálculo de porcentajes
        double normalPercentage = total > 0 ? (normal * 100.0 / total) : 0;
        double faultPercentage = total > 0 ? (faulty * 100.0 / total) : 0;

        doc.add(new Paragraph("Funcionamiento normal: " + String.format("%.2f", normalPercentage) + "%"));
        doc.add(new Paragraph("Mal funcionamiento: " + String.format("%.2f", faultPercentage) + "%"));

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Cantidad de dispositivos por tipo:"));
        for (Map.Entry<String, Integer> e : countByType.entrySet()) {
            doc.add(new Paragraph(" - " + e.getKey() + ": " + e.getValue()));
        }

        doc.close();
    }
    public void generateFinesReport(Set<Fine> fines) throws Exception {
        if (fines == null || fines.isEmpty()) {
            throw new Exception("No hay multas disponibles para generar el informe.");
        }

        Path outputDir = Paths.get("fines");
        if (!Files.exists(outputDir)) Files.createDirectories(outputDir);
        Path outputFile = outputDir.resolve("Listado_Multas.pdf");

        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, new FileOutputStream(outputFile.toFile()));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        // === Encabezado general ===
        Paragraph header = new Paragraph("LISTADO DE MULTAS AGRUPADAS POR TIPO", titleFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(new Paragraph("Generado: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
        document.add(new Paragraph(" "));

        // === Agrupar por tipo ===
        Map<String, java.util.List<Fine>> finesByType = new TreeMap<>();
        for (Fine f : fines) {
            finesByType.computeIfAbsent(f.getInfractionType().getName(), k -> new ArrayList<>()).add(f);
        }

        double totalGeneral = 0;
        int totalCount = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        // === Procesar cada grupo ===
        for (String type : finesByType.keySet()) {
            java.util.List<Fine> group = finesByType.get(type);
            group.sort(Comparator.comparing(Fine::getAmount).reversed());

            document.add(new Paragraph("Tipo de infracción: " + type, headerFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.addCell("Patente");
            table.addCell("Titular");
            table.addCell("Fecha/Hora");
            table.addCell("Lugar");
            table.addCell("Importe");

            double subtotal = 0;

            for (Fine f : group) {
                table.addCell(f.getAutomobile().getLicensePlate());
                table.addCell(f.getAutomobile().getOwner().getFullName());
                table.addCell(f.getEventGeolocation().getDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                table.addCell(f.getEventGeolocation().getAddress());
                table.addCell("$" + f.getAmount().toString());

                double amount = f.getAmount().doubleValue();
                subtotal += amount;
                totalGeneral += amount;
                totalCount++;
                if (amount < min) min = amount;
                if (amount > max) max = amount;
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Subtotal (" + group.size() + " multas): $" + String.format("%.2f", subtotal)));
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            document.add(new Paragraph(" "));
        }

        // === Totales generales ===
        document.add(new Paragraph("=== RESUMEN GENERAL ===", headerFont));
        document.add(new Paragraph("Cantidad total de multas: " + totalCount));
        document.add(new Paragraph("Suma total: $" + String.format("%.2f", totalGeneral)));
        if (totalCount > 0) {
            document.add(new Paragraph("Promedio: $" + String.format("%.2f", totalGeneral / totalCount)));
            document.add(new Paragraph("Mínimo: $" + String.format("%.2f", min)));
            document.add(new Paragraph("Máximo: $" + String.format("%.2f", max)));
        }

        document.close();
    }


}