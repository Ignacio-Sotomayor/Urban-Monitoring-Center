package com.model.Devices;

import com.DAO.SecurityNoticeDAO;
import com.model.Service;

import java.io.Serial;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

public class SecurityCamera extends Device{

    @Serial
    private static final long serialVersionUID = 8877992666445631151L;

    public SecurityCamera(String address, Location location, boolean state) {
        super(address, location,state);
    }

    public void issueSecurityNotice(String description, Set<Service> services){
        SecurityNoticeDAO securityNoticeDao = new SecurityNoticeDAO();
        try {
            securityNoticeDao.insertSecurityNotice(description, Timestamp.from(Instant.now()),super.getAddress(),super.getLocation().getLatitude(),super.getLocation().getLongitude(), super.getId().toString(),services);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}