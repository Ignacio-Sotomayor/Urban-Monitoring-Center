package com.view.Reports;

import com.DAO.FineDAO;
import com.DAO.SecurityNoticeDAO;
import com.controller.UrbanMonitoringCenter;
import com.model.Devices.Device;
import com.model.Fines.Fine;
import com.model.SecurityNotice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

public class DeviceEventsWindow extends JFrame {

    private JTextField addressField;
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public DeviceEventsWindow() {
        super("Device Events");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: ingreso de dirección
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Device Address:"));
        addressField = new JTextField(30);
        inputPanel.add(addressField);

        JButton searchButton = new JButton("Search");
        inputPanel.add(searchButton);
        add(inputPanel, BorderLayout.NORTH);

        // Tabla para mostrar resultados
        String[] columnNames = {"Event Type", "Date/Time", "Address", "Detail"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventsTable = new JTable(tableModel);
        add(new JScrollPane(eventsTable), BorderLayout.CENTER);

        // Acción del botón Buscar
        searchButton.addActionListener(e -> {
            String address = addressField.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a device address.");
                return;
            }

            try {

                if (address != null) {
                    loadDeviceEvents(String.valueOf(address));
                }else
                    JOptionPane.showMessageDialog(this, "No device found at this address.");



            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });
    }

    // Carga los eventos (multas y avisos) generados por el dispositivo
    private void loadDeviceEvents(String deviceAddress) throws SQLException {
        tableModel.setRowCount(0);

        UrbanMonitoringCenter center = UrbanMonitoringCenter.getUrbanMonitoringCenter();
        Device device = null;
        for (Device d : center.getDevices().values()) {
            if (d.getLocation() != null &&
                    d.getAddress().equalsIgnoreCase(deviceAddress)) {
                device = d;
                break;
            }
        }

        if (device == null) {
            JOptionPane.showMessageDialog(this, "No se encontró un dispositivo con esa dirección.");
            return;
        }

        // === Multas del dispositivo ===
        FineDAO finesDAO = new FineDAO();
        Set<Fine> fines = finesDAO.getNFinesByDeviceUUIDBetweenDates(
                Timestamp.valueOf("2000-01-01 00:00:00"),
                Timestamp.valueOf("2100-01-01 00:00:00"),
                Integer.MAX_VALUE,
                0,
                device.getId()
        );

        for (Fine fine : fines) {
            tableModel.addRow(new Object[]{
                    "Fine",
                    fine.getEventGeolocation().getDateTime(),
                    fine.getEventGeolocation().getAddress(),
                    fine.getInfractionType().getName()
            });
        }

        // === Avisos de seguridad del dispositivo ===
        SecurityNoticeDAO noticesDAO = new SecurityNoticeDAO();
        Set<SecurityNotice> notices = noticesDAO.getAllSecurityNoticesFromDevice(deviceAddress);

        for (SecurityNotice notice : notices) {
            tableModel.addRow(new Object[]{
                    "Security Notice",
                    notice.getEventGeolocation().getDateTime(),
                    notice.getEventGeolocation().getAddress(),
            });
        }

        if (fines.isEmpty() && notices.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron eventos para este dispositivo.");
        }
    }
}