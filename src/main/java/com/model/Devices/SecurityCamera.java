package com.model.Devices;

import com.DAO.SecurityNoticeDAO;
import com.model.Service;

import java.io.Serial;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.HashSet;

public class SecurityCamera extends Device{

    @Serial
    private static final long serialVersionUID = 8877992666445631151L;

    public SecurityCamera(String address, Location location, boolean state) {
        super(address, location,state);
    }

    @Override
    public String getIconPath() {
        String path = (getState())?"/Icons/OperativeSecurityCamera.png" : "/Icons/InoperativeSecurityCamera.png";
        URL resource = getClass().getResource(path);
        return resource != null ? resource.toExternalForm() : "";
    }

    @Override
    public String getDeviceTypeName() {
        return "SecurityCamera";
    }

    @Override
    public String getDeviceSpecificInfo() {
        return ""; // No specific info for a general security camera
    }

    public void issueSecurityNotice(String description, Set<Service> services){
        SecurityNoticeDAO securityNoticeDao = new SecurityNoticeDAO();
        try {
            securityNoticeDao.insertSecurityNotice(description, Timestamp.from(Instant.now()),super.getAddress(),super.getLocation().getLatitude(),super.getLocation().getLongitude(), super.getId().toString(),services);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void triggerSecurityNotice() {
        issueSecurityNotice("Security alert triggered by camera", new HashSet<>());
    }
}