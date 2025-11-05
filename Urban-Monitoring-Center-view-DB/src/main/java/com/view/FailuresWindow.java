package com.view;

import com.model.FailureRecord;
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
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"Dispositivo", "Tipo de Fallo", "Fecha y Hora"};
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

    private void updateTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear existing rows
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (FailureRecord record : UrbanMonitoringCenter.getUrbanMonitoringCenter().getFailureRecords()) {
                tableModel.addRow(new Object[]{
                    record.getDevice().getAddress(),
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
