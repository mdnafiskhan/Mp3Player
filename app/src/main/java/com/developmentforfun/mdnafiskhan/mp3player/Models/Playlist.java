package com.developmentforfun.mdnafiskhan.mp3player.Models;

/**
 * Created by mdnafiskhan on 24/01/2018.
 */

public class Playlist {
    String name;
    String data;
    String dateAdded;
    String lastModified;
    String noOfSongs;
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getNoOfSongs() {
        return noOfSongs;
    }

    public void setNoOfSongs(String noOfSongs) {
        this.noOfSongs = noOfSongs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
