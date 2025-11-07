package com.DAO;

import com.model.Automobile.Brand;
import com.model.Automobile.Owner;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class OwnersDAO {
    public Integer insertOwner(String legalID, String fullName, String Address) throws SQLException {
        String sql = "INSERT INTO Owners (Owner_LegalID, Owner_FullName, Owner_Address) VALUES (?, ?, ?)";
        int id=99;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1,legalID);
            pstmt.setString(2,fullName);
            pstmt.setString(3,Address);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
                id=rs.getInt(1);
        }
        return id;
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

    public Map<Integer, Owner> getAllBrands()throws SQLException{
        Map<Integer,Owner> response= new HashMap<>();
        String sql = "Select * From Owners";

        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ){
            while(rs.next())
                response.put( Integer.valueOf( rs.getInt("Owner_IdOwner_name")) ,new Owner( rs.getString("Owner_LegalID"),rs.getString("Owner_FullName"),rs.getString("Owner_address")) );
        }
        return response;
    }

    public int getOwnerIdByLegalID(String legalID)throws SQLException{
        String sql = "SELECT Owner_ID FORM OWNERS WHERE Owner_LegalID = ?";
        int response=0;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,legalID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                response = rs.getInt("Owner_ID");
        }
        return response;
    }
    public Owner getOwnerByOwnerId(Integer OwnerID) throws SQLException{
        String sql = "SELECT Owner_LegalID, Owner_FullName, Owner_Address FROM OWNERS WHERE Owner_ID = ?";
        String legalID = "",address = "",fullname = "";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,OwnerID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                legalID = rs.getString("Owner_LegalID");
                fullname = rs.getString("Owner_FullName");
                address = rs.getString("Owner_Address");
            }
        }
        return new Owner(legalID,fullname,address);
    }
}