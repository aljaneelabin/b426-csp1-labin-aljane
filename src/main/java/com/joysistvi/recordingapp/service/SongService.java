package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;
import java.util.List;

public interface SongService {

    // Retrieve Songs
    List<Song> getAllActiveSongs();

    List<Song> getAllArchivedSongs();

    // Create and Update
    boolean createSong(String title, String genre, String length, int albumId);

    boolean updateSong(int id, String title, String genre,
                       String length, int albumId);

    // Delete
    boolean deleteSong(int id);

    // Archive and Restore
    boolean archiveSong(int id);

    boolean restoreSong(int id);

    // Search
    List<Song> searchSongs(String keyword);


}
