package com.DAO;

import com.model.Automobile.Brand;
import com.model.Automobile.Model;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ModelsDAO {
    public Model getModelByModelId(Integer ModelID) throws SQLException {
        String sql = "SELECT Model_Name FROM Models WHERE Models_ID =?";
        String modelName = "";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, ModelID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                modelName = rs.getString("Model_Name");
        }
        return new Model(modelName);
    }

    public int getModelIdByName(String Name) throws SQLException {
        String sql = "SELECT Models_ID FROM Models WHERE Model_Name =?";
        int modelId = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, Name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                modelId = rs.getInt("Models_ID");
        }
        return modelId;
    }

    public Brand getBrandOfModel(Integer ModelID) throws SQLException {
        String sql = "SELECT Brand_ID FROM Models WHERE Models_ID =?";
        Integer BrandID = 0;
        String BrandName;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, ModelID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
                BrandID = rs.getInt("Brand_ID");
        }
        BrandsDAO brandDao = new BrandsDAO();
        return brandDao.getBrandByBrandID(BrandID);
    }

    public int insertModel(String ModelName, Integer BrandID) throws SQLException {
        String sql = " INSERT INTO Models (Model_Name, Brand_ID) VALUES (?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, ModelName);
            pstmt.setInt(2, BrandID);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
            else
                return 0;
        }
    }

    public int updateModel(int modelId, String modelName, Integer brandID) throws SQLException {

        String sql = "UPDATE Models SET Brand_ID = ? WHERE Models_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, brandID);
            pstmt.setInt(2, modelId);
            pstmt.executeUpdate();
        }
        return modelId;
    }


    public String getBrandNameByModelID(int modelID)throws SQLException{
        String sql = "Select Brand_Name from Brands JOin Models m ON Brand_ID= m.Brand_ID where m.Model_ID=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,modelID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                return rs.getString("Brand_Name");
            else throw new RuntimeException();
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

    public Set<Model> getAllModels()throws SQLException{
        Set<Model> response= new HashSet<>();
        String sql = "Select * From Models";

        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ){
            while(rs.next()){
               response.add(new Model(rs.getString("model_name")));
        }
        return response;
    }
    }
}