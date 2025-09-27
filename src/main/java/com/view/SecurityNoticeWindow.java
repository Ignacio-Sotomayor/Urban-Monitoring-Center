package com.view;

import com.model.Fines.Fine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SecurityNoticeWindow extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public SecurityNoticeWindow() {
        super("Security Notices");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Automobile", "Infraction", "Amount", "Scoring", "Location"};

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }
}

