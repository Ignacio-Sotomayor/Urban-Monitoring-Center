package com.DAO;

import com.model.SecurityNotice;
import java.sql.*;

public class SecurityNoticeDAO {

    public static void insertSecurityNotice(String description, Integer geoLocID) throws SQLException{
        String sql = "INSERT INTO SecurityNotices (SecurityNotice_Description, GeoLocation_ID) VALUES (?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,description);
            pstmt.setInt(2,geoLocID);
            pstmt.executeUpdate();
        }
    }
    public static void deleteSecurityNotice(Integer SecurityNoticeID) throws SQLException{
        String sql = "DELETE FROM SecurityNotices WHERE SecurityNotice_ID = ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,SecurityNoticeID);
            pstmt.executeUpdate();
        }
    }
    public static SecurityNotice getSecurityNotice(Integer ID) throws SQLException{
        String sql = "SELECT SecurityNotice_Description, GeoLocation_ID FROM SecurityNotices WHERE SecurityNotice_ID = ?";
        String description;
        Integer geoLocID;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, ID);
            ResultSet rs = pstmt.executeQuery();
            description = rs.getString("SecurityNotice_Description");
            geoLocID = rs.getInt("GeoLocation_ID");
        }
        return new SecurityNotice(description, GeoLocationDAO.getGeoLocation(geoLocID));
    }
}