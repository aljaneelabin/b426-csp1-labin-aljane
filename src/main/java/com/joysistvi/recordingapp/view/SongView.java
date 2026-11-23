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

    public void handleMenu() {
        while (true) {
            System.out.println("\n=== SONG MANAGEMENT ===");
            System.out.println("1. View All Active Songs");
            System.out.println("2. Create Song");
            System.out.println("3. Update Song");
            System.out.println("4. Search Songs");
            System.out.println("5. Archive Song");
            System.out.println("6. Restore Song");
            System.out.println("7. Delete Song");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1:
                    displayAllSongs();
                    break;
                case 2:
                    promptCreateSong();
                    break;
                case 3:
                    promptUpdateSong();
                    break;
                case 4:
                    promptSearchSongs();
                    break;
                case 5:
                    promptArchiveSong();
                    break;
                case 6:
                    promptRestoreSong();
                    break;
                case 7:
                    promptDeleteSong();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public void displayAllSongs() {
        printSongListTable("----- Active Songs -----", songService.getAllActiveSongs());
    }

    public void promptCreateSong() {
        System.out.println("\n--- Create Song ---");
        System.out.print("Enter song title: ");
        String title = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter length (e.g. 3:45): ");
        String length = scanner.nextLine();
        int albumId = promptForInt("Enter album ID: ");

        if (songService.createSong(title, genre, length, albumId)) {
            System.out.println("Song created successfully.");
        } else {
            System.out.println("Failed to create song.");
        }
    }

    public void promptUpdateSong() {
        System.out.println("\n--- Update Song ---");
        displayAllSongs();

        int id = promptForInt("Enter song ID to update: ");

        Song current = songService.getSongById(id);

        if (current == null) {
            System.out.println("Song not found or invalid ID.");
            return;
        }

        System.out.print("Enter new song title (Press Enter to keep the current ["
                + current.getTitle() + "]): ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            title = current.getTitle();
        }

        System.out.print("Enter new genre (Press Enter to keep the current ["
                + current.getGenre() + "]): ");
        String genre = scanner.nextLine().trim();

        if (genre.isEmpty()) {
            genre = current.getGenre();
        }

        System.out.print("Enter new length (Press Enter to keep the current ["
                + current.getLength() + "]): ");
        String length = scanner.nextLine().trim();

        if (length.isEmpty()) {
            length = current.getLength();
        }

        System.out.print("Enter new album ID (Press Enter to keep the current ["
                + current.getAlbumId() + "]): ");
        String albumInput = scanner.nextLine().trim();

        int albumId;
        if (albumInput.isEmpty()) {
            albumId = current.getAlbumId();
        } else {
            try {
                albumId = Integer.parseInt(albumInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid album ID.");
                return;
            }
        }

        if (songService.updateSong(id, title, genre, length, albumId)) {
            System.out.println("Song updated successfully.");
            displayAllSongs();
        } else {
            System.out.println("Failed to update song.");
        }
    }

    public void promptSearchSongs() {
        System.out.println("\n--- Search Songs ---");
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();
        printSongListTable("----- Search Results -----", songService.searchSongs(keyword));
    }

    public void promptArchiveSong() {
        System.out.println("\n--- Archive Song ---");
        int id = promptForInt("Enter song ID to archive: ");
        if (songService.archiveSong(id)) {
            System.out.println("Song archived successfully.");
        } else {
            System.out.println("Failed to archive song.");
        }
    }

    public void promptRestoreSong() {
        System.out.println("\n--- Restore Song ---");
        List<Song> archived = songService.getAllArchivedSongs();
        if (archived.isEmpty()) {
            System.out.println("No archived songs found.");
            return;
        }
        printSongListTable("----- Archived Songs -----", archived);
        int id = promptForInt("Enter song ID to restore: ");
        if (songService.restoreSong(id)) {
            System.out.println("Song restored successfully.");
        } else {
            System.out.println("Failed to restore song.");
        }
    }

    public void promptDeleteSong() {
        System.out.println("\n--- Delete Song ---");
        int id = promptForInt("Enter song ID to delete permanently: ");
        if (songService.deleteSong(id)) {
            System.out.println("Song deleted successfully.");
        } else {
            System.out.println("Failed to delete song.");
        }
    }

    private void printSongListTable(String header, List<Song> songs) {
        String border = "+-------+------------------------+-----------------+----------+----------------------+";

        System.out.println("\n" + header);
        System.out.println(border);
        System.out.printf("| %-5s | %-22s | %-15s | %-8s | %-20s |%n",
                "ID", "Title", "Genre", "Length", "Album");
        System.out.println(border);

        if (songs == null || songs.isEmpty()) {
            System.out.printf("| %-84s |%n", truncate("No songs found.", 84));
            System.out.println(border);
            return;
        }

        for (Song s : songs) {
            String title = s.getTitle() != null ? s.getTitle() : "";
            String genre = s.getGenre() != null ? s.getGenre() : "";
            String album = s.getAlbumName() != null ? s.getAlbumName() : "";

            System.out.printf("| %-5d | %-22s | %-15s | %-8s | %-20s |%n",
                    s.getId(),
                    truncate(title, 22),
                    truncate(genre, 15),
                    s.getLength(),
                    truncate(album, 20));
        }

        System.out.println(border);
    }

    private String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        if (maxLength <= 3) {
            return text.substring(0, maxLength);
        }
        return text.substring(0, maxLength - 3) + "...";
    }


    private int promptForInt(String promptMessage) {
        while (true) {
            System.out.print(promptMessage);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}