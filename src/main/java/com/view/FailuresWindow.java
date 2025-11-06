package com.view;

import com.model.Devices.*;
import com.model.UrbanMonitoringCenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class FailuresWindow extends JFrame {

    private JTable failuresTable;
    private DefaultTableModel tableModel;
    private Timer updateTimer;

    public FailuresWindow() {
        super("Panel de Fallos");
        setSize(800, 400); // Increased width for the new column
        setLocationRelativeTo(null);

        String[] columnNames = {"Dispositivo", "Tipo de Dispositivo", "Tipo de Fallo", "Fecha y Hora"};
        tableModel = new DefaultTableModel(columnNames, 0);
        failuresTable = new JTable(tableModel);

        add(new JScrollPane(failuresTable), BorderLayout.CENTER);

        startAutoUpdate();
    }

    private void startAutoUpdate() {
        updateTimer = new Timer(true);
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTable();
            }
        }, 0, 2000); // Update every 2 seconds
    }

    private String getDeviceTypeAsString(Device device) {
        if (device instanceof TrafficLightController) return "Semaforo";
        if (device instanceof Radar) return "Radar";
        if (device instanceof ParkingLotSecurityCamera) return "Cámara de Estacionamiento";
        if (device instanceof SecurityCamera) return "Cámara de Seguridad";
        return "Desconocido";
    }

    private void updateTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear existing rows
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (UrbanMonitoringCenter.FailureRecord record : UrbanMonitoringCenter.getUrbanMonitoringCenter().getFailureRecords()) {
                tableModel.addRow(new Object[]{
                    record.getDevice().getAddress(),
                    getDeviceTypeAsString(record.getDevice()),
                    record.getFailureType(),
                    record.getTimestamp().format(formatter)
                });
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        if (updateTimer != null) {
            updateTimer.cancel();
        }
    }
}
