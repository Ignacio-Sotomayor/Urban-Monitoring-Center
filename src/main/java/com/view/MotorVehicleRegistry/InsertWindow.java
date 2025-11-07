package com.view.MotorVehicleRegistry;

import com.DAO.AutomobileDAO;
import com.DAO.BrandsDAO;
import com.DAO.ModelsDAO;
import com.DAO.OwnersDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class InsertWindow extends JFrame {


        private final BrandsDAO brandDAO = new BrandsDAO();
        private final ModelsDAO modelsDAO = new ModelsDAO();
        private final OwnersDAO ownerDAO = new OwnersDAO();
        private final AutomobileDAO automobileDAO = new AutomobileDAO();

        public InsertWindow() {
            super("Insert Window");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(4, 1, 12, 12));

            JButton btnModel = new JButton("Insert Model");
            JButton btnOwner = new JButton("Insert Owner");
            JButton btnAutomobile = new JButton("Insert Automobile");
            JButton btnBrand = new JButton("Insert Brand");

            btnModel.addActionListener(e -> insertModel());
            btnOwner.addActionListener(e -> insertOwner());
            btnBrand.addActionListener(e -> insertBrand());
            btnAutomobile.addActionListener(e -> insertAutomobile());

            add(btnModel);
            add(btnOwner);
            add(btnAutomobile);
            add(btnBrand);

            setSize(420, 220);
            setLocationRelativeTo(null);
        }

        private void insertBrand() {
            String name = prompt("Brand name:");
            if (blank(name)) return;
            try {
                int id = brandDAO.insertBrand(name.trim());
                info("Brand inserted. ID=" + id);
            } catch (SQLException ex) { error(ex); }
        }

        private void insertModel() {
            String name = prompt("Model name:");
            String brand = prompt("Brand name: ");
            if (blank(name)) return;
            try {
                modelsDAO.insertModel(name.trim(),brandDAO.getBrand_IdByName(brand));
                info("Model inserted.");
            } catch (SQLException ex) { error(ex); }
        }

        private void insertOwner() {
            String full = prompt("Owner full name:");
            String legalId = prompt("LegalId");
            String address = prompt("Address");
            if (blank(full)) return;
            try {
                ownerDAO.insertOwner(legalId.trim(),full.trim(),address.trim());
                info("Owner inserted");
            } catch (SQLException ex) { error(ex); }
        }

        private void insertAutomobile() {
            String plate = prompt("Plate:");
            if (blank(plate)) return;
            String brandName = prompt("Brand Name:");
            String modelName = prompt("Model name:");
            String ownerLegalId = prompt("Owner legalID:");
            if (blank(brandName) || blank(modelName) || blank(ownerLegalId)) return;
            try {
                automobileDAO.insertAutomobile(
                        plate.trim(),
                        brandDAO.getBrand_IdByName(brandName.trim()),
                        modelsDAO.getModelIdByName(modelName.trim()),
                        ownerDAO.getOwnerIdByLegalID(ownerLegalId.trim())
                );
                info("Automobile inserted.");
            } catch (Exception ex) { error(ex); }
        }


        private String prompt(String msg) {
            return JOptionPane.showInputDialog(this, msg, "");
        }
        private boolean blank(String s){ return s == null || s.trim().isEmpty(); }
        private void info(String m){ JOptionPane.showMessageDialog(this, m, "OK", JOptionPane.INFORMATION_MESSAGE); }
        private void error(Exception e){ JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }

    }