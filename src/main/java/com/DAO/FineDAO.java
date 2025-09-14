package com.DAO;

import com.model.Fines.ExcessiveSpeedFine;
import com.model.Fines.Fine;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FineDAO {

    public static void insertSpeedingFine(BigDecimal Amount, int Scoring,Integer AutomobileID, Integer geoLocID, int speedLimit, int AutomobileSpeed) throws SQLException{
        String sql = "INSERT INTO Fines (Fine_Amount, Fine_Scoring, InfractionType_ID, Automobile_ID, GeoLocation_ID, SpeedLimit, AutomobileSpeed) VALUES (?, ?, 1, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setBigDecimal(1,Amount);
            pstmt.setInt(2,Scoring);
            pstmt.setInt(3,AutomobileID);
            pstmt.setInt(4,geoLocID);
            pstmt.setInt(5,speedLimit);
            pstmt.setInt(6,AutomobileSpeed);

            pstmt.executeUpdate();
        }
    }
    public static void insertFine(BigDecimal Amount, int Scoring, Integer InfractionTypeID, Integer AutomobileID, Integer geoLocID) throws SQLException {
        String sql = "INSERT INTO Fines (Fine_Amount, Fine_Scoring, InfractionType_ID, Automobile_ID, GeoLocation_ID) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setBigDecimal(1,Amount);
            pstmt.setInt(2,Scoring);
            pstmt.setInt(3,InfractionTypeID);
            pstmt.setInt(4,AutomobileID);
            pstmt.setInt(5,geoLocID);

            pstmt.executeUpdate();
        }
    }
    public static @NotNull Set<Fine> getNFinesBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, geoLocID, InfractionTypeID, AutomobileID;
        ResultSet rs;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setTimestamp(1,InitDate);
            pstmt.setTimestamp(2,EndDate);
            pstmt.setInt(3,N);
            pstmt.setInt(4,Offset);

            rs = pstmt.executeQuery();
        }
        if(rs != null){
            while (rs.next()) {
                FineID = rs.getInt("Fine_ID");
                geoLocID = rs.getInt("GeoLocation_ID");
                AutomobileID = rs.getInt("Automobile_ID");
                InfractionTypeID = rs.getInt("InfractionType_ID");
            if(InfractionTypeID!=1) {
                    fines.add(new Fine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(InfractionTypeID), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID)));

            } else
                fines.add(new ExcessiveSpeedFine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(1), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));

            }
        }
        return fines;
    }
    public static @NotNull Set<Fine> getNSpecifTypeFineBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer InfractionTypeID) throws SQLException{
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE m.InfractionType_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, geoLocID, AutomobileID;
        ResultSet rs;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,InfractionTypeID);
            pstmt.setTimestamp(2,InitDate);
            pstmt.setTimestamp(3,EndDate);
            pstmt.setInt(4,N);
            pstmt.setInt(5,Offset);

            rs = pstmt.executeQuery();
        }
        if(rs != null){
            if(InfractionTypeID!=1) {
                while (rs.next()) {
                    FineID = rs.getInt("Fine_ID");
                    geoLocID = rs.getInt("GeoLocation_ID");
                    AutomobileID = rs.getInt("Automobile_ID");

                    fines.add(new Fine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(InfractionTypeID), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID)));
                }
            } else{
                while (rs.next()) {
                    FineID = rs.getInt("Fine_ID");
                    geoLocID = rs.getInt("GeoLocation_ID");
                    AutomobileID = rs.getInt("Automobile_ID");

                    fines.add(new ExcessiveSpeedFine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(1), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
                }
            }
        }
        return fines;
    }
    public static @NotNull Set<Fine> getNFinesByAutomobileIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer AutomobileID) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE m.Automobile_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, geoLocID, InfractionTypeID;
        ResultSet rs;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,AutomobileID);
            pstmt.setTimestamp(2,InitDate);
            pstmt.setTimestamp(3,EndDate);
            pstmt.setInt(4,N);
            pstmt.setInt(5,Offset);

            rs = pstmt.executeQuery();
        }
        if(rs != null){
            while (rs.next()) {
                FineID = rs.getInt("Fine_ID");
                geoLocID = rs.getInt("GeoLocation_ID");
                InfractionTypeID = rs.getInt("InfractionType_ID");
                if(InfractionTypeID!=1) {
                    fines.add(new Fine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(InfractionTypeID), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID)));

                } else
                    fines.add(new ExcessiveSpeedFine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(1), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
            }
        }
        return fines;
    }
    public static @NotNull Set<Fine> getNSpecifTypeFineByAutomobileIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer AutomobileID, Integer InfractionTypeID) throws SQLException{
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE m.Automobile_ID = ? AND m.InfractionType_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, geoLocID;
        ResultSet rs;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1,AutomobileID);
            pstmt.setInt(2,InfractionTypeID);
            pstmt.setTimestamp(3,InitDate);
            pstmt.setTimestamp(4,EndDate);
            pstmt.setInt(5,N);
            pstmt.setInt(6,Offset);

            rs = pstmt.executeQuery();
        }
        if(rs != null){
            if(InfractionTypeID!=1) {
                while (rs.next()) {
                    FineID = rs.getInt("Fine_ID");
                    geoLocID = rs.getInt("GeoLocation_ID");

                    fines.add(new Fine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(InfractionTypeID), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID)));
                }
            } else{
                while (rs.next()) {
                    FineID = rs.getInt("Fine_ID");
                    geoLocID = rs.getInt("GeoLocation_ID");

                    fines.add(new ExcessiveSpeedFine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(1), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
                }
            }
        }
        return fines;
    }
    public static @NotNull Set<Fine> getNFinesByDeviceUUIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, @NotNull UUID DeviceUUID) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE gl.DeviceUUID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, geoLocID, InfractionTypeID, AutomobileID;
        ResultSet rs;
        String uuid = DeviceUUID.toString();

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1,uuid);
            pstmt.setTimestamp(2,InitDate);
            pstmt.setTimestamp(3,EndDate);
            pstmt.setInt(4,N);
            pstmt.setInt(5,Offset);

            rs = pstmt.executeQuery();
        }
        if(rs != null){
            while (rs.next()) {
                FineID = rs.getInt("Fine_ID");
                geoLocID = rs.getInt("GeoLocation_ID");
                AutomobileID = rs.getInt("Automobile_ID");
                InfractionTypeID = rs.getInt("InfractionType_ID");
                if(InfractionTypeID!=1) {
                    fines.add(new Fine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(InfractionTypeID), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID)));

                } else
                    fines.add(new ExcessiveSpeedFine(FineID, GeoLocationDAO.getGeoLocation(geoLocID), InfractionTypesDAO.getInfractionTypeByID(1), AutomobileDAO.getAutomobileByAutomobileID(AutomobileID), PhotosDAO.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
            }
        }
        return fines;
    }
}