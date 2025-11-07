package com.DAO;

import com.model.Fines.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PhotosDAO {
    public Photo getPhotoByID(Integer PhotoID)throws SQLException{
        String sql = "SELECT Photo_Path FROM Photos WHERE Photo_ID = ?";
        String path;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,PhotoID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                path = rs.getString(1);
            else
                throw new SQLException("No existe la foto con ID: " + PhotoID);

        }
        return  new Photo(path);
    }
    public Set<Photo> getAllPhotosFromFine(Integer FineID) throws SQLException{
        String sql = "SELECT Photo_Path From Photos WHERE Fine_ID = ?";
        Set<Photo> photos = new HashSet<>();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,FineID);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                photos.add(new Photo(rs.getString("Photo_Path")));
            }
        }
        return photos;
    }
    public Integer InsertPhoto(String PhotoPath, Integer FineID)throws SQLException{
        String sql = "INSERT INTO Photos (Photo_Path, FineID) VALUES (?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1,PhotoPath);
            pstmt.setInt(2,FineID);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.getInt(1);
        }
    }
    public void deletePhoto(Integer PhotoID) throws SQLException {
        String sql = "DELETE FROM Photos WHERE Photo_ID = ? ";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1, PhotoID);
            pstmt.executeUpdate();
        }
    }
}