package com.view;

import com.DAO.FineDAO;
import com.controller.RegistryNotifier;
import com.model.Fines.Fine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Iterator;

public class IssuedTable extends JTable {
    private final DefaultTableModel model;

    public IssuedTable(String[] columnNames){
        model = new DefaultTableModel(columnNames,0);
        setModel(model);
        RegistryNotifier.getNotifier().setIssuedTable(this);
    }
    public void onFineIssued(Fine f) {
        SwingUtilities.invokeLater(() -> {
            model.addRow(new Object[]{f.getAutomobile().getLicensePlate(), f.getInfractionType().getName(), f.getAmount(),f.getScoring(),f.getEventGeolocation().getLocation().toString()});
        });
    }

    public void update() {
        SwingUtilities.invokeLater(() -> {
            FineDAO fineDao = new FineDAO();
            Iterator<Fine> fines = null ;
            try {
                fines = fineDao.getNRecentFines(15);
                for (Iterator<Fine> it = fines; it.hasNext(); ) {
                    Fine f = it.next();
                    model.addRow(new Object[]{f.getAutomobile().getLicensePlate(), f.getInfractionType().getName(), f.getAmount(), f.getScoring(), f.getEventGeolocation().getLocation().toString()});
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}