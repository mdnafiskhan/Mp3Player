package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by mdnafiskhan on 13/03/2018.
 */
@Dao
public interface SongInterface {


    @Query("SELECT * FROM songentity")
    List<SongEntity> getAll();

    @Insert
    void insertAll(SongEntity... users);

    @Delete
    void delete(SongEntity user);
}
