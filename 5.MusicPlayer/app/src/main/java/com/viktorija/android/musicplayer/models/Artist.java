package com.viktorija.android.musicplayer.models;

import java.util.ArrayList;

public class Artist {

    /**
     * Artist Name
     */
    private String name;

    /**
     * List of albums released by the artist
     */
    private ArrayList<Album> albums;

    /**
     * Artist Thumbnail resource id
     */
    private int thumbnailId;

    /**
     * Create a new Artist object.
     *
     * @param name        is the artist name
     * @param albums      albums that belong to an artist
     * @param thumbnailId is is the drawable resource ID for the thumbnail associated with the artist
     */
    public Artist(String name, ArrayList<Album> albums, int thumbnailId) {
        this.name = name;
        this.albums = albums;
        this.thumbnailId = thumbnailId;
    }

    /**
     * Get the name of the Artist
     */
    public String getName() {
        return name;
    }

    /**
     * Get the count of the albums the Artist has
     */
    public int getAlbumsCount() {
        return albums.size();
    }

    /**
     * Get the thumbnail ID of the Artist
     */
    public int getThumbnailId() {
        return thumbnailId;
    }

    /**
     * Get albums that belong to an artist
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }
}