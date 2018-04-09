package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by mdnafiskhan on 19/03/2018.
 */
@Dao
public interface MostPlayedSongInterface {

    @Query("SELECT * FROM MostPlayedEntity")
    List<MostPlayedEntity> getAll();

    @Query("SELECT * FROM MostPlayedEntity WHERE songId == (:songid)")
    List<MostPlayedEntity> getSongById(String songid);

    @Insert
    void insertOne(MostPlayedEntity mostPlayedEntity);

    @Delete
    void delete(MostPlayedEntity mostPlayedEntity);
}
