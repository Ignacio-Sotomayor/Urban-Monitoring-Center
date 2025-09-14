package com.DAO;

import com.model.Devices.Location;
import com.model.Fines.EventGeolocation;
import com.model.UrbanMonitoringCenter;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;


public class GeoLocationDAO {

    public static void insertGeoLocation(Timestamp timestamp, String address, BigDecimal latitude, BigDecimal longitude, String UUID)throws SQLException{
        String sql = "INSERT INTO GeoLocations (GeoLocation_DateTime, GeoLocation_Address, Latitude, Longitude, DeviceUUID) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setTimestamp(1,timestamp);
            pstmt.setString(2, address);
            pstmt.setBigDecimal(3, latitude);
            pstmt.setBigDecimal(4, longitude);
            pstmt.setString(5, UUID);

            pstmt.executeUpdate();
        }
    }

    public static EventGeolocation getGeoLocation(long GeoLocation_ID) throws SQLException{
        String sql = "SELECT GeoLocation_DateTime, GeoLocation_Address, Latitude, Longitude, DeviceUUID FROM GeoLocations WHERE GeoLocation_ID = ?";

        LocalDateTime dateTime;
        String address;
        UUID id;
        BigDecimal latitude, longitude;

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ){
            pstmt.setLong(1,GeoLocation_ID);
            ResultSet rs = pstmt.executeQuery();
            dateTime = rs.getTimestamp("GeoLocation_DateTime").toLocalDateTime();
            address = rs.getString("GeoLocation_Address");
            latitude = rs.getBigDecimal("Latitude");
            longitude = rs.getBigDecimal("Longitude");
            id = UUID.fromString(rs.getString("DeviceUUID"));

        }
        return new EventGeolocation(dateTime,address,new Location(latitude,longitude), UrbanMonitoringCenter.getSpecificDevice(id));
    }

    public static void deleteGeoLocation(long GeoLocation_ID) throws SQLException {
        String sql = "DELETE FROM GeoLocations WHERE GeoLocation_ID = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, GeoLocation_ID);
            pstmt.executeUpdate();
        }
    }

}