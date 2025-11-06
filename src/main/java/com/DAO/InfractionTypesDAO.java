package com.DAO;

import com.model.Fines.ExcessiveSpeed;
import com.model.Fines.InfractionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InfractionTypesDAO {
    public InfractionType getInfractionTypeByID(Integer InfractionTypeID) throws SQLException {
        String sql = "SELECT InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchargePer10PercentExcess FROM InfractionTypes WHERE InfractionType_ID = ?";
        String InfractionTypeName="", InfractionTypeDesc="";
        int InfractionTypeScoring=0;
        BigDecimal InfractionTypeAmount = new BigDecimal(0);
        BigDecimal surchargePer10PercentExcess =new BigDecimal(0);

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,InfractionTypeID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                InfractionTypeName = rs.getString("InfractionType_Name");
                InfractionTypeDesc = rs.getString("InfractionType_Description");
                InfractionTypeScoring = rs.getInt("InfractionType_Scoring");
                InfractionTypeAmount = rs.getBigDecimal("InfractionType_Amount");
                surchargePer10PercentExcess = rs.getBigDecimal("surchargePer10PercentExcess");
            }
        }
        if(surchargePer10PercentExcess !=null)
            return new ExcessiveSpeed(InfractionTypeDesc,InfractionTypeAmount,InfractionTypeScoring, surchargePer10PercentExcess);
        else
            return new InfractionType(InfractionTypeName,InfractionTypeDesc,InfractionTypeAmount,InfractionTypeScoring);
    }
    public Integer getInfractionTypeIdByName(String name) throws SQLException{
        String sql = "SELECT InfractionType_ID FROM InfractionTypes WHERE InfractionType_Name = ?";
        int id=0;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                id = rs.getInt("InfractionType_ID");
        }
        return id;
    }
    public int insertInfractionType(String Name, String Description, int scoring, int amount, BigDecimal surcharge ) throws SQLException{
        String sql = "INSERT INTO InfractionTypes (InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchargePer10PercentExcess) VALUES (?, ?, ?, ? ,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1,Name);
            pstmt.setString(2,Description);
            pstmt.setInt(3,scoring);
            pstmt.setInt(4,amount);
            pstmt.setBigDecimal(5,surcharge);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return (rs.next())?rs.getInt(1):0;
        }
    }
    public int insertInfractionType(String Name, String Description, int scoring, int amount) throws SQLException{
        String sql = "INSERT INTO InfractionTypes (InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1,Name);
            pstmt.setString(2,Description);
            pstmt.setInt(3,scoring);
            pstmt.setInt(4,amount);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return (rs.next())?rs.getInt(1):0;
        }
    }
    public void deleteInfractionType(Integer InfractionTypeID) throws SQLException {
        String sql = "DELETE FROM InfractionTypes WHERE InfractionType_ID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1, InfractionTypeID);
            pstmt.executeUpdate();
        }
    }

    public List<InfractionType> getAllInfractionTypes() throws SQLException {
        String sql = "SELECT InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchargePer10PercentExcess FROM InfractionTypes";

        List<InfractionType> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("InfractionType_Name");
                String desc = rs.getString("InfractionType_Description");
                int scoring = rs.getInt("InfractionType_Scoring");
                BigDecimal amount = rs.getBigDecimal("InfractionType_Amount");
                BigDecimal surcharge = rs.getBigDecimal("surchargePer10PercentExcess");

                if (surcharge != null) {
                    list.add(new ExcessiveSpeed(desc, amount, scoring, surcharge));
                } else {
                    list.add(new InfractionType(name, desc, amount, scoring));
                }
            }
        }

        return list;
    }

}