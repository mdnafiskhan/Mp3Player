package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

/**
 * Created by mdnafiskhan on 13/03/2018.
 */

public class CloneSong {

   public static SongEntity cloneInto( Songs songs)
    {
        SongEntity songEntity = new SongEntity();
        songEntity.setSongName(songs.getTitle());
        songEntity.setSongUri(songs.getSonguri().toString());
        songEntity.setSongId(songs.getSongId());
        songEntity.setAlbum(songs.getAlbum());
        songEntity.setAlbumId(songs.getAlbumId());
        songEntity.setAlbumArt(songs.getAlbumart());
        songEntity.setArtistName(songs.getArtist());
        songEntity.setDuration(songs.getDuration());
        songEntity.setPosition(songs.getPosition());
        songEntity.setUid(songs.getSongId());
        return songEntity;
    }

}
