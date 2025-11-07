package com.DAO;

import com.model.Devices.Location;
import com.model.Fines.EventGeolocation;
import com.model.Fines.ExcessiveSpeedFine;
import com.model.Fines.Fine;
import com.controller.UrbanMonitoringCenter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class FineDAO {

    public Integer insertSpeedingFine(BigDecimal Amount, int Scoring, BigDecimal FineLatitude, BigDecimal FineLongitude, String FineAddress, Timestamp FineDateTime, String IssuerDeviceUUID,Integer AutomobileID, int speedLimit, int AutomobileSpeed) throws SQLException{
        String sql = "INSERT INTO Fines (Fine_Amount, Fine_Scoring, Fine_Latitude, Fine_Longitude, Fine_Address, Fine_DateTime, Issuer_DeviceUUID, InfractionType_ID, Automobile_ID, SpeedLimit, AutomobileSpeed) VALUES (?,?, ?, ?, ?, ?, ?, 1, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            pstmt.setBigDecimal(1,Amount);
            pstmt.setInt(2,Scoring);
            pstmt.setBigDecimal(3,FineLatitude);
            pstmt.setBigDecimal(4,FineLongitude);
            pstmt.setString(5,FineAddress);
            pstmt.setTimestamp(6,FineDateTime);
            pstmt.setString(7,IssuerDeviceUUID);
            pstmt.setInt(8,AutomobileID);
            pstmt.setInt(9,speedLimit);
            pstmt.setInt(10,AutomobileSpeed);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.getInt(1);
        }
    }
    public Integer insertFine(BigDecimal Amount, int Scoring, BigDecimal FineLatitude, BigDecimal FineLongitude, String FineAddress, Timestamp FineDateTime, String IssuerDeviceUUID, Integer InfractionTypeID, Integer AutomobileID) throws SQLException {
        String sql = "INSERT INTO Fines (Fine_Amount, Fine_Scoring, Fine_Latitude, Fine_Longitude, Fine_Address, Fine_DateTime, Issuer_DeviceUUID, InfractionType_ID, Automobile_ID) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            pstmt.setBigDecimal(1,Amount);
            pstmt.setInt(2,Scoring);
            pstmt.setBigDecimal(3,FineLatitude);
            pstmt.setBigDecimal(4,FineLongitude);
            pstmt.setString(5,FineAddress);
            pstmt.setTimestamp(6,FineDateTime);
            pstmt.setString(7,IssuerDeviceUUID);
            pstmt.setInt(8,InfractionTypeID);
            pstmt.setInt(9,AutomobileID);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return (rs.next())?rs.getInt(1):0;
        }
    }
    public Set<Fine> getNFinesBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, InfractionTypeID, AutomobileID;
        Timestamp timestamp;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
        String uuid;
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
            InfractionTypesDAO infractionDao = new InfractionTypesDAO();
            AutomobileDAO automobileDao = new AutomobileDAO();
            PhotosDAO photoDao = new PhotosDAO();
            while (rs.next()) {
                FineID = rs.getInt("Fine_ID");
                timestamp = rs.getTimestamp("Fine_DateTime");
                address = rs.getString("Fine_Address");
                latitude = rs.getBigDecimal("Fine_Latitude");
                longitude = rs.getBigDecimal("Fine_Longitude");
                uuid= rs.getString("Issuer_DeviceUUID");
                AutomobileID = rs.getInt("Automobile_ID");
                InfractionTypeID = rs.getInt("InfractionType_ID");
            if(InfractionTypeID!=1) {
                fines.add(new Fine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(InfractionTypeID), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID)));

            } else
                fines.add(new ExcessiveSpeedFine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude),UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid)) ), infractionDao.getInfractionTypeByID(1), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));

            }
        }
        return fines;
    }
    public Set<Fine> getNSpecifTypeFineBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer InfractionTypeID) throws SQLException{
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE m.InfractionType_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, AutomobileID;
        Timestamp timestamp;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
        String uuid;
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
            InfractionTypesDAO infractionDao = new InfractionTypesDAO();
            AutomobileDAO automobileDao = new AutomobileDAO();
            PhotosDAO photoDao = new PhotosDAO();
            while (rs.next()) {
                FineID = rs.getInt("Fine_ID");
                timestamp = rs.getTimestamp("Fine_DateTime");
                address = rs.getString("Fine_Address");
                latitude = rs.getBigDecimal("Fine_Latitude");
                longitude = rs.getBigDecimal("Fine_Longitude");
                uuid= rs.getString("Issuer_DeviceUUID");
                AutomobileID = rs.getInt("Automobile_ID");
                if(InfractionTypeID!=1)
                    fines.add(new Fine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(InfractionTypeID), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID)));
                else
                    fines.add(new ExcessiveSpeedFine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(1), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
            }
        }
        return fines;
    }
    public Set<Fine> getNFinesByAutomobileIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer AutomobileID) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE m.Automobile_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, InfractionTypeID;
        Timestamp timestamp;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
        String uuid;
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
            InfractionTypesDAO infractionDao = new InfractionTypesDAO();
            AutomobileDAO automobileDao = new AutomobileDAO();
            PhotosDAO photoDao = new PhotosDAO();
            while (rs.next()) {
                FineID = rs.getInt("Fine_ID");
                timestamp = rs.getTimestamp("Fine_DateTime");
                address = rs.getString("Fine_Address");
                latitude = rs.getBigDecimal("Fine_Latitude");
                longitude = rs.getBigDecimal("Fine_Longitude");
                uuid= rs.getString("Issuer_DeviceUUID");
                InfractionTypeID = rs.getInt("InfractionType_ID");
                if(InfractionTypeID!=1) {
                    fines.add(new Fine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(InfractionTypeID), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID)));

                } else
                    fines.add(new ExcessiveSpeedFine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(1), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
            }
        }
        return fines;
    }
    public Set<Fine> getNSpecifTypeFineByAutomobileIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer AutomobileID, Integer InfractionTypeID) throws SQLException{
        String sql = "SELECT * FROM Fines  WHERE Automobile_ID = ? AND InfractionType_ID = ? AND Fine_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID;
        Timestamp timestamp;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
        String uuid;
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
            if(rs != null){
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                AutomobileDAO automobileDao = new AutomobileDAO();
                PhotosDAO photoDao = new PhotosDAO();
                if(InfractionTypeID!=1) {
                    while (rs.next()) {
                        FineID = rs.getInt("Fine_ID");
                        timestamp = rs.getTimestamp("Fine_DateTime");
                        address = rs.getString("Fine_Address");
                        latitude = rs.getBigDecimal("Fine_Latitude");
                        longitude = rs.getBigDecimal("Fine_Longitude");
                        uuid= rs.getString("Issuer_DeviceUUID");

                        fines.add(new Fine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(InfractionTypeID), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID)));
                    }
                } else{
                    while (rs.next()) {
                        FineID = rs.getInt("Fine_ID");
                        timestamp = rs.getTimestamp("Fine_DateTime");
                        address = rs.getString("Fine_Address");
                        latitude = rs.getBigDecimal("Fine_Latitude");
                        longitude = rs.getBigDecimal("Fine_Longitude");
                        uuid= rs.getString("Issuer_DeviceUUID");

                        fines.add(new ExcessiveSpeedFine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(1), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID),rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
                    }
                }
            }
        }

        return fines;
    }
    public Set<Fine> getNFinesByDeviceUUIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, @NotNull UUID DeviceUUID) throws SQLException {
        String sql = "SELECT * FROM Fines  WHERE Issuer_DeviceUUID = ? AND Fine_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, InfractionTypeID, AutomobileID;
        Timestamp timestamp;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
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
            InfractionTypesDAO infractionDao = new InfractionTypesDAO();
            AutomobileDAO automobileDao = new AutomobileDAO();
            PhotosDAO photoDao = new PhotosDAO();
            while (rs.next()) {
                FineID = rs.getInt("Fine_ID");
                timestamp = rs.getTimestamp("Fine_DateTime");
                address = rs.getString("Fine_Address");
                latitude = rs.getBigDecimal("Fine_Latitude");
                longitude = rs.getBigDecimal("Fine_Longitude");
                uuid= rs.getString("Issuer_DeviceUUID");
                AutomobileID = rs.getInt("Automobile_ID");
                InfractionTypeID = rs.getInt("InfractionType_ID");

                if(InfractionTypeID!=1) {
                    fines.add(new Fine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(InfractionTypeID), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID)));

                } else
                    fines.add(new ExcessiveSpeedFine(FineID, new EventGeolocation(timestamp.toLocalDateTime(), address, new Location(latitude, longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(1), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID), rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
            }
        }

        return fines;
    }
    public Set<Fine> getAllFinesFromAutomobile(int automobileID)throws SQLException{
        String sql = "SELECT * FROM fines WHERE automobile_id = ? ";
        Set<Fine> fines = new HashSet();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,automobileID);
            ResultSet rs = pstmt.executeQuery();
            InfractionTypesDAO infractionDao = new InfractionTypesDAO();
            AutomobileDAO automobileDao = new AutomobileDAO();
            PhotosDAO photoDao = new PhotosDAO();

            while (rs.next()) {
                int FineID = rs.getInt("Fine_ID");
                Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                String address = rs.getString("Fine_Address");
                BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                String uuid= rs.getString("Issuer_DeviceUUID");
                int InfractionTypeID = rs.getInt("InfractionType_ID");

                if(InfractionTypeID!=1) {
                    fines.add(new Fine(FineID, new EventGeolocation(timestamp.toLocalDateTime(),address,new Location(latitude,longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(InfractionTypeID), automobileDao.getAutomobileByAutomobileID(automobileID), photoDao.getAllPhotosFromFine(FineID)));

                } else
                    fines.add(new ExcessiveSpeedFine(FineID, new EventGeolocation(timestamp.toLocalDateTime(), address, new Location(latitude, longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(1), automobileDao.getAutomobileByAutomobileID(automobileID), photoDao.getAllPhotosFromFine(FineID), rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));
            }
        }
        return fines;

    }
    public Iterator<Fine> getNRecentFines(int N) throws SQLException {
        String sql = "SELECT * FROM Fines ORDER BY Fine_DateTime DESC Limit ? ";

        Set<Fine> fines = new HashSet<>(N);
        Integer FineID, InfractionTypeID, AutomobileID;
        Timestamp timestamp;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
        String uuid;
        ResultSet rs;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, N);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                AutomobileDAO automobileDao = new AutomobileDAO();
                PhotosDAO photoDao = new PhotosDAO();
                while (rs.next()) {
                    FineID = rs.getInt("Fine_ID");
                    timestamp = rs.getTimestamp("Fine_DateTime");
                    address = rs.getString("Fine_Address");
                    latitude = rs.getBigDecimal("Fine_Latitude");
                    longitude = rs.getBigDecimal("Fine_Longitude");
                    uuid = rs.getString("Issuer_DeviceUUID");
                    AutomobileID = rs.getInt("Automobile_ID");
                    InfractionTypeID = rs.getInt("InfractionType_ID");
                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID, new EventGeolocation(timestamp.toLocalDateTime(), address, new Location(latitude, longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(InfractionTypeID), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID)));

                    } else
                        fines.add(new ExcessiveSpeedFine(FineID, new EventGeolocation(timestamp.toLocalDateTime(), address, new Location(latitude, longitude), UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificDevice(UUID.fromString(uuid))), infractionDao.getInfractionTypeByID(1), automobileDao.getAutomobileByAutomobileID(AutomobileID), photoDao.getAllPhotosFromFine(FineID), rs.getInt("SpeedLimit"), rs.getInt("AutomobileSpeed")));

                }
            }
        }
        return fines.iterator();
    }
}