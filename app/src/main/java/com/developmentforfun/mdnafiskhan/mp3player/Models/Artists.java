package com.developmentforfun.mdnafiskhan.mp3player.Models;

/**
 * Created by mdnafiskhan on 16-01-2017.
 */

public class Artists {

    private int nofosongs ;
    private int noalbums;
    private String artistname ;
    private String artistId;

    public int getNofosongs() {
        return nofosongs;
    }

    public void setNofosongs(int nofosongs) {
        this.nofosongs = nofosongs;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public int getNoalbums() {
        return noalbums;
    }

    public void setNoalbums(int noalbums) {
        this.noalbums = noalbums;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}

