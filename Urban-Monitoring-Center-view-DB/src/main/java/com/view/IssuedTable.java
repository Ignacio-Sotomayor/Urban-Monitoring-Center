package com.view;

import com.DAO.FineDAO;
import com.model.Fines.Fine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class IssuedTable extends JTable implements Observer {
    private final DefaultTableModel model;

    public IssuedTable(String[] columnNames){
        model = new DefaultTableModel(columnNames,0);
        setModel(model);
    }
    public void onFineIssued(Fine f) {
        SwingUtilities.invokeLater(() -> {
            model.addRow(new Object[]{f.getAutomobile().getLicensePlate(), f.getInfractionType().getName(), f.getAmount(),f.getScoring(),f.getEventGeolocation().getLocation().toString()});
        });
    }
    @Override
    public void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(() -> {
            FineDAO fineDao = new FineDAO();
            Iterator<Fine> fines = null ;
            try {
                fines = fineDao.getNRecentFines(15);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                for (Iterator<Fine> it = fines; it.hasNext(); ) {
                    Fine f = it.next();
                    model.addRow(new Object[]{f.getAutomobile().getLicensePlate(), f.getInfractionType().getName(), f.getAmount(), f.getScoring(), f.getEventGeolocation().getLocation().toString()});
                }
            }
        });
    }
}