package com.viktorija.android.musicplayer.models;

public class Song {

    /**
     * Song Name
     */
    private String songName;

    /**
     * Song Duration
     */
    private int songDuration;

    /**
     * Create a new Song object.
     *
     * @param songName     is the name of the song
     * @param songDuration is the duration of the song
     */
    public Song(String songName, int songDuration) {
        this.songName = songName;
        this.songDuration = songDuration;
    }

    /**
     * Get the name of the Song
     */
    public String getName() {
        return songName;
    }

    /**
     * Get the duration of the song
     */
    public String getDuration() {
        int minutes = songDuration / 60;
        int seconds = songDuration % 60;
        if (seconds < 10) {
            return minutes + ":0" + seconds;
        } else {
            return minutes + ":" + seconds;
        }
    }
}
