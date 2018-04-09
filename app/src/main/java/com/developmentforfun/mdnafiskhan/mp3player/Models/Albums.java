package com.developmentforfun.mdnafiskhan.mp3player.Models;

/**
 * Created by mdnafiskhan on 16/01/2018.
 */

public class Albums {

    String albumName ;
    String artistName;
    int noOfSong;
    String albumId;
    String albumArt;

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getNoOfSong() {
        return noOfSong;
    }

    public void setNoOfSong(int noOfSong) {
        this.noOfSong = noOfSong;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
