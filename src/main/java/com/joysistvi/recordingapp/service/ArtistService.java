package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Artist;

import java.util.List;

public interface ArtistService {

    public List<Artist> getAllActiveArtists();

    public List<Artist> getAllArchivedArtists();

    public boolean createArtist(String name);

    public boolean updateArtist(int id, String name);

    public boolean deleteArtist(int id);

    public boolean archiveArtist(int id);

    public boolean restoreArtist(int id);

    public List<Artist> searchArtists(String keyword);

}