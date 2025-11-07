package com.view.Reports;

import com.DAO.SecurityNoticeDAO;
import com.model.Fines.EventGeolocation;
import com.model.SecurityNotice;
import com.model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SecurityNoticeWindow extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private JLabel summaryLabel;

    public SecurityNoticeWindow() {
        super("Security Notices Report");

        setSize(850, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- PANEL SUPERIOR (Filtros de fecha) ----------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.add(new JLabel("From:"));
        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner = new JSpinner(new SpinnerDateModel());
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "dd/MM/yyyy HH:mm"));
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "dd/MM/yyyy HH:mm"));
        topPanel.add(fromDateSpinner);
        topPanel.add(new JLabel("To:"));
        topPanel.add(toDateSpinner);

        JButton filterButton = new JButton("Generate Report");
        filterButton.addActionListener(this::onGenerateReport);
        topPanel.add(filterButton);

        add(topPanel, BorderLayout.NORTH);

        // ---------- TABLA CENTRAL ----------
        tableModel = new DefaultTableModel(
                new Object[]{"Date & Time", "Address", "Description", "Services"}, 0
        );
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---------- PANEL INFERIOR (Resumen) ----------
        summaryLabel = new JLabel(" ");
        summaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 13));
        add(summaryLabel, BorderLayout.SOUTH);
    }

    private void onGenerateReport(ActionEvent e) {
        try {
            Timestamp from = new Timestamp(((Date) fromDateSpinner.getValue()).getTime());
            Timestamp to = new Timestamp(((Date) toDateSpinner.getValue()).getTime());

            SecurityNoticeDAO dao = new SecurityNoticeDAO();
            Set<SecurityNotice> notices = dao.getAllSecurityNoticesBetweenDates(from, to);

            tableModel.setRowCount(0);

            // ---------- Inicializar contadores por servicio ----------
            Map<Service, Integer> serviceCounts = new EnumMap<>(Service.class);
            for (Service s : Service.values()) {
                serviceCounts.put(s, 0);
            }

            // ---------- Llenar tabla ----------
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (SecurityNotice sn : notices) {
                EventGeolocation eg = sn.getEventGeolocation();

                // Obtener los servicios asociados
                StringBuilder servicesStr = new StringBuilder();
                Iterator<Service> it = sn.getCalledServices();
                while (it.hasNext()) {
                    Service s = it.next();
                    servicesStr.append(s.name());
                    if (it.hasNext()) servicesStr.append(", ");
                    serviceCounts.put(s, serviceCounts.get(s) + 1);
                }

                if (servicesStr.length() == 0)
                    servicesStr.append("None");

                tableModel.addRow(new Object[]{
                        eg.getDateTime().format(formatter),
                        eg.getAddress(),
                        sn.getDescription(),
                        servicesStr.toString()
                });
            }

            // ---------- Calcular totales ----------
            int total = notices.size();

            StringBuilder summary = new StringBuilder("<html><center><b>Totals:</b><br>");
            for (Service s : Service.values()) {
                Integer countObj = serviceCounts.get(s);
                int count = (countObj != null) ? countObj : 0;
                double percent = (total == 0) ? 0.0 : (count * 100.0 / total);

                summary.append(String.format("%s: %s (%.2f%%)<br>", s.name(), count, percent));
            }
            summary.append(String.format("<br><b>Total Notices:</b> %s</center></html>", total));
            summaryLabel.setText(summary.toString());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error retrieving security notices: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}