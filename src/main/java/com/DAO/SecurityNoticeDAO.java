package com.DAO;

import com.model.Devices.Location;
import com.model.Fines.EventGeolocation;
import com.model.SecurityNotice;
import com.model.Service;
import com.model.UrbanMonitoringCenter;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Set;
import java.util.UUID;

public class SecurityNoticeDAO {

    public Integer insertSecurityNotice(String description, Timestamp timestamp, String address, BigDecimal latitude, BigDecimal longitude, String UUID, Set<Service> calledServices) throws SQLException{
        String sql = "INSERT INTO SecurityNotices (SecurityNotice_Description, SecurityNotice_Latitude, SecurityNotice_Longitude, SecurityNotice_Address, SecurityNotice_DateTime, Issuer_DeviceUUID) VALUES (?, ?, ?, ?, ?, ?)";
        Integer SecurityNoticeID;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,description);
            pstmt.setBigDecimal(2,latitude);
            pstmt.setBigDecimal(3, longitude);
            pstmt.setString(4,address);
            pstmt.setTimestamp(5,timestamp);
            pstmt.setString(6,UUID);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            SecurityNoticeID = rs.getInt(1);
        }
        ServiceDAO serviceDao = new ServiceDAO();
        SecurityNoticeDetailsDAO detailsDao = new SecurityNoticeDetailsDAO();
        for(Service s: calledServices){
            Integer serviceID = serviceDao.getServiceIdByPhone(s.getName(),s.getPhoneNumber());
            detailsDao.insertSecurityNoticeDetail(SecurityNoticeID, serviceID);
        }
        return SecurityNoticeID;
    }
    public void deleteSecurityNotice(Integer SecurityNoticeID) throws SQLException{
        String sql = "DELETE FROM SecurityNotices WHERE SecurityNotice_ID = ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,SecurityNoticeID);
            pstmt.executeUpdate();
        }
    }
    public SecurityNotice getSecurityNotice(Integer ID) throws SQLException{
        String sql = "SELECT SecurityNotice_Description, GeoLocation_ID FROM SecurityNotices WHERE SecurityNotice_ID = ?";
        String description;
        Timestamp timestamp;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
        String uuid;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, ID);
            ResultSet rs = pstmt.executeQuery();
            description = rs.getString("SecurityNotice_Description");
            latitude = rs.getBigDecimal("SecurityNotice_Latitude");
            longitude = rs.getBigDecimal("SecurityNotice_Longitude");
            address = rs.getString("SecurityNotice_Address");
            timestamp = rs.getTimestamp("SecurityNotice_DateTime");
            uuid = rs.getString("Issuer_DeviceUUID");
        }
        SecurityNoticeDetailsDAO detailsDao = new SecurityNoticeDetailsDAO();

        return new SecurityNotice(description, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(java.util.UUID.fromString(uuid))), detailsDao.getAllServicesForSecurityNotice(ID));
    }
}