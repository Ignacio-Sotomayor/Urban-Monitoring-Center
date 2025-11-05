package com.view;

import com.model.Automobile.Automobile;
import com.model.Automobile.MotorVehicleRegistry;
import com.controller.UrbanMonitoringCenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FinesWindow extends JFrame {
    private final JPanel filterPanel;
    private static RegistryNotifier notifier = new RegistryNotifier();

    public static RegistryNotifier getNotifier() {
        return notifier;
    }

    public FinesWindow() {
        super("Fines");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // North sector
        filterPanel = new JPanel(new GridLayout(1, 0, 10, 0)); // 1 row, auto columns
        addFilterButtons();
        add(filterPanel, BorderLayout.NORTH);

        // Center sector
        String[] columnNames = {"Automobile", "Infraction", "Amount", "Scoring", "Location"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        IssuedTable table = new IssuedTable(columnNames);
        notifier.addObserver(table);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addFilterButtons() {
        //Por ahora agrego botones placeholder
        for (int i = 1; i <= 4; i++) {
            JButton btn = new JButton("Button #" + i);

            int filterIndex = i;
            btn.addActionListener(e -> applyFilter("Filter" + filterIndex));

            filterPanel.add(btn);
        }
    }
    private void loadFines(){
    }

    private void applyFilter(String filterType) {
        Automobile a = MotorVehicleRegistry.getMotorVehicleRegistry().getRandomAutomobile();
        UrbanMonitoringCenter.getUrbanMonitoringCenter().getRandomFineIssuerDevice().issueFine(a);
    }
}