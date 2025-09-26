package com.DAO;

import com.model.Service;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceDAO {
    public static Integer insertService(String serviceName, String ServicePhoneNumber) throws SQLException{
        String sql = "INSERT INTO Services (Service_name, Service_PhoneNumber) VALUES (?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,serviceName);
            pstmt.setString(2,ServicePhoneNumber);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.getInt(1);
        }
    }
    public static void deleteService(Integer ID)throws SQLException{
        String sql = "DELETE FORM Services WHERE Service_ID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,ID);
            pstmt.executeUpdate();
        }
    }
    public static Integer getServiceIdByPhone(String name, String phoneNumber) throws SQLException{
        String sql = "SELECT Service_ID FROM Services WHERE Service_PhoneNumber = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, name);
            pstmt.setString(2,phoneNumber);

            ResultSet rs = pstmt.executeQuery();
            return  rs.getInt("Service_ID");
        }
    }
    public static Service getServiceByID(Integer serviceID)throws SQLException{
        String sql = "Select * FROM Services WHERE Service_ID = ?";

        try( Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, serviceID.toString());
            ResultSet rs = pstmt.executeQuery();
            return Service.getService(rs.getString("Service_PhoneNumber"));
        }
    }
}