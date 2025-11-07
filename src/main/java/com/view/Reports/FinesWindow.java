package com.view.Reports;

import com.view.FinesTable;
import com.view.IssuedTable;
import com.DAO.FineDAO;
import com.model.Fines.Fine;
import com.model.PDFGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;

public class FinesWindow extends JFrame {
    private IssuedTable table;

    public FinesWindow() {
        super("Fines");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Center sector
        String[] columnNames = {"Automobile", "Infraction", "Amount", "Scoring", "Location", "Imprimir"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table = new FinesTable(columnNames);
        table.update();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // South panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Actualizar");
        JButton printAllButton = new JButton("Imprimir todas");
        bottomPanel.add(refreshButton);
        bottomPanel.add(printAllButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Botón Actualizar
        refreshButton.addActionListener(e -> {
            table = new FinesTable(columnNames);
            table.update();
        });

        // Botón Imprimir todas
        printAllButton.addActionListener(e -> generateAllFinesPDF());
    }

    private void generateAllFinesPDF() {
        try {
            FineDAO fineDao = new FineDAO();

            PDFGenerator pdfGen = new PDFGenerator();
            Set<Fine> setfines = new HashSet<>();
            Iterator<Fine> it = fineDao.getNRecentFines(15);
            while (it.hasNext()) {
                setfines.add(it.next());
            }
            pdfGen.generateFinesReport(setfines);

            JOptionPane.showMessageDialog(this, "PDF generado correctamente en la carpeta 'fines'.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error SQL generando el PDF: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando el PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}