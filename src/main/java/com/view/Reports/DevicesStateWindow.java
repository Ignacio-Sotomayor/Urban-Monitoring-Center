package com.view.Reports;

import com.itextpdf.text.DocumentException;
import com.model.Devices.Device;
import com.model.PDFGenerator;
import com.controller.UrbanMonitoringCenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DevicesStateWindow extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private HashMap<UUID, Device> devices;

    public DevicesStateWindow() {
        super("Devices State Report");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Obtener los dispositivos desde el controlador principal
        devices = UrbanMonitoringCenter.getUrbanMonitoringCenter().getDevices();

        // Crear tabla
        String[] columns = {"ID", "Type", "Location", "State"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadDevices();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Exportar a PDF");
        JButton refreshButton = new JButton("Actualizar");
        bottomPanel.add(refreshButton);
        bottomPanel.add(exportButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Acción de actualizar
        refreshButton.addActionListener(e -> {
            model.setRowCount(0);
            loadDevices();
        });

        // Acción de exportar PDF
        exportButton.addActionListener(e -> {
            try {
                PDFGenerator pdfGen = new PDFGenerator();
                pdfGen.generatePdf(devices, "Devices_Report.pdf");
                JOptionPane.showMessageDialog(this, "PDF generado correctamente: Devices_Report.pdf");
            } catch (IOException | DocumentException ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + ex.getMessage());
            }
        });
    }

    private void loadDevices() {
        if (devices == null || devices.isEmpty()) {
            model.addRow(new Object[]{"-", "-", "-", "-"});
            return;
        }

        for (Map.Entry<UUID, Device> entry : devices.entrySet()) {
            Device d = entry.getValue();
            String id = entry.getKey().toString().substring(0, 8);
            String type = d.getClass().getSimpleName();
            String location = d.getLocation() != null ? d.getAddress() : "N/A";
            String state = d.getState() ? "OK" : "FAULTY";
            model.addRow(new Object[]{id, type, location, state});
        }
    }
}