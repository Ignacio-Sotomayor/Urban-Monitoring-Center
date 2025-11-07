package com.view.Reports;

import com.controller.RegistryNotifier;
import com.model.Fines.Fine;
import com.view.IssuedTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class FinesWindow extends JFrame {
    private IssuedTable table;

    public FinesWindow() {
        super("Fines");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Center sector
        String[] columnNames = {"Automobile", "Infraction", "Amount", "Scoring", "Location"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table = new IssuedTable(columnNames);
        table.update();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}