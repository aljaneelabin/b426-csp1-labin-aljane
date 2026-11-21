package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.repository.ArtistRepository;

import java.util.List;


// Application Tier: Business Rule / Validation
public class ArtistServiceImpl implements ArtistService{

    //Constructor Injection
    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    //Read all active
    @Override
    public List<Artist> getAllActiveArtists() {
        return artistRepository.findAllActive();
    }

    // Read all Archived
    @Override
    public List<Artist> getAllArchivedArtists() {
        return artistRepository.findAllArchived();
    }

    //Create
    @Override
    public boolean createArtist(String name){
        // Guard Clause
        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return false;
        }

        Artist artist = new Artist(name);
        return artistRepository.save(artist);
    }

    //Update Artist
    @Override
    public boolean updateArtist(int id, String name) {

        if (id <= 0) {
            System.out.println("\nValidation Error: Invalid artist ID.");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return false;
        }

        Artist artist = new Artist(id, name.trim());

        return artistRepository.update(artist);
    }

    //Delete
    @Override
    public boolean deleteArtist(int id) {

        if (id <= 0) {
            System.out.println("\nValidation Error: Invalid artist ID.");
            return false;
        }

        return artistRepository.deleteById(id);
    }

    //Archive
    @Override
    public boolean archiveArtist(int id) {

        if (id <= 0) {
            System.out.println("\nValidation Error: Invalid artist ID.");
            return false;
        }

        return artistRepository.archiveById(id);
    }

    //Restore
    @Override
    public boolean restoreArtist(int id) {

        if (id <= 0) {
            System.out.println("\nValidation Error: Invalid artist ID.");
            return false;
        }

        return artistRepository.restoreById(id);
    }

    //Search
    @Override
    public List<Artist> searchArtists(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return java.util.Collections.emptyList();
        }

        return artistRepository.searchByName(keyword.trim());
    }

}

