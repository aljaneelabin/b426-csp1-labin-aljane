package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.List;
import java.util.Scanner;

public class ArtistView {

    private final ArtistService artistService;
    private final Scanner scanner;

    public ArtistView(ArtistService artistService) {
        this.artistService = artistService;
        this.scanner = new Scanner(System.in);
    }


    public void displayAllArtists() {
        List<Artist> artists = artistService.getAllActiveArtists();

        System.out.println("\n----- Active Artists -----");
        System.out.printf("%-4s %-20s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No active artists found.");
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-4d %-20s%n",
                    artist.getId(),
                    artist.getName());
        }

    }

    public void promptCreateArtist(){
        System.out.println("\n----- Create New Artist -----");
        System.out.print("Enter artist name: ");
        String name = scanner.nextLine();

        boolean created = artistService.createArtist(name);

        if (created) {
            System.out.println("Artist" + name.trim() + " created successfully");
        } else {
            System.out.println("Failed to create artist.");
        }
    }

    public void promptUpdateArtist() {

        System.out.println("\n----- UPDATE ARTIST -----");

        displayAllArtists();

        System.out.print("\nEnter artist ID to update: ");
        int id = readInt();

        System.out.print("Enter new artist name: ");
        String name = scanner.nextLine();

        boolean updated = artistService.updateArtist(id, name);

        if (updated) {
            System.out.println("Artist updated successfully!");
        } else {
            System.out.println("Failed to update artist.");
        }
    }

    public void promptDeleteArtist() {

        System.out.println("\n----- DELETE ARTIST -----");

        displayAllArtists();

        System.out.print("\nEnter artist ID to delete: ");
        int id = readInt();

        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            System.out.println("Artist deleted successfully!");
        } else {
            System.out.println("Failed to delete artist.");
        }
    }

    public void promptArchiveArtist() {

        System.out.println("\n----- ARCHIVE ARTIST -----");

        displayAllArtists();

        System.out.print("\nEnter artist ID to archive: ");
        int id = readInt();

        boolean archived = artistService.archiveArtist(id);

        if (archived) {
            System.out.println("Artist archived successfully!");
        } else {
            System.out.println("Failed to archive artist.");
        }
    }

    public void promptRestoreArtist() {

        System.out.println("\n----- RESTORE ARTIST -----");

        List<Artist> artists =
                artistService.getAllArchivedArtists();

        System.out.printf("%-5s %-25s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No archived artists found.");
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-5d %-25s%n",
                    artist.getId(),
                    artist.getName());
        }

        System.out.print("\nEnter artist ID to restore: ");
        int id = readInt();

        boolean restored = artistService.restoreArtist(id);

        if (restored) {
            System.out.println("Artist restored successfully!");
        } else {
            System.out.println("Failed to restore artist.");
        }
    }

    public void promptSearchArtists() {

        System.out.println("\n----- SEARCH ARTISTS -----");

        System.out.print("Enter artist name to search: ");
        String keyword = scanner.nextLine();

        List<Artist> artists =
                artistService.searchArtists(keyword);

        if (artists == null || artists.isEmpty()) {
            System.out.println("No matching artists found.");
            return;
        }

        System.out.printf("%-5s %-25s%n", "ID", "Artist Name");

        for (Artist artist : artists) {
            System.out.printf("%-5d %-25s%n",
                    artist.getId(),
                    artist.getName());
        }
    }

    private int readInt() {

        String input = scanner.nextLine();

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }
}
