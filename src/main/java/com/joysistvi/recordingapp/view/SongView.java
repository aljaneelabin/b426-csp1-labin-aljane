package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;
import java.util.Scanner;

public class SongView {

    private final SongService songService;
    private final Scanner scanner;

    public SongView(SongService songService, Scanner scanner) {
        this.songService = songService;
        this.scanner = scanner;
    }

    // 1. Song Management Menu
    public void handleMenu() {

        while (true) {

            System.out.println("\n=== MANAGE SONGS ===");
            System.out.println("1. View Songs");
            System.out.println("2. Create Song");
            System.out.println("3. Update Song");
            System.out.println("4. Search Song");
            System.out.println("5. Archive Song");
            System.out.println("6. Restore Song");
            System.out.println("7. Delete Song");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");

            switch (choice) {

                case 1 -> displayAllSongs();

                case 2 -> promptCreateSong();

                case 3 -> promptUpdateSong();

                case 4 -> promptSearchSongs();

                case 5 -> promptArchiveSong();

                case 6 -> promptRestoreSong();

                case 7 -> promptDeleteSong();

                case 0 -> {
                    return;
                }

                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // 2. Display All Active Songs
    public void displayAllSongs() {

        printSongTable(
                "\n----- Active Songs -----",
                songService.getAllActiveSongs(),
                "No active songs found."
        );
    }

    // 3. Create Song
    public void promptCreateSong() {

        System.out.println("\n=== CREATE SONG ===");

        System.out.print("Enter song title: ");
        String title = scanner.nextLine();

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter song length (e.g. 03:45): ");
        String length = scanner.nextLine();

        int albumId = promptForInt("Enter album ID: ");

        boolean created = songService.createSong(
                title, genre, length, albumId
        );

        if (created) {
            System.out.println("Song '" + title.trim() + "' created successfully!");
        } else {
            System.out.println("Failed to create song.");
        }
    }

    // 4. Update Song
    public void promptUpdateSong() {

        System.out.println("\n=== UPDATE SONG ===");

        displayAllSongs();

        int id = promptForInt("Enter song ID to update: ");

        System.out.print("Enter new song title: ");
        String title = scanner.nextLine();

        System.out.print("Enter new genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter new song length: ");
        String length = scanner.nextLine();

        int albumId = promptForInt("Enter new album ID: ");

        boolean updated = songService.updateSong(
                id, title, genre, length, albumId
        );

        if (updated) {
            System.out.println("Song ID " + id + " updated successfully!");
        } else {
            System.out.println("No song updated (check ID or input).");
        }
    }

    // 5. Search Songs
    public void promptSearchSongs() {

        System.out.println("\n=== SEARCH SONGS ===");

        System.out.print("Enter song title keyword: ");
        String keyword = scanner.nextLine();

        List<Song> matches = songService.searchSongs(keyword);

        printSongTable(
                "\n----- Search Results (Keyword: '" + keyword.trim() + "') -----",
                matches,
                "No matching active songs found."
        );
    }

    // 6. Archive Song
    public void promptArchiveSong() {

        System.out.println("\n=== ARCHIVE SONG ===");

        displayAllSongs();

        int id = promptForInt("Enter song ID to archive: ");

        boolean archived = songService.archiveSong(id);

        if (archived) {
            System.out.println("Song ID " + id + " archived successfully!");
        } else {
            System.out.println("No song archived (check ID or input).");
        }
    }

    // 7. Restore Song
    public void promptRestoreSong() {

        System.out.println("\n=== RESTORE SONG ===");

        List<Song> archivedSongs = songService.getAllArchivedSongs();

        printSongTable(
                "\n----- Archived Songs -----",
                archivedSongs,
                "No archived songs found."
        );

        if (archivedSongs == null || archivedSongs.isEmpty()) {
            return;
        }

        int id = promptForInt("Enter song ID to restore: ");

        boolean restored = songService.restoreSong(id);

        if (restored) {
            System.out.println("Song ID " + id + " restored successfully!");
        } else {
            System.out.println("No archived song restored (check ID or input).");
        }
    }

    // 8. Delete Song Permanently
    public void promptDeleteSong() {

        System.out.println("\n=== DELETE SONG ===");

        displayAllSongs();

        int id = promptForInt("Enter song ID to permanently delete: ");

        boolean deleted = songService.deleteSong(id);

        if (deleted) {
            System.out.println("Song ID " + id + " deleted successfully!");
        } else {
            System.out.println("No song deleted (check ID or input).");
        }
    }

    // 9. Display Song Table
    private void printSongTable(
            String header,
            List<Song> songs,
            String emptyMessage) {

        System.out.println(header);

        System.out.printf(
                "%-4s %-22s %-15s %-12s %-8s %-20s%n",
                "ID", "Song Title", "Genre", "Length", "Album ID", "Album Name"
        );

        if (songs == null || songs.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }

        for (Song song : songs) {

            System.out.printf(
                    "%-4d %-22s %-15s %-12s %-8d %-20s%n",
                    song.getId(),
                    song.getTitle(),
                    song.getGenre(),
                    song.getLength(),
                    song.getAlbumId(),
                    song.getAlbumName()
            );
        }
    }

    // 10. Prompt for Integer
    private int promptForInt(String promptMessage) {

        while (true) {

            System.out.print(promptMessage);

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a valid number."
                );
            }
        }
    }
}