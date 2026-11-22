package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;

public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    // 1. Get All Active Songs
    @Override
    public List<Song> getAllActiveSongs() {
        return songRepository.findAllActive();
    }

    // 2. Get All Archived Songs
    @Override
    public List<Song> getAllArchivedSongs() {
        return songRepository.findAllArchived();
    }

    // 3. Create Song
    @Override
    public boolean createSong(String title, String genre,
                              String length, int albumId) {

        if (!isValidSongInput(title, genre, length, albumId)) {
            return false;
        }

        Song song = new Song(
                title.trim(),
                genre.trim(),
                length.trim(),
                albumId
        );

        return songRepository.save(song);
    }

    // 4. Update Song
    @Override
    public boolean updateSong(int id, String title, String genre,
                              String length, int albumId) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than 0.");
            return false;
        }

        if (!isValidSongInput(title, genre, length, albumId)) {
            return false;
        }

        Song song = new Song(
                id,
                title.trim(),
                genre.trim(),
                length.trim(),
                albumId,
                null
        );

        return songRepository.update(song);
    }

    // 5. Delete Song
    @Override
    public boolean deleteSong(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than 0.");
            return false;
        }

        return songRepository.deleteById(id);
    }

    // 6. Archive Song
    @Override
    public boolean archiveSong(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than 0.");
            return false;
        }

        return songRepository.archiveById(id);
    }

    // 7. Restore Song
    @Override
    public boolean restoreSong(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than 0.");
            return false;
        }

        return songRepository.restoreById(id);
    }

    // 8. Search Songs
    @Override
    public List<Song> searchSongs(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Validation Error: Search keyword cannot be blank.");
            return new ArrayList<>();
        }

        return songRepository.searchByTitle(keyword.trim());
    }

    // Validation Method
    private boolean isValidSongInput(String title, String genre,
                                     String length, int albumId) {

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Validation Error: Song title cannot be blank.");
            return false;
        }

        if (title.trim().length() > 150) {
            System.out.println("Validation Error: Song title must not exceed 150 characters.");
            return false;
        }

        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Validation Error: Genre cannot be blank.");
            return false;
        }

        if (genre.trim().length() > 50) {
            System.out.println("Validation Error: Genre must not exceed 50 characters.");
            return false;
        }

        if (length == null || length.trim().isEmpty()) {
            System.out.println("Validation Error: Song length cannot be blank.");
            return false;
        }

        if (length.trim().length() > 10) {
            System.out.println("Validation Error: Song length must not exceed 10 characters.");
            return false;
        }

        if (albumId <= 0) {
            System.out.println("Validation Error: Album ID must be greater than 0.");
            return false;
        }

        return true;
    }
}