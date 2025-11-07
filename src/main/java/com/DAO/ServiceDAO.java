package com.DAO;

import com.model.Service;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceDAO {
    public int insertService(String serviceName, String ServicePhoneNumber) throws SQLException{
        String sql = "INSERT INTO Services (Service_name, Service_PhoneNumber) VALUES (?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,serviceName);
            pstmt.setString(2,ServicePhoneNumber);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return (rs.next())?rs.getInt(1):0;
        }
    }
    public void deleteService(Integer ID)throws SQLException{
        String sql = "DELETE FORM Services WHERE Service_ID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,ID);
            pstmt.executeUpdate();
        }
    }
    public int getServiceIdByPhone(String phoneNumber) throws SQLException {
        String sql = "SELECT Service_ID FROM Services WHERE Service_PhoneNumber = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phoneNumber);

            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("Service_ID") : -1;
        }
    }

    public Service getServiceByID(Integer serviceID)throws SQLException{
        String sql = "Select * FROM Services WHERE Service_ID = ?";

        try( Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, serviceID.toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.next()?Service.getService(rs.getString("Service_PhoneNumber")):null;
        }
    }
}