package com.DAO;

import com.model.Automobile.Automobile;
import com.model.Automobile.Brand;
import com.model.Automobile.Model;
import com.model.Automobile.Owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AutomobileDAO {
    public Integer getAutomobileIdByLicensePlate(String licensePLate) throws SQLException {
        String sql = "SELECT Automobile_ID FROM automobiles WHERE license_plate = ?";
        int AutomobileID=0;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1, licensePLate);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                AutomobileID = rs.getInt("Automobile_ID");
        }
        return AutomobileID;
    }

    public Automobile getAutomobileByAutomobileID(Integer AutomobileID) throws SQLException{
        String sql =" SELECT license_Plate, Automobile_Year, Model_ID, Owner_ID FROM Automobiles WHERE Automobile_ID = ?";
        int ModelID,OwnerID, Year;
        String licensePlate;
        Owner owner;
        Model model;
        Brand brand;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,AutomobileID);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) {
                throw new SQLException("No existe el automóvil con ID " + AutomobileID);
            }
            licensePlate = rs.getString("license_Plate");
            Year = rs.getInt("Automobile_Year");
            ModelID = rs.getInt("Model_ID");
            OwnerID = rs.getInt("Owner_ID");

        }
        OwnersDAO ownerDao= new OwnersDAO();
        ModelsDAO modelDao = new ModelsDAO();

        owner = ownerDao.getOwnerByOwnerId(OwnerID);
        model = modelDao.getModelByModelId(ModelID);
        brand = modelDao.getBrandOfModel(ModelID);

        return new Automobile(licensePlate,brand,model,owner,Year);
    }
    public void insertAutomobile(String licensePlate, int AutomobileYear, Integer ModelID, Integer OwnerID) throws SQLException{
        String sql = "INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES (?,?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,licensePlate);
            pstmt.setInt(2,AutomobileYear);
            pstmt.setInt(3,ModelID);
            pstmt.setInt(4,OwnerID);

            pstmt.executeUpdate();
        }
    }
    public void deleteAutomobileByAutomobileID(Integer automobileID) throws SQLException{
        String sql = "DELETE FROM Automobiles WHERE Automobile_ID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,automobileID);
            pstmt.executeUpdate();
        }
    }

    public Set<Automobile> getAllAutomobiles() throws SQLException{
        String sql = "SELECT * FROM automobiles";
        Set<Automobile> Automobiles = new HashSet<>();
        int ModelID, OwnerID, Year;
        Owner owner = new Owner("dsadsad", "Dasdsad", "Dasdas");
        Model model = new Model("dsads");
        Brand brand = new Brand("dasd");

        OwnersDAO ownerDao = new OwnersDAO();
        ModelsDAO modelDao = new ModelsDAO();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String licensePlate = rs.getString("license_Plate");
                Year = rs.getInt("Automobile_Year");
                ModelID = rs.getInt("Model_ID");
                OwnerID = rs.getInt("Owner_ID");
                brand = modelDao.getBrandOfModel(ModelID);
                model = modelDao.getModelByModelId(ModelID);
                owner = ownerDao.getOwnerByOwnerId(OwnerID);
                Automobiles.add(new Automobile(licensePlate,brand, model,owner,Year));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Automobiles;
    }
}