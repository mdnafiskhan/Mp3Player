package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.ListViewAdapter;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.albumdetailadapter;

import java.util.ArrayList;


/**
 * Created by mdnafiskhan on 04-01-2017.
 */

public class AlbumDetail extends Activity{
    ArrayList<Songs> give = new ArrayList<>();
    MediaPlayer mp = new MediaPlayer();
    MusicService musicService;
    boolean mBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumdetail);
        Bundle b= getIntent().getExtras();
        songDetailloader loader = new songDetailloader(getBaseContext());
        String album =(String) b.getCharSequence("album","ALBUM");
       // final int pos = loader.getpositionofalbumwithalbum(album);
        Log.d("recived","Album name forn d "+album);
        String image = loader.albumartwithalbum(album);
        ImageView albumimage =  (ImageView) findViewById(R.id.albumart);
        TextView albumname = (TextView) findViewById(R.id.albumname);
        ListView listView = (ListView) findViewById(R.id.listView);
        give = loader.getSongs("album",album);
        albumdetailadapter albumdetailadapter = new albumdetailadapter(this,give);
        listView.setAdapter(albumdetailadapter);
        Intent i = new Intent(this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        albumname.setText(album);
        if(image!=null)
        albumimage.setImageBitmap(BitmapFactory.decodeFile(image));
        else
        {
            albumimage.setImageResource(R.drawable.default_album_small_light);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    musicService.setplaylist(give,position);
                    musicService.setMediaPlayer();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("","error occured");
                }


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
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }


}
