package com.developmentforfun.mdnafiskhan.mp3player;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityViewModel.MainActivityViewModel;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.NowPlayingChanged;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.SelectorChangeInterface;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.SongIsLoaded;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter;

/**
 * Created by mdnafiskhan on 13/03/2018.
 */

public class Mp3PlayerApplication extends Application {

    public static ApplicationModelClass applicationModelClass = new ApplicationModelClass();
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    static NowPlayingChanged nowPlayingChanged;
    public  static ApplicationSongsContent applicationSongsContent ;

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
    public void setApplicationModelClass(Songs songs)
    {
        Log.d("msg","Song has been set");
        Log.d("msg","song album art = "+songs.getAlbumart());
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter();
        applicationModelClass.CurrentPlayingSong = songs;
        nowPlayingChanged.setSong(songs);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationSongsContent = new ApplicationSongsContent(getBaseContext());
    }
}
