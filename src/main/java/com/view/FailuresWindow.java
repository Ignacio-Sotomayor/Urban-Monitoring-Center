package com.view;

import com.model.Devices.*;
import com.controller.UrbanMonitoringCenter;
import com.model.FailureRecord;

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

    private String getDeviceType(Device device){
        if (device.getDeviceTypeName().equals("TrafficLightController")){
            return "Semaforo";
        } else{
            if (device.getDeviceTypeName().equals("Radar")){
                return "Radar";
            }
            else {
                if (device.getDeviceTypeName().equals("ParkingLotSecurityCamera")) {
                    return "Camara de estacionamiento";
                } else {
                    if (device.getDeviceTypeName().equals("SecurityCamera")) {
                        return "Camara de seguridad";
                    }
                }
            }
        }
        return "desconocido";
    }

    private void updateTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear existing rows
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();
            for (FailureRecord record : umc.getFailureRecords()) {
                tableModel.addRow(new Object[]{
                        record.getDevice().getAddress(),
                        getDeviceType(record.getDevice()),
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
