package com.DAO;

import com.model.Service;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceDAO {
    public static void insertService(String serviceName, String ServicePhoneNumber, Integer SecurityNoticeID) throws SQLException{
        String sql = "INSERT INTO Services (Service_name, Service_PhoneNumber,SecurityNotice_ID) VALUES (?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,serviceName);
            pstmt.setString(2,ServicePhoneNumber);
            pstmt.setInt(3,SecurityNoticeID);
            pstmt.executeUpdate();
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
    public static Set<Service> getServiceOnSecurityNotice(Integer SecurityNoticeID)throws SQLException{
        String sql = "SELECT Service_name,ServicePhoneNumber FROM Services WHERE SecurityNotice_ID = ?";
        HashSet<Service> services = new HashSet<>();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,SecurityNoticeID);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                switch (rs.getString("Service_name")){
                    case "Police": services.add(Service.Police); break;
                    case "FireFighters": services.add(Service.FireFighters); break;
                    case "Ambulance": services.add(Service.Ambulance); break;
                    case "CivilDefense":services.add(Service.CivilDefense); break;
                }
            }
        }
        return services;
    }
}