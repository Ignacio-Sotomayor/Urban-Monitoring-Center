package com.DAO;

import com.model.Automobile.Owner;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnersDAO {
    public Integer insertOwner(String legalID, String fullName, String Address) throws SQLException {
        String sql = "INSERT INTO Owners (Owner_LegalID, Owner_FullName, Owner_Address) VALUES (?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1,legalID);
            pstmt.setString(2,fullName);
            pstmt.setString(3,Address);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.getInt(1);
        }
    }
    public void deleteOwner(String legalID)throws SQLException{
        String sql = "DELETE FROM Owners WHERE Owner_LegalID=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,legalID);
            pstmt.executeUpdate();
        }
    }
    public Integer getOwnerIdByLegalID(String legalID)throws SQLException{
        String sql = "SELECT Owner_ID FORM OWNERS WHERE Owner_LegalID = ?";
        Integer response;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,legalID);
            ResultSet rs = pstmt.executeQuery();
            response = Integer.valueOf(rs.getInt("Owner_ID"));
        }
        return response;
    }
    public Owner getOwnerByOwnerId(Integer OwnerID) throws SQLException{
        String sql = "SELECT Owner_LegalID, Owner_FullName, Owner_Address FROM OWNERS WHERE Owner_ID = ?";
        String legalID,address,fullname;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,OwnerID);
            ResultSet rs = pstmt.executeQuery();
            legalID = rs.getString("Owner_LegalID");
            fullname = rs.getString("Owner_FullName");
            address = rs.getString("Owner_Address");

        }
        return new Owner(legalID,fullname,address);
    }
}