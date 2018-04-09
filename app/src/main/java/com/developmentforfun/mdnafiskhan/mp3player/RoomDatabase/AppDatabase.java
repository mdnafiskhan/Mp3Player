package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by mdnafiskhan on 13/03/2018.
 */



@Database(entities = {SongEntity.class,MostPlayedEntity.class,EqualiserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SongInterface userDao();
    public abstract MostPlayedSongInterface mostPlayed();
    public abstract EqualiserEntityInterface equaliserEntityInterface();
}
