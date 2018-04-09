package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.net.Uri;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mdnafiskhan on 19/03/2018.
 */

public class ConvertToSongFromMostPlayedSongEntity {

    public static ArrayList<Songs> getSongsFromMostPlayed(List<MostPlayedEntity> mostPlayedEntities)
    {
        Log.d("msg","mostPlayedEntities = "+mostPlayedEntities.toString());
        Collections.sort(mostPlayedEntities,getCompByNoOfTime());
        ArrayList<Songs> songs = new ArrayList<>();
        for(int i=0;i<mostPlayedEntities.size();i++)
        {
            Songs songs1 = new Songs();
            songs1.setDateAdded(mostPlayedEntities.get(i).getDateAdded());
            songs1.setAlbumart(mostPlayedEntities.get(i).getAlbumArt());
            songs1.setAlbum(mostPlayedEntities.get(i).getAlbum());
            songs1.setArtist(mostPlayedEntities.get(i).getArtistName());
            songs1.setPosition(mostPlayedEntities.get(i).getPosition());
            songs1.setDuration(mostPlayedEntities.get(i).getDuration());
            songs1.setAlbumId(mostPlayedEntities.get(i).getAlbumId());
            songs1.setPosition(mostPlayedEntities.get(i).getPosition());
            songs1.setAlbum(mostPlayedEntities.get(i).getAlbum());
            songs1.setSongId(mostPlayedEntities.get(i).getSongId());
            songs1.setTitle(mostPlayedEntities.get(i).getSongName());
            songs1.setSonguri(Uri.parse(mostPlayedEntities.get(i).getSongUri()));
            songs.add(songs1);
        }
        return songs;
    }

    private static Comparator<MostPlayedEntity> getCompByNoOfTime() {
        return new Comparator<MostPlayedEntity>() {
            @Override
            public int compare(MostPlayedEntity s1, MostPlayedEntity s2) {
                return s2.getNoOfTimes().compareTo(s1.getNoOfTimes());
            }
        };
    }
}
