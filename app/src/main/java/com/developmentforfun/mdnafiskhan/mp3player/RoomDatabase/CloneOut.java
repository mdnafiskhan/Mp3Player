package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import android.net.Uri;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

/**
 * Created by mdnafiskhan on 14/03/2018.
 */

public class CloneOut {
    public static Songs cloneFrom( SongEntity songEntity)
    {
        Songs songs = new Songs();
        songs.setTitle(songEntity.getSongName());
        songs.setSonguri(Uri.parse(songEntity.getSongUri()));
        songs.setSongId(songEntity.getSongId());
        songs.setalbum(songEntity.getAlbum());
        songs.setAlbumId(songEntity.getAlbumId());
        songs.setAlbumart(songEntity.getAlbumArt());
        songs.setArtist(songEntity.getArtistName());
        songs.setDuration(songEntity.getDuration());
        songs.setPosition(songEntity.getPosition());
        return songs;
    }
}
