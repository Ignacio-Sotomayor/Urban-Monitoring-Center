package com.view.Reports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Set;

import com.model.Fines.ExcessiveSpeedFine;
import com.model.Fines.Fine;
import com.DAO.AutomobileDAO;
import com.DAO.FineDAO;

public class AutomobileFinesWindow extends JFrame {
    private JTextField licensePlateField;
    private JButton searchButton;
    private JTable finesTable;
    private DefaultTableModel tableModel;

    public AutomobileFinesWindow() {
        setTitle("Multas por Vehículo");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Patente:"));
        licensePlateField = new JTextField(10);
        topPanel.add(licensePlateField);
        searchButton = new JButton("Buscar");
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID Multa", "Fecha y Hora", "Dirección", "Tipo Infracción", "Velocidad", "Límite"};
        tableModel = new DefaultTableModel(columnNames, 0);
        finesTable = new JTable(tableModel);
        add(new JScrollPane(finesTable), BorderLayout.CENTER);

        // Acciones
        searchButton.addActionListener(e -> searchFines());
        licensePlateField.addActionListener(e -> searchFines());
    }

    private void searchFines() {
        String licensePlate = licensePlateField.getText().trim().toUpperCase();

        if (licensePlate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una patente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            AutomobileDAO autoDAO = new AutomobileDAO();
            FineDAO fineDao = new FineDAO();

            // Buscar el ID del automóvil
            int automobileID = autoDAO.getAutomobileIdByLicensePlate(licensePlate);

            // Obtener las multas de ese auto
            Set<Fine> fines = fineDao.getAllFinesFromAutomobile(automobileID);

            // Limpiar y mostrar resultados
            tableModel.setRowCount(0);
            if (fines.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron multas para la patente " + licensePlate, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                ExcessiveSpeedFine esf = null;
                for (Fine f : fines) {
                    String speed = (f.getInfractionType().getClass().getSimpleName().equals("ExcessiveSpeedFine")) ? String.valueOf(esf.getAutomobileSpeed()) : "-";
                    String limit = (f.getInfractionType().getClass().getSimpleName().equals("ExcessiveSpeedFine")) ? String.valueOf(esf.getSpeedLimit()) : "-";

                    tableModel.addRow(new Object[]{
                            f.getFine_Id(),
                            f.getEventGeolocation().getDateTime(),
                            f.getEventGeolocation().getAddress(),
                            f.getInfractionType().getName(),
                            speed,
                            limit
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener las multas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}