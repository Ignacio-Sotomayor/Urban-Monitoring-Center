package com.view.MotorVehicleRegistry;

import com.DAO.AutomobileDAO;
import com.DAO.BrandsDAO;
import com.DAO.ModelsDAO;
import com.DAO.OwnersDAO;
import com.model.Automobile.Model;
import com.view.FinesTable;
import com.view.IssuedTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewWindow extends JFrame {

    private final BrandsDAO brandDAO = new BrandsDAO();
    private final ModelsDAO modelDAO = new ModelsDAO();
    private final OwnersDAO ownerDAO = new OwnersDAO();
    private final AutomobileDAO automobileDAO = new AutomobileDAO();
IssuedTable table;

    public ViewWindow() {
        super("View Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 12, 12));

        JButton btnModel = new JButton("Model");
        JButton btnOwner = new JButton("Owner");
        JButton btnAutomobile = new JButton("Automobile");
        JButton btnBrand = new JButton("Brand");

        btnModel.addActionListener(e -> displayModel());
        btnOwner.addActionListener(e -> displayOwner());
        btnBrand.addActionListener(e -> displayBrand());
        btnAutomobile.addActionListener(e -> displayAutomobile());

        add(btnModel);
        add(btnOwner);
        add(btnAutomobile);
        add(btnBrand);

        setSize(420, 220);
        setLocationRelativeTo(null);
    }

    void displayModel() {
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        new ModelsForm().setVisible(true);

    }

    void displayOwner(){
        OwnersDAO ownersDao = new OwnersDAO();

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

    }

    void displayBrand(){
        BrandsDAO brandsDAO = new BrandsDAO();

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Brand", "Models"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);




    }

    void displayAutomobile(){
        AutomobileDAO automobileDAO = new AutomobileDAO();

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"License Plate", "Brand", "Model", "Owner", "Year"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);



    }


}