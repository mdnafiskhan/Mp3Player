package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by mdnafiskhan on 19/03/2018.
 */

@Entity
public class MostPlayedEntity {

    @PrimaryKey
    @NonNull
    private String uid;
    @ColumnInfo(name = "count")
    private Integer NoOfTimes;

    @ColumnInfo(name = "songName")
    private String SongName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @ColumnInfo(name = "artistName")
    private String ArtistName;

    @ColumnInfo(name = "songAlbum")
    private String Album;

    @ColumnInfo(name = "songDuration")
    private long Duration;

    @ColumnInfo(name = "songPosition")
    private int Position;

    @ColumnInfo(name = "songAlbumArt")
    private String AlbumArt;

    @ColumnInfo(name = "songUri")
    private String SongUri;

    @ColumnInfo(name = "albumId")
    private String AlbumId;

    @ColumnInfo(name = "songId")
    private String SongId;

    @ColumnInfo(name = "dateAdded")
    private String DateAdded;

    public String getSongName() {
        return SongName;
    }

    public String getSongId() {
        return SongId;
    }

    public void setSongId(String songId) {
        SongId = songId;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }


    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public long getDuration() {
        return Duration;
    }

    public void setDuration(long duration) {
        Duration = duration;
    }

    public String getAlbumArt() {
        return AlbumArt;
    }

    public void setAlbumArt(String albumArt) {
        AlbumArt = albumArt;
    }

    public String getSongUri() {
        return SongUri;
    }

    public void setSongUri(String songUri) {
        SongUri = songUri;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public String getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(String dateAdded) {
        DateAdded = dateAdded;
    }

    public Integer getNoOfTimes() {
        return NoOfTimes;
    }

    public void setNoOfTimes(Integer noOfTimes) {
        NoOfTimes = noOfTimes;
    }
}
