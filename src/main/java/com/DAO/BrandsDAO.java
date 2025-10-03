package com.DAO;
import com.model.Automobile.Brand;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class BrandsDAO {
    public Integer insertBrand(String BrandName)throws SQLException {
        String sql = "INSERT INTO Brands (Brand_Name) VALUES (?) ";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1,BrandName);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.getInt(1);
        }
    }
    public int getBrand_IdByName(String Name)throws SQLException{
        String sql = "Select Brand_ID from Brands WHERE Brand_Name = ?";
        int Brand_ID;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, Name);
            ResultSet rs = pstmt.executeQuery();
            Brand_ID = rs.getInt("Brand_ID");
        }
        return Brand_ID;
    }
    public Brand getBrandByBrandID(Integer BrandID)throws SQLException{
        String sql = "SELECT Brand_Name FROM Brands WHERE Brand_ID = ?";
        String brandName;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, BrandID);
            ResultSet rs = pstmt.executeQuery();
            brandName = rs.getString("Brand_Name");
        }
        return new Brand(brandName);
    }
    public Map<Integer,Brand> getAllBrands()throws SQLException{
        Map<Integer,Brand> response= new HashMap<>();
        String sql = "Select * From Brands";

        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ){
            while(rs.next())
                response.put( Integer.valueOf( rs.getInt("Brand_ID") ),new Brand( rs.getString("Brand_name") ) );
        }
        return response;
    }

    public void deleteBrand(String BrandName)throws SQLException{
        String sql = "DELETE FROM Brands WHERE Brand_Name = ?";
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,BrandName);
            pstmt.executeUpdate();
        }
    }
}