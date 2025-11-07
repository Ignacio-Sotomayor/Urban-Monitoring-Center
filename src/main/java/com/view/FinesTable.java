package com.view;

import com.DAO.FineDAO;
import com.model.Fines.Fine;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Iterator;

public class FinesTable extends IssuedTable {
    public FinesTable(String[] columnNames) {
        super(columnNames);
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            FineDAO fineDao = new FineDAO();
            try {
                for (Iterator<Fine> it = fineDao.getNRecentFines(15); it.hasNext(); ) {
                    Fine f = it.next();
                    super.getModel().addRow(new Object[]{f.getAutomobile().getLicensePlate(), f.getInfractionType().getName(), f.getAmount(), f.getScoring(), f.getEventGeolocation().getLocation().toString()});
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}