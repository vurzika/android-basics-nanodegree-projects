package com.viktorija.android.musicplayer.models;

import java.util.ArrayList;

public class Album {

    /**
     * Album Name
     */
    private String name;

    /**
     * List of songs inside an album
     */
    private ArrayList<Song> songs;

    /**
     * Album Release Year
     */
    private int year;

    /**
     * Album Thumbnail Resource Id
     */
    private int thumbnailId;

    /**
     * Create a new Album object.
     *
     * @param name        is the album name
     * @param songs       list of songs inside album
     * @param year        album release year
     * @param thumbnailId is is the drawable resource ID for the thumbnail associated with the album
     */
    public Album(String name, ArrayList<Song> songs, int year, int thumbnailId) {
        this.name = name;
        this.songs = songs;
        this.year = year;
        this.thumbnailId = thumbnailId;
    }

    /**
     * Get the name of the Album
     */
    public String getName() {
        return name;
    }

    /**
     * Get the count of the songs in the Album
     */
    public int getSongCount() {
        return songs.size();
    }

    /**
     * Get list of songs in the album
     */
    public ArrayList<Song> getSongs() {
        return songs;
    }

    /**
     * Get the year of the Album
     */
    public int getYear() {
        return year;
    }

    /**
     * Get album thumbnail resource id
     */
    public int getThumbnailId() {
        return thumbnailId;
    }
}

