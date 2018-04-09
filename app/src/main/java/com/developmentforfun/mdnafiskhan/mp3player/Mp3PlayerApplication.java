package com.developmentforfun.mdnafiskhan.mp3player;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.NowPlayingChanged;
import com.developmentforfun.mdnafiskhan.mp3player.Models.EqualizerBands;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.AppDatabase;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.CloneEqualiser;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.MostPlayedEntity;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter0;

import java.util.List;

/**
 * Created by mdnafiskhan on 13/03/2018.
 */

public class Mp3PlayerApplication extends Application {

    public static ApplicationModelClass applicationModelClass = new ApplicationModelClass();
    CustomRecyclerViewAdapter0 customRecyclerViewAdapter0;
    static NowPlayingChanged nowPlayingChanged;
    public  static ApplicationSongsContent applicationSongsContent ;
    public static EqualizerBands equalizerBands = new EqualizerBands();
    AppDatabase appDatabase;
    public ApplicationModelClass getInstance()
    {
        return applicationModelClass;
    }

    public Mp3PlayerApplication() {
        super();
    }

    public static void setInterface(NowPlayingChanged nowPlayingChanged2)
    {
        nowPlayingChanged = nowPlayingChanged2;
    }
    public void setApplicationModelClass(Songs songs,Context context)
    {
        Log.d("msg","Song has been set");
        Log.d("msg","song album art = "+songs.getAlbumart());
        customRecyclerViewAdapter0 = new CustomRecyclerViewAdapter0();
        applicationModelClass.CurrentPlayingSong = songs;
        nowPlayingChanged.setSong(songs);
        new UpdateMostPlayed(songs,context).execute();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationSongsContent = new ApplicationSongsContent(getBaseContext());
        new GetBandValue().execute();
    }



    public class GetBandValue extends AsyncTask<Void,Void,Void>{

        public GetBandValue() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appDatabase =  Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").build();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(appDatabase.equaliserEntityInterface().getAll().size()>0)
            equalizerBands = CloneEqualiser.CloneFromEqualiserEntityToBand(appDatabase.equaliserEntityInterface().getAll().get(0));
            Log.d("msg","equaliser val getting = "+equalizerBands.getBand0()+" "+equalizerBands.getBand1()+" "+equalizerBands.getBand2()+" "+equalizerBands.getBand3()+" "+equalizerBands.getBand4()+" ");
            return null;
        }
    }




    public class UpdateMostPlayed extends AsyncTask<Void,Void,Void>{
        Songs songs;
        Context context;
        public UpdateMostPlayed(Songs songs,Context context) {
            super();
            this.songs = songs;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appDatabase =  Room.databaseBuilder(context,
                    AppDatabase.class, "database-name").build();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<MostPlayedEntity> songsList = appDatabase.mostPlayed().getSongById(songs.getSongId());
            MostPlayedEntity mostPlayedEntity = new MostPlayedEntity();
            if(songsList.size()==1)
            {
                mostPlayedEntity = songsList.get(0);
                mostPlayedEntity.setNoOfTimes( mostPlayedEntity.getNoOfTimes()+1);
                appDatabase.mostPlayed().delete(songsList.get(0));
                appDatabase.mostPlayed().insertOne(mostPlayedEntity);
            }
            else if(songsList.size()==0)
            {
                mostPlayedEntity.setNoOfTimes(1);
                mostPlayedEntity.setAlbum(songs.getAlbum());
                mostPlayedEntity.setAlbumArt(songs.getAlbumart());
                mostPlayedEntity.setAlbumId(songs.getAlbumId());
                mostPlayedEntity.setArtistName(songs.getArtist());
                mostPlayedEntity.setDateAdded(songs.getDateAdded());
                mostPlayedEntity.setDuration(songs.getDuration());
                mostPlayedEntity.setSongId(songs.getSongId());
                mostPlayedEntity.setSongUri(songs.getSonguri().toString());
                mostPlayedEntity.setUid(songs.getSongId());
                mostPlayedEntity.setPosition(songs.getPosition());
                mostPlayedEntity.setSongName(songs.getTitle());
                appDatabase.mostPlayed().insertOne(mostPlayedEntity);
            }
            return null;
        }
    }


}
