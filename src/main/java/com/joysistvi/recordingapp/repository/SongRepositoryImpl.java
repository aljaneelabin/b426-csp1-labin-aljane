package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongRepositoryImpl implements SongRepository {
    private final DbConnection dbConnection;

    public SongRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // 1. Find All Active Songs
    @Override
    public List<Song> findAllActive() {

        List<Song> songs = new ArrayList<>();

        String query = "SELECT s.id, s.title, s.genre, s.length, " +
                "s.album_id, a.name AS album_name, s.is_archived " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE s.is_archived = 0 " +
                "ORDER BY s.id ASC";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet rs = prep.executeQuery()) {

            while (rs.next()) {

                Song song = new Song(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("length"),
                        rs.getInt("album_id"),
                        rs.getString("album_name")
                );

                song.setArchived(rs.getBoolean("is_archived"));
                songs.add(song);
            }

        } catch (SQLException e) {
            System.out.println("Repository Error (findAllActive): " + e.getMessage());
        }

        return songs;
    }

    // 2. Find All Archived Songs
    @Override
    public List<Song> findAllArchived() {

        List<Song> songs = new ArrayList<>();

        String query = "SELECT s.id, s.title, s.genre, s.length, " +
                "s.album_id, a.name AS album_name, s.is_archived " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE s.is_archived = 1 " +
                "ORDER BY s.id ASC";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet rs = prep.executeQuery()) {

            while (rs.next()) {

                Song song = new Song(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("length"),
                        rs.getInt("album_id"),
                        rs.getString("album_name")
                );

                song.setArchived(rs.getBoolean("is_archived"));
                songs.add(song);
            }

        } catch (SQLException e) {
            System.out.println("Repository Error (findAllArchived): " + e.getMessage());
        }

        return songs;
    }

    // 3. Save Song
    @Override
    public boolean save(Song song) {

        String query = "INSERT INTO songs (title, genre, length, album_id) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, song.getTitle());
            prep.setString(2, song.getGenre());
            prep.setString(3, song.getLength());
            prep.setInt(4, song.getAlbumId());

            return prep.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Repository Error (save): " + e.getMessage());
            return false;
        }
    }

    // 4. Update Song
    @Override
    public boolean update(Song song) {

        String query = "UPDATE songs SET title = ?, genre = ?, " +
                "length = ?, album_id = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, song.getTitle());
            prep.setString(2, song.getGenre());
            prep.setString(3, song.getLength());
            prep.setInt(4, song.getAlbumId());
            prep.setInt(5, song.getId());

            return prep.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Repository Error (update): " + e.getMessage());
            return false;
        }
    }

    // 5. Delete Song Permanently
    @Override
    public boolean deleteById(int id) {

        String query = "DELETE FROM songs WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            return prep.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Repository Error (deleteById): " + e.getMessage());
            return false;
        }
    }

    // 6. Archive Song
    @Override
    public boolean archiveById(int id) {

        String query = "UPDATE songs SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            return prep.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Repository Error (archiveById): " + e.getMessage());
            return false;
        }
    }

    // 7. Restore Song
    @Override
    public boolean restoreById(int id) {

        String query = "UPDATE songs SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            return prep.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Repository Error (restoreById): " + e.getMessage());
            return false;
        }
    }

    // 8. Search Songs by Title
    @Override
    public List<Song> searchByTitle(String keyword) {

        List<Song> songs = new ArrayList<>();

        String query = "SELECT s.id, s.title, s.genre, s.length, " +
                "s.album_id, a.name AS album_name, s.is_archived " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE s.is_archived = 0 AND s.title LIKE ? " +
                "ORDER BY s.id ASC";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword + "%");

            try (ResultSet rs = prep.executeQuery()) {

                while (rs.next()) {

                    Song song = new Song(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("length"),
                            rs.getInt("album_id"),
                            rs.getString("album_name")
                    );

                    song.setArchived(rs.getBoolean("is_archived"));
                    songs.add(song);
                }
            }

        } catch (SQLException e) {
            System.out.println("Repository Error (searchByTitle): " + e.getMessage());
        }

        return songs;
    }
}
