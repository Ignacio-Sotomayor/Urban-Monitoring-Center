package com.DAO;

import com.model.Devices.Location;
import com.model.Fines.EventGeolocation;
import com.model.Fines.ExcessiveSpeedFine;
import com.model.Fines.Fine;
import com.model.UrbanMonitoringCenter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class FineDAO {

    public Integer insertSpeedingFine(BigDecimal Amount, int Scoring, BigDecimal FineLatitude, BigDecimal FineLongitude, String FineAddress, Timestamp FineDateTime, String IssuerDeviceUUID,Integer AutomobileID, double  speedLimit, double  AutomobileSpeed) throws SQLException{
        String sql = "INSERT INTO Fines (Fine_Amount, Fine_Scoring, Fine_Latitude, Fine_Longitude, Fine_Address, Fine_DateTime, Issuer_DeviceUUID, Automobile_ID, InfractionType_ID, SpeedLimit, AutomobileSpeed) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            pstmt.setInt(9, 1);
            pstmt.setDouble(10,speedLimit);
            pstmt.setDouble(11,AutomobileSpeed);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("No se generó la llave para la multa de velocidad");
            }
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
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("No se generó la llave para la multa");
            }

        }
    }
    public Set<Fine> getNFinesBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE gl.GeoLocation_DateTime BETWEEN ? AND ? Limit ? OFFSET ?";
        Set<Fine> fines = new HashSet<>(N);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, InitDate);
            pstmt.setTimestamp(2, EndDate);
            pstmt.setInt(3, N);
            pstmt.setInt(4, Offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                AutomobileDAO automobileDao = new AutomobileDAO();
                PhotosDAO photoDao = new PhotosDAO();

                while (rs.next()) {
                    Integer FineID = rs.getInt("Fine_ID");
                    Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                    String address = rs.getString("Fine_Address");
                    BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                    BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                    String uuid = rs.getString("Issuer_DeviceUUID");
                    Integer AutomobileID = rs.getInt("Automobile_ID");
                    Integer InfractionTypeID = rs.getInt("InfractionType_ID");

                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(InfractionTypeID),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID)));
                    } else {
                        fines.add(new ExcessiveSpeedFine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(1),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID),
                                rs.getDouble("SpeedLimit"),
                                rs.getDouble("AutomobileSpeed")));
                    }
                }
            }
        }
        return fines;
    }

    public Set<Fine> getNSpecifTypeFineBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer InfractionTypeID) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE f.InfractionType_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? LIMIT ? OFFSET ?";
        Set<Fine> fines = new HashSet<>(N);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, InfractionTypeID);
            pstmt.setTimestamp(2, InitDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt(4, N);
            pstmt.setInt(5, Offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                AutomobileDAO automobileDao = new AutomobileDAO();
                PhotosDAO photoDao = new PhotosDAO();

                while (rs.next()) {
                    Integer FineID = rs.getInt("Fine_ID");
                    Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                    String address = rs.getString("Fine_Address");
                    BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                    BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                    String uuid = rs.getString("Issuer_DeviceUUID");
                    Integer AutomobileID = rs.getInt("Automobile_ID");

                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(InfractionTypeID),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID)));
                    } else {
                        fines.add(new ExcessiveSpeedFine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(1),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID),
                                rs.getDouble("SpeedLimit"),
                                rs.getDouble("AutomobileSpeed")));
                    }
                }
            }
        }

        return fines;
    }

    public Set<Fine> getNFinesByAutomobileIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer AutomobileID) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE f.Automobile_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? LIMIT ? OFFSET ?";
        Set<Fine> fines = new HashSet<>(N);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, AutomobileID);
            pstmt.setTimestamp(2, InitDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt(4, N);
            pstmt.setInt(5, Offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                PhotosDAO photoDao = new PhotosDAO();

                while (rs.next()) {
                    Integer FineID = rs.getInt("Fine_ID");
                    Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                    String address = rs.getString("Fine_Address");
                    BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                    BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                    String uuid = rs.getString("Issuer_DeviceUUID");
                    Integer InfractionTypeID = rs.getInt("InfractionType_ID");

                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(InfractionTypeID),
                                new AutomobileDAO().getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID)));
                    } else {
                        fines.add(new ExcessiveSpeedFine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(1),
                                new AutomobileDAO().getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID),
                                rs.getDouble("SpeedLimit"),
                                rs.getDouble("AutomobileSpeed")));
                    }
                }
            }
        }

        return fines;
    }

    public Set<Fine> getNSpecifTypeFineByAutomobileIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, Integer AutomobileID, Integer InfractionTypeID) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE f.Automobile_ID = ? AND f.InfractionType_ID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? LIMIT ? OFFSET ?";
        Set<Fine> fines = new HashSet<>(N);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, AutomobileID);
            pstmt.setInt(2, InfractionTypeID);
            pstmt.setTimestamp(3, InitDate);
            pstmt.setTimestamp(4, EndDate);
            pstmt.setInt(5, N);
            pstmt.setInt(6, Offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                PhotosDAO photoDao = new PhotosDAO();

                while (rs.next()) {
                    Integer FineID = rs.getInt("Fine_ID");
                    Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                    String address = rs.getString("Fine_Address");
                    BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                    BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                    String uuid = rs.getString("Issuer_DeviceUUID");

                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(InfractionTypeID),
                                new AutomobileDAO().getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID)));
                    } else {
                        fines.add(new ExcessiveSpeedFine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(1),
                                new AutomobileDAO().getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID),
                                rs.getDouble("SpeedLimit"),
                                rs.getDouble("AutomobileSpeed")));
                    }
                }
            }
        }

        return fines;
    }

    public Set<Fine> getNFinesByDeviceUUIDBetweenDates(Timestamp InitDate, Timestamp EndDate, int N, int Offset, @NotNull UUID DeviceUUID) throws SQLException {
        String sql = "SELECT f.* FROM Fines JOIN GeoLocations gl ON f.GeoLocation_ID = gl.GeoLocation_ID WHERE gl.DeviceUUID = ? AND gl.GeoLocation_DateTime BETWEEN ? AND ? LIMIT ? OFFSET ?";
        Set<Fine> fines = new HashSet<>(N);
        String uuid = DeviceUUID.toString();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid);
            pstmt.setTimestamp(2, InitDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt(4, N);
            pstmt.setInt(5, Offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                AutomobileDAO automobileDao = new AutomobileDAO();
                PhotosDAO photoDao = new PhotosDAO();

                while (rs.next()) {
                    Integer FineID = rs.getInt("Fine_ID");
                    Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                    String address = rs.getString("Fine_Address");
                    BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                    BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                    String deviceUUID = rs.getString("Issuer_DeviceUUID");
                    Integer AutomobileID = rs.getInt("Automobile_ID");
                    Integer InfractionTypeID = rs.getInt("InfractionType_ID");

                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(deviceUUID))),
                                infractionDao.getInfractionTypeByID(InfractionTypeID),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID)));
                    } else {
                        fines.add(new ExcessiveSpeedFine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(deviceUUID))),
                                infractionDao.getInfractionTypeByID(1),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID),
                                rs.getDouble("SpeedLimit"),
                                rs.getDouble("AutomobileSpeed")));
                    }
                }
            }
        }
        return fines;
    }

    public Set<Fine> getAllFinesFromAutomobile(int automobileID) throws SQLException {
        String sql = "SELECT * FROM Fines WHERE Automobile_ID = ?";
        Set<Fine> fines = new HashSet<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, automobileID);

            try (ResultSet rs = pstmt.executeQuery()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                PhotosDAO photoDao = new PhotosDAO();

                while (rs.next()) {
                    int FineID = rs.getInt("Fine_ID");
                    Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                    String address = rs.getString("Fine_Address");
                    BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                    BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                    String uuid = rs.getString("Issuer_DeviceUUID");
                    int InfractionTypeID = rs.getInt("InfractionType_ID");

                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(InfractionTypeID),
                                new AutomobileDAO().getAutomobileByAutomobileID(automobileID),
                                photoDao.getAllPhotosFromFine(FineID)));
                    } else {
                        fines.add(new ExcessiveSpeedFine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(1),
                                new AutomobileDAO().getAutomobileByAutomobileID(automobileID),
                                photoDao.getAllPhotosFromFine(FineID),
                                rs.getDouble("SpeedLimit"),
                                rs.getDouble("AutomobileSpeed")));
                    }
                }
            }
        }
        return fines;
    }

    public Iterator<Fine> getNRecentFines(int N) throws SQLException {
        String sql = "SELECT * FROM Fines LIMIT ?";
        Set<Fine> fines = new HashSet<>(N);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, N);  // corregido: antes estaba pstmt.setInt(3, N)

            try (ResultSet rs = pstmt.executeQuery()) {
                InfractionTypesDAO infractionDao = new InfractionTypesDAO();
                AutomobileDAO automobileDao = new AutomobileDAO();
                PhotosDAO photoDao = new PhotosDAO();

                while (rs.next()) {
                    Integer FineID = rs.getInt("Fine_ID");
                    Timestamp timestamp = rs.getTimestamp("Fine_DateTime");
                    String address = rs.getString("Fine_Address");
                    BigDecimal latitude = rs.getBigDecimal("Fine_Latitude");
                    BigDecimal longitude = rs.getBigDecimal("Fine_Longitude");
                    String uuid = rs.getString("Issuer_DeviceUUID");
                    Integer AutomobileID = rs.getInt("Automobile_ID");
                    Integer InfractionTypeID = rs.getInt("InfractionType_ID");

                    if (InfractionTypeID != 1) {
                        fines.add(new Fine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(InfractionTypeID),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID)));
                    } else {
                        fines.add(new ExcessiveSpeedFine(FineID,
                                new EventGeolocation(timestamp.toLocalDateTime(), address,
                                        new Location(latitude, longitude),
                                        UrbanMonitoringCenter.getUrbanMonitoringCenter()
                                                .getSpecificDevice(UUID.fromString(uuid))),
                                infractionDao.getInfractionTypeByID(1),
                                automobileDao.getAutomobileByAutomobileID(AutomobileID),
                                photoDao.getAllPhotosFromFine(FineID),
                                rs.getDouble("SpeedLimit"),
                                rs.getDouble("AutomobileSpeed")));
                    }
                }
            }
        }

        return fines.iterator();
    }

}