package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Song;
import java.util.List;

public interface SongRepository {
    // Retrieve songs
    List<Song> findAllActive();

    List<Song> findAllArchived();

    // Create and Update
    boolean save(Song song);

    boolean update(Song song);

    // Delete
    boolean deleteById(int id);

    // Archive and Restore
    boolean archiveById(int id);

    boolean restoreById(int id);

    // Search
    List<Song> searchByTitle(String keyword);


}
