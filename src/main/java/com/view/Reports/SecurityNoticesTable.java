package com.view.Reports;

import com.DAO.FineDAO;
import com.DAO.SecurityNoticeDAO;
import com.model.Fines.EventGeolocation;
import com.model.SecurityNotice;
import com.model.Service;
import com.view.IssuedTable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Iterator;

public class SecurityNoticesTable extends IssuedTable {
    public SecurityNoticesTable(String[] columnNames) {
        super(columnNames);
    }

    @Override
    public void update() {
            SwingUtilities.invokeLater(() -> {
                SecurityNoticeDAO secNot = new SecurityNoticeDAO();
                try {

                    for (SecurityNotice s: secNot.getNSecurityNotice(15)) {
                        EventGeolocation geoLoc = s.getEventGeolocation();
                        StringBuilder strB = new StringBuilder();
                        for (Iterator it = s.getCalledServices(); it.hasNext(); ) {
                            Service service = (Service) it.next();
                            strB.append(service.getName());strB.append(" - ");
                        }
                        super.getModel().addRow(new Object[]{geoLoc.getDateTime().toString(),strB.toString(),geoLoc.getAddress()});
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
}