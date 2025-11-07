package com.view;

import com.DAO.FineDAO;
import com.model.Fines.Fine;
import com.model.PDFGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;

public class FinesTable extends IssuedTable {
    private Map<Integer, Fine> finesMap = new HashMap<>();

    public FinesTable(String[] columnNames) {
        super(columnNames);
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {

                this.getModel().setRowCount(0); // Limpiar tabla
            finesMap.clear();

            FineDAO fineDao = new FineDAO();
            try {
                Iterator<Fine> it = fineDao.getNRecentFines(15);
                int rowIndex = 0;
                while (it.hasNext()) {
                    Fine f = it.next();

                    // Agregar fila con texto "PDF" en la columna del botón
                    getModel().addRow(new Object[]{
                            f.getAutomobile().getLicensePlate(),
                            f.getInfractionType().getName(),
                            f.getAmount(),
                            f.getScoring(),
                            f.getEventGeolocation().getLocation().toString(),
                            "PDF"
                    });

                    finesMap.put(rowIndex, f); // Guardamos el fine por fila
                    rowIndex++;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Configurar columna PDF como botón
            if (getColumn("Imprimir") != null) {
                getColumn("Imprimir").setCellRenderer(new ButtonRenderer());
                getColumn("Imprimir").setCellEditor(new ButtonEditor(new JCheckBox(), finesMap));
            }
        });
    }

    private JTable getTable() {
        return this;
    }

    private javax.swing.table.TableColumn getColumn(String name) {
        return getTable().getColumn(name);
    }

    // === RENDERER ===
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // === EDITOR ===
    public static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private Map<Integer, Fine> finesMap;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox, Map<Integer, Fine> finesMap) {
            super(checkBox);
            this.finesMap = finesMap;
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Fine fine = finesMap.get(currentRow);
                    if (fine != null) {
                        PDFGenerator pdfGen = new PDFGenerator();
                        pdfGen.generatePdf(fine, fine.getFine_Id());
                        JOptionPane.showMessageDialog(button, "PDF generado correctamente: Multa_" + fine.getFine_Id() + ".pdf");
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            button.setText(value == null ? "" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}