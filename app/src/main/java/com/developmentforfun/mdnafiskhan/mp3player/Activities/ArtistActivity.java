package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.albumdetailadapter;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.artistdetailaddapter;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 05-02-2017.
 */

public class ArtistActivity extends Activity {
   ListView listView;
    String where;
    MusicService musicService;
    boolean mBound;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> album = new ArrayList<>();
    ArrayList<Long> duration = new ArrayList<>();
    ArrayList<Songs> give = new ArrayList<>();
    final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID};
    final static String orderBy = MediaStore.Audio.Media.TITLE;
    Cursor cursor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistactivity);
        Intent i = new Intent(this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        String artist =(String) getIntent().getCharSequenceExtra("artist");
        where= "is_music AND duration > 10000 AND _size <> '0' AND artist = \""+artist+"\" ";
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where,null, orderBy);
        listView = (ListView) findViewById(R.id.artistlistview);
        Log.d("note","going to load song");
        new loadsong().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicService.setplaylist(give,position);
                musicService.setMediaPlayer();

            }
        });

    }

    public class loadsong extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("note","in exicute method");
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(int i=0;i<cursor.getCount();i++) {
                if(i==0)
                cursor.moveToFirst();
                else
                {
                    cursor.moveToNext();
                }
                Songs songs= new Songs();
                songs.settitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                songs.setSonguri(Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))));
                songs.setartist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                songs.setalbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                songs.setDuration(Long.decode(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                songs.setPosition(i);
                give.add(songs);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("note","going to set the list view adapter");
            listView.setAdapter(new artistdetailaddapter(getBaseContext(),give));

        }


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
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }
}
