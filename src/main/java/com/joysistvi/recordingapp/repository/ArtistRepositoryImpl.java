package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepositoryImpl implements ArtistRepository {

    // Constructor Injection
    // Composition -> has a relationship
    // Using Composition Artist Repository is loosely coupled to DbConnection

    private final DbConnection dbConnection;

    public ArtistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    //Read all active
    @Override
    public List<Artist> findAllActive() {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT * FROM artists WHERE is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                artists.add(new Artist(id, name));


            }

        } catch (SQLException e) {
            System.out.println("Read Artists Error: " + e.getMessage());
        }

        return artists;
    }

    // Read all archive artists
    @Override
    public List<Artist> findAllArchived() {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT * FROM artists WHERE is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                artists.add(new Artist(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Archived Artists Error: " + e.getMessage());
        }

        return artists;
    }


    @Override
    public boolean save(Artist artists) {
        String query = "INSERT INTO artists (name) VALUES (?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, artists.getName().trim());

            return preparedStatement.executeUpdate() > 0;

            //System.out.println("\nArtist: " + artists.getName().trim() + " created successfully!");

        } catch (SQLException e) {
            System.out.println("\nCreate Artist Error: " + e.getMessage());
        }
        return false;
    }

    //Update
    @Override
    public boolean update(Artist artist) {
        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, artist.getName().trim());
            preparedStatement.setInt(2, artist.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update Artist Error: " + e.getMessage());
        }
        return false;
    }

    //Delete
    @Override
    public boolean deleteById(int id) {
        String query = "DELETE FROM artists WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete Artist Error: " + e.getMessage());
        }
        return false;
    }

    //Archived
    @Override
    public boolean archiveById(int id) {

        String query = "UPDATE artists SET is_archived = 1 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Archive Artist Error: " + e.getMessage());
        }
        return false;
    }

    //Restore
    @Override
    public boolean restoreById(int id) {

        String query = "UPDATE artists SET is_archived = 0 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Restore Artist Error: " + e.getMessage());
            return false;
        }
    }

    //Search
    @Override
    public List<Artist> searchByName(String keyword) {

        List<Artist> artists = new ArrayList<>();

        String query = "SELECT id, name FROM artists " +
                "WHERE is_archived = 0 AND name LIKE ? " +
                "ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    artists.add(new Artist(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Search Artists Error: " + e.getMessage());
        }

        return artists;
    }

}

