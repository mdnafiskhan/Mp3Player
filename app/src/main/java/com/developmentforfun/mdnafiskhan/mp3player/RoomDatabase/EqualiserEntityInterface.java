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
public interface EqualiserEntityInterface {

    @Query("SELECT * FROM EqualiserEntity")
    List<EqualiserEntity> getAll();

    @Query("delete from EqualiserEntity")
    void clearDatabase();

    @Insert
    void insertOne(EqualiserEntity equaliserEntity);

}
