package com.view.MotorVehicleRegistry;


import com.DAO.BrandsDAO;
import com.DAO.ModelsDAO;
import com.model.Automobile.Model;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Set;

public class ModelsForm extends JFrame {

    private JTextField modelName, brandName, modelId;



    private JButton confirmButton, deleteButton, cancelButton;

    private JLabel messageLabel;
    private JTable modelTable;
    private DefaultTableModel tableModel;
    private ModelsDAO modelsDao;
    private BrandsDAO BrandDao;

    public ModelsForm() {
        this.modelsDao = new ModelsDAO();
        this.BrandDao = new BrandsDAO();

        setTitle("Models From");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel IdLabel = new JLabel("Model Id: ", JLabel.RIGHT);
        modelId = new JTextField(20);
        JLabel nameLabel = new JLabel("Model Name: ", JLabel.RIGHT);
        modelName = new JTextField(20);
        JLabel brandLabel = new JLabel("Brand Name: ", JLabel.RIGHT);
        brandName = new JTextField(20);


        confirmButton = new JButton("Confirmar");
        deleteButton = new JButton("Eliminar");
        cancelButton = new JButton("Limpiar");


        JPanel horizontalPanel = new JPanel(new GridLayout());
        horizontalPanel.add(modelId);
        horizontalPanel.add(modelId);
        inputPanel.add(horizontalPanel);

        horizontalPanel = new JPanel(new GridLayout());
        horizontalPanel.add(nameLabel);
        horizontalPanel.add(modelName);
        inputPanel.add(horizontalPanel);


        horizontalPanel = new JPanel(new GridLayout());
        horizontalPanel.add(brandLabel);
        horizontalPanel.add(brandName);
        inputPanel.add(horizontalPanel);


        horizontalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        horizontalPanel.add(confirmButton);
        horizontalPanel.add(deleteButton);
        horizontalPanel.add(cancelButton);
        inputPanel.add(horizontalPanel);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelTable = new JTable(tableModel);


        tableModel.addColumn("Model Id");
        tableModel.addColumn("Model Name");
        tableModel.addColumn("Brand Name");


        modelTable.getColumnModel().getColumn(0).setMinWidth(40);
        modelTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        modelTable.getColumnModel().getColumn(1).setMinWidth(40);
        modelTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        modelTable.getColumnModel().getColumn(2).setMinWidth(60);
        modelTable.getColumnModel().getColumn(2).setPreferredWidth(60);



        modelTable.getColumnModel().getColumn(5).setMinWidth(0);
        modelTable.getColumnModel().getColumn(5).setMaxWidth(0);
        modelTable.getColumnModel().getColumn(5).setPreferredWidth(0);

        JScrollPane scrollPane = new JScrollPane(modelTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setBackground(Color.RED);
        messageLabel.setOpaque(false);
        mainPanel.add(messageLabel, BorderLayout.SOUTH);
        mainPanel.setPreferredSize(new Dimension(800,600));
        getContentPane().add(mainPanel);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    int selectedRow = modelTable.getSelectedRow();
                    if (selectedRow == -1)
                        insertModel();
                    else
                        updateModel();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteModel();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        loadModels();

        modelTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = modelTable.getSelectedRow();
                    if (selectedRow != -1) {
                        long modelID = (long) tableModel.getValueAt(selectedRow, 0);
                        String name = (String) tableModel.getValueAt(selectedRow, 1);
                        String BrandName = (String) tableModel.getValueAt(selectedRow, 2);

                        modelId.setText(String.valueOf(modelID));
                        modelName.setText(name);
                        brandName.setText(BrandName);
                    }
                }
            }
        });
        clearFields();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadModels() {
        try {
            tableModel.setRowCount(0);

            Set<Model> models = modelsDao.getAllModels();

            for (Model m : models) {
                int modelID = modelsDao.getModelIdByName(m.getName());
                Object[] row = {modelID, m.getName(), modelsDao.getBrandNameByModelID(modelID)};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error while updating model " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public boolean validateFields() {
        boolean response = false;
        if (modelName.getText().isBlank()) {
            setMessage("Must write a model name");
            modelName.requestFocus();
        }
        else
            response = true;
        return response;
    }

    private void insertModel() {
        String name = modelName.getText();

        try {
            modelsDao.insertModel(name, BrandDao.getBrand_IdByName(brandName.getText()));
            JOptionPane.showMessageDialog(this, "Model inserted correctly");
            clearFields();
            loadModels();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Fail to insert Model: " + ex.getMessage(),
                    "Fail", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateModel() {
        int selectedRow = modelTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please slect a model to update",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelId = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            String Brandname = modelsDao.getBrandNameByModelID(modelId);
            modelsDao.updateModel(modelId,modelName.getText(),BrandDao.getBrand_IdByName(Brandname));

            JOptionPane.showMessageDialog(this, "Model updated correctly");
            clearFields();
            loadModels();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Fail to update model: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setOpaque(true);
    }

    private void deleteModel() {
        int selectedRow = modelTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this model?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            long modelID = (long) tableModel.getValueAt(selectedRow, 0);

            try {
                modelsDao.deleteModel(Integer.valueOf(modelId.getText()));
                JOptionPane.showMessageDialog(this, "Model deleted correctly");
                clearFields();
                loadModels();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Fail to delete model: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        modelName.setText("");
        modelId.setText("");
        brandName.setText("");
        modelTable.clearSelection();
        messageLabel.setText(" ");
        messageLabel.setOpaque(false);
    }
}