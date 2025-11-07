package com.view;

import com.DAO.FineDAO;
import com.controller.RegistryNotifier;
import com.model.Fines.Fine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Iterator;

public abstract class IssuedTable extends JTable {
    private final DefaultTableModel model;

    public IssuedTable(String[] columnNames){
        model = new DefaultTableModel(columnNames,0);
        setModel(model);
        RegistryNotifier.getNotifier().setIssuedTable(this);
    }

    @Override
    public DefaultTableModel getModel() {
        return model;
    }

    public abstract void update() ;

    public void update(String[] columnNames) {
    }
}