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
        String sql = "SELECT InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchangePer10PercentExcess FROM InfractionTypes WHERE InfractionType_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, InfractionTypeID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No se encontró InfractionType con ID: " + InfractionTypeID);
                }

                String name = rs.getString("InfractionType_Name");
                String desc = rs.getString("InfractionType_Description");
                int scoring = rs.getInt("InfractionType_Scoring");
                BigDecimal amount = rs.getBigDecimal("InfractionType_Amount");
                BigDecimal surcharge = rs.getBigDecimal("surchangePer10PercentExcess");

                if (surcharge != null) {
                    return new ExcessiveSpeed(desc, amount, scoring, surcharge);
                } else {
                    return new InfractionType(name, desc, amount, scoring);
                }
            }
        }
    }



    public Integer getInfractionTypeIdByName(String name) throws SQLException{
        String sql = "SELECT InfractionType_ID FROM InfractionTypes WHERE InfractionType_Name = ?";
        Integer id;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                id = rs.getInt("InfractionType_ID");
            } else {
                throw new SQLException("No se encontró InfractionType con nombre: " + name);
            }
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

    public List<InfractionType> getAllInfractionTypes() throws SQLException {
        String sql = "SELECT InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchangePer10PercentExcess FROM InfractionTypes";

        List<InfractionType> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("InfractionType_Name");
                String desc = rs.getString("InfractionType_Description");
                int scoring = rs.getInt("InfractionType_Scoring");
                BigDecimal amount = rs.getBigDecimal("InfractionType_Amount");
                BigDecimal surcharge = rs.getBigDecimal("surchangePer10PercentExcess");

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