package com.DAO;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import com.model.Service;

public class SecurityNoticeDetailsDAO {
    public static Set<Service> getAllServicesForSecurityNotice(Integer SecurityNotice_ID)throws SQLException{
        String sql = "SELECT Service_name,ServicePhoneNumber FROM Services WHERE SecurityNotice_ID = ?";
        HashSet<Service> services = new HashSet<>();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,SecurityNotice_ID);
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
    public static void insertSecurityNoticeDetail(Integer SecurityNoticeID, Integer serviceID)throws SQLException{
        String sql = "INSERT INTO SecurityNoticeDetails (Service_ID, SecurityNotice_ID) VALUES (?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,serviceID);
            pstmt.setInt(2, SecurityNoticeID);

            pstmt.executeUpdate();
        }
    }
    public static void deleteSecurityNoticeDetails(Integer SecurityNoticeID)throws SQLException{
        String sql = "DELETE FROM SecurityNoticeDetails WHERE SecurityNoticeID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,SecurityNoticeID);
            pstmt.executeUpdate();
        }
    }
}