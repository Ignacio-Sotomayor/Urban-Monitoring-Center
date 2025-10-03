package com.DAO;

import com.model.Automobile.Brand;
import com.model.Automobile.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelsDAO {
    public Model getModelByModelId(Integer ModelID)throws SQLException {
        String sql = "SELECT Model_Name FROM Models WHERE Models_ID =?";
        String modelName;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,ModelID);
            ResultSet rs = pstmt.executeQuery();
            modelName = rs.getString("Model_Name");
        }
        return new Model(modelName);
    }
    public Integer getModelIdByName(String Name)throws SQLException{
        String sql = "SELECT Models_ID FROM Models WHERE Model_Name =?";
        Integer modelId;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,Name);
            ResultSet rs = pstmt.executeQuery();
            modelId = rs.getInt("Models_ID");
        }
        return modelId;
    }
    public Brand getBrandOfModel(Integer ModelID) throws SQLException{
        String sql = "SELECT Brand_ID FROM Models WHERE Models_ID =?";
        Integer BrandID;
        String BrandName;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,ModelID);
            ResultSet rs = pstmt.executeQuery();
            BrandID = rs.getInt("Brand_ID");
        }
        BrandsDAO brandDao = new BrandsDAO();
        return brandDao.getBrandByBrandID(BrandID);
    }
    public Integer insertModel(String ModelName, Integer BrandID)throws SQLException{
        String sql = " INSERT INTO Models (Model_Name, Brand_ID) VALUES (?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,ModelName);
            pstmt.setInt(2,BrandID);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.getInt(1);
        }
    }
    public void deleteModel(Integer ModelID)throws SQLException{
        String sql = " DELETE FROM Models WHERE Model_ID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,ModelID);
            pstmt.executeUpdate();
        }
    }
}