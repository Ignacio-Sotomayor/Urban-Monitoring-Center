package com.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.model.Devices.Device;
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

    public void generatePDF(Fine fine, int fineNumber) {
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
            document.add(new Paragraph("Automóvil: " + fine.getAutomobile().getBrand() + " " + fine.getAutomobile().getModel()));
            document.add(new Paragraph("Patente: " + fine.getAutomobile().getLicensePlate()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Tipo de infracción: " + fine.getInfractionType().getDescription()));
            document.add(new Paragraph("Lugar: " + fine.getEventGeolocation().getAddress()));
            document.add(new Paragraph("Fecha/Hora: " + fine.getEventGeolocation().getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(new Paragraph("Valor a pagar: $" + fine.getAmount()));
            document.add(new Paragraph("Puntos a reducir: "));
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


}