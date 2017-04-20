package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.SongfullistViewAdapter;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 25-03-2017.
 */

public class Playlist extends Activity {
    ArrayList<Songs> s = new ArrayList<>();
    MusicService musicService = new MusicService();
    boolean mBound ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplaylist);
        DataBaseClass dataBaseClass = new DataBaseClass(getBaseContext());
        CharSequence playlistname = getIntent().getCharSequenceExtra("playlistname");
        ListView listView = (ListView) findViewById(R.id.myplaylist);
        s = dataBaseClass.getfromplaylist(playlistname.toString());
        listView.setAdapter(new SongfullistViewAdapter(getBaseContext(),s));
        Intent i = new Intent(getBaseContext(),MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicService.setplaylist(s, position);
                musicService.setMediaPlayer();
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService  =  binder.getService();
            mBound =true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound =false;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        try{
          unbindService(serviceConnection);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
