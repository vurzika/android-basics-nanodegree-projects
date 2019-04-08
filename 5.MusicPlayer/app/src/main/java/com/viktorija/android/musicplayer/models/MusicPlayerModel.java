package com.viktorija.android.musicplayer.models;

import com.viktorija.android.musicplayer.R;

import java.util.ArrayList;

/**
 * For simplification this class represents Music Player Model and stores application data and state
 */
public class MusicPlayerModel {

    // Selected Artist
    private static Artist selectedArtist;

    // Selected Album
    private static Album selectedAlbum;

    // Selected Song
    private static Song selectedSong;

    // Music Player Data
    private static ArrayList<Artist> artists = createArtists();

    // Application State Methods

    /**
     * Sets provided artist as selected
     *
     * @param artist artist to select
     */
    public static void setSelectedArtist(Artist artist) {
        selectedArtist = artist;
    }

    /**
     * Returns currently selected artist
     */
    public static Artist getSelectedArtist() {
        return selectedArtist;
    }

    /**
     * Sets provided album as selected
     *
     * @param album album to select
     */
    public static void setSelectedAlbum(Album album) {
        selectedAlbum = album;
    }

    /**
     * Returns currently selected album
     */
    public static Album getSelectedAlbum() {
        return selectedAlbum;
    }

    /**
     * Sets provided song as selected
     *
     * @param song to select
     */
    public static void setSelectedSong(Song song) {
        selectedSong = song;
    }

    /**
     * Returns currently selected song
     */
    public static Song getSelectedSong() {
        return selectedSong;
    }

    // Application Data Methods

    /**
     * Returns list of all artists for application
     */
    public static ArrayList<Artist> getArtists() {
        return artists;
    }

    /**
     * Creates list of artists and related data
     */
    private static ArrayList<Artist> createArtists() {
        ArrayList<Artist> artists = new ArrayList<>();

        artists.add(new Artist("Coldplay", createColdplayAlbums(), R.drawable.artist_coldplay));
        artists.add(new Artist("Muse", createMuseAlbums(), R.drawable.artist_muse));
        artists.add(new Artist("Brainstorm", createBrainstormAlbums(), R.drawable.artist_brainstorm));
        artists.add(new Artist("One Republic", createOneRepublicAlbums(), R.drawable.artist_one_republic));

        return artists;
    }

    private static ArrayList<Album> createColdplayAlbums() {
        ArrayList<Album> albums = new ArrayList<>();

        albums.add(new Album("A Head Full Of Dreams", createAlbumAHeadFullOfDreamsSongs(), 2015, R.drawable.album_coldplay_dreams));
        albums.add(new Album("Ghost Stories", createAlbumGhostStoriesSongs(), 2014, R.drawable.album_coldplay_ghost_stories));
        albums.add(new Album("Kaleidoscope EP", createAlbumKaleidoscopeEPSongs(), 2017, R.drawable.album_colbplay_kaleidoscope));
        albums.add(new Album("Parachutes", createAlbumParachutesSongs(), 2000, R.drawable.album_coldplay_parachutes));

        return albums;
    }

    private static ArrayList<Album> createMuseAlbums() {
        ArrayList<Album> albums = new ArrayList<>();

        albums.add(new Album("The Resistance", createAlbumTheResistanceSongs(), 2009, R.drawable.album_muse_resistance));
        albums.add(new Album("The 2nd Law", createAlbumThe2ndLawSongs(), 2012, R.drawable.album_muse_2nd_law));

        return albums;
    }

    private static ArrayList<Album> createBrainstormAlbums() {
        ArrayList<Album> albums = new ArrayList<>();

        albums.add(new Album("7 Steps of Fresh Air", createAlbum7StepsOfFreshAirSongs(), 2015, R.drawable.album_brainstorms_7_steps));
        albums.add(new Album("Another Still Life ", createAlbumAnotherStillLifeSongs(), 2012, R.drawable.album_brainstorm_another_still_life));
        albums.add(new Album("Years and Seconds", createAlbumYearsAndSecondsSongs(), 2010, R.drawable.album_brainstorm_years_seconds));

        return albums;
    }

    private static ArrayList<Album> createOneRepublicAlbums() {
        ArrayList<Album> albums = new ArrayList<>();

        albums.add(new Album("Oh My My", createAlbumOhMyMySongs(), 2016, R.drawable.album_one_republic_oh_my_my));

        return albums;
    }

    private static ArrayList<Song> createAlbumAHeadFullOfDreamsSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Birds", 3 * 60 + 49));
        songs.add(new Song("Hymn for the Weekend", 4 * 60 + 18));
        songs.add(new Song("Everglow", 4 * 60 + 42));

        return songs;
    }

    private static ArrayList<Song> createAlbumGhostStoriesSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Always in My Head", 3 * 60 + 36));
        songs.add(new Song("Magic", 4 * 60 + 45));
        songs.add(new Song("Ink", 3 * 60 + 48));
        songs.add(new Song("True Love", 4 * 60 + 5));
        songs.add(new Song("Midnight", 4 * 60 + 54));

        return songs;
    }

    private static ArrayList<Song> createAlbumParachutesSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Yellow", 4 * 60 + 29));
        songs.add(new Song("Trouble", 4 * 60 + 31));

        return songs;
    }

    private static ArrayList<Song> createAlbumKaleidoscopeEPSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Hypnotised", 6 * 60 + 31));
        songs.add(new Song("Something Just like This", 4 * 60 + 34));
        songs.add(new Song("Aliens", 4 * 60 + 42));
        songs.add(new Song("All I Can Think About Is You", 4 * 60 + 34));

        return songs;
    }

    private static ArrayList<Song> createAlbumTheResistanceSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Uprising", 5 * 60 + 3));
        songs.add(new Song("Undisclosed Desires", 3 * 60 + 56));
        songs.add(new Song("Resistance", 5 * 60 + 46));

        return songs;
    }

    private static ArrayList<Song> createAlbumThe2ndLawSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Animals", 4 * 60 + 23));
        songs.add(new Song("Follow Me", 3 * 60 + 51));
        songs.add(new Song("Big Freeze", 4 * 60 + 41));
        songs.add(new Song("Save Me", 5 * 60 + 9));
        songs.add(new Song("Liquid State", 3 * 60 + 3));

        return songs;
    }

    private static ArrayList<Song> createAlbum7StepsOfFreshAirSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Waiting", 4 * 60 + 35));
        songs.add(new Song("Stay Young and Beautiful", 3 * 60 + 31));
        songs.add(new Song("Snow Falls in Reverse", 3 * 60 + 31));

        return songs;
    }

    private static ArrayList<Song> createAlbumAnotherStillLifeSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Lantern", 4 * 60 + 30));
        songs.add(new Song("Europa", 4 * 60 + 34));
        songs.add(new Song("Violet", 4 * 60 + 52));
        songs.add(new Song("Knife", 4 * 60 + 6));
        songs.add(new Song("Men of Snow", 2 * 60 + 57));

        return songs;
    }

    private static ArrayList<Song> createAlbumYearsAndSecondsSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Years and Seconds", 4 * 60 + 52));
        songs.add(new Song("Your Call", 3 * 60 + 34));
        songs.add(new Song("On My Way", 4 * 60 + 16));
        songs.add(new Song("Siam", 64));

        return songs;
    }

    private static ArrayList<Song> createAlbumOhMyMySongs() {
        ArrayList<Song> songs = new ArrayList<>();

        songs.add(new Song("Fingertips", 4 * 60 + 16));
        songs.add(new Song("Heaven", 4 * 60 + 19));
        songs.add(new Song("Choke", 3 * 60 + 47));
        songs.add(new Song("Wherever I Go", 2 * 60 + 50));
        songs.add(new Song("Future Looks Good", 3 * 60 + 31));

        return songs;
    }
}