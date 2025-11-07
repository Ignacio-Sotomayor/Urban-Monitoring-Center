package com.view.Reports;

import com.view.FinesTable;
import com.view.IssuedTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SecurityNoticeWindow extends JFrame {
    IssuedTable table;

    public SecurityNoticeWindow() {
        super("Security Notices");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Date - Time", "Services Called", "Address"};
        table = new SecurityNoticesTable(columnNames);
        table.update();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        //south pane
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Actualizar");
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Acción de actualizar
        refreshButton.addActionListener(e -> {
            table = new FinesTable(columnNames);
            table.update();

        });
    }

}