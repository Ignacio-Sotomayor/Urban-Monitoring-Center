package com.DAO;

import com.model.Fines.ExcessiveSpeed;
import com.model.Fines.InfractionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfractionTypesDAO {
    public InfractionType getInfractionTypeByID(Integer InfractionTypeID) throws SQLException {
        String sql = "SELECT InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchangePer10PercentExcess FROM InfractionTypes WHERE InfractionType_ID = ?";
        String InfractionTypeName, InfractionTypeDesc;
        int InfractionTypeScoring;
        BigDecimal InfractionTypeAmount;
        BigDecimal surchangePer10PercentExcess;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,InfractionTypeID);
            ResultSet rs = pstmt.executeQuery();
            InfractionTypeName = rs.getString("InfractionType_Name");
            InfractionTypeDesc = rs.getString("InfractionType_Description");
            InfractionTypeScoring = rs.getInt("InfractionType_Scoring");
            InfractionTypeAmount = rs.getBigDecimal("InfractionType_Amount");
            surchangePer10PercentExcess = rs.getBigDecimal("surchangePer10PercentExcess");
        }
        if(surchangePer10PercentExcess!=null)
            return new ExcessiveSpeed(InfractionTypeDesc,InfractionTypeAmount,InfractionTypeScoring,surchangePer10PercentExcess);
        else
            return new InfractionType(InfractionTypeName,InfractionTypeDesc,InfractionTypeAmount,InfractionTypeScoring);
    }
    public Integer getInfractionTypeIdByName(String name) throws SQLException{
        String sql = "SELECT InfractionType_ID FROM InfractionTypes WHERE InfractionType_Name = ?";
        Integer id;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            id = rs.getInt("InfractionType_ID");
        }
        return id;
    }
    public void insertInfractionType(String Name, String Description, int scoring, int amount, BigDecimal surcharge ) throws SQLException{
        String sql = "INSERT INTO InfractionTypes (InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchangePer10PercentExcess) VALUES (?, ?, ?, ? ,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,Name);
            pstmt.setString(2,Description);
            pstmt.setInt(3,scoring);
            pstmt.setInt(4,amount);
            pstmt.setBigDecimal(5,surcharge);

            pstmt.executeUpdate();
        }
    }
    public void insertInfractionType(String Name, String Description, int scoring, int amount) throws SQLException{
        String sql = "INSERT INTO InfractionTypes (InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,Name);
            pstmt.setString(2,Description);
            pstmt.setInt(3,scoring);
            pstmt.setInt(4,amount);

            pstmt.executeUpdate();
        }
    }
    public void deleteInfractionType(Integer InfractionTypeID) throws SQLException {
        String sql = "DELETE FROM InfractionTypes WHERE InfractionType_ID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, InfractionTypeID);
            pstmt.executeUpdate();
        }
    }
}