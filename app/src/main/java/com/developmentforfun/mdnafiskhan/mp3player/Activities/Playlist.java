package com.developmentforfun.mdnafiskhan.mp3player.Activities;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.SongChooserFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 25-03-2017.
 */

public class Playlist extends AppCompatActivity {
    ArrayList<Songs> songses = new ArrayList<>();
    SongDetailLoader loader = new SongDetailLoader();
    MusicService musicService = new MusicService();
    CharSequence playlistName,playlistId;
    boolean mBound ;
    RecyclerView recyclerView;
    Cursor cursor,cursor2;
    private final static String[] columns2 ={"DISTINCT "+ MediaStore.Audio.Playlists.Members.PLAYLIST_ID, MediaStore.Audio.Playlists.Members.AUDIO_ID};
    private final static String[] columns ={MediaStore.Audio.Media.DATA,MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID};
    private final String orderBy =  MediaStore.Audio.Media.TITLE;
    Toolbar toolbar;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_song_layout);
        DataBaseClass dataBaseClass = new DataBaseClass(getBaseContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.addnewsong);
        playlistName = getIntent().getCharSequenceExtra("playlistName");
        playlistId = getIntent().getCharSequenceExtra("playlistId");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview3);
        toolbar.setTitle(playlistName+"");
        toolbar.setNavigationIcon(R.mipmap.left_arrow);
        Intent i = new Intent(getBaseContext(),MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        loader.set(getBaseContext());
        new getPlaylist().execute();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_edit_name");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }
                SongChooserFragment editNameDialog = new SongChooserFragment();
                editNameDialog.show(manager, "fragment_edit_name");

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

    public class getPlaylist extends AsyncTask<Void,Void,Void>
    {

        public getPlaylist() {
            super();
            Log.d("plalistid",playlistId.toString()+"");
            cursor2 = getContentResolver().query( MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(playlistId.toString()) ), columns2, null, null, null);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setAdapter(new CustomRecyclerViewAdapter(Playlist.this,songses));
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(cursor2!=null)
            {
                cursor2.moveToFirst();
                while (!cursor2.isAfterLast())
                {
                    String where =  "is_music AND duration > 10000 AND _size <> '0' AND "+MediaStore.Audio.Media._ID+" = "+cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
                    cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
                    if(cursor!=null)
                    {
                        cursor.moveToFirst();
                        while(!cursor.isAfterLast())
                        {
                            Songs song = new Songs();
                            song.setalbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                            song.settitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                            song.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                            song.setSongId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                            song.setSonguri(Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))));
                            song.setartist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                            song.setDuration(Long.decode(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                            song.setPosition(cursor.getPosition());
                            song.setAlbumart(loader.albumartwithalbum(song.getalbum()));
                            if(!songses.contains(song))
                            songses.add(song);
                            cursor.moveToNext();
                        }
                    }
                    cursor2.moveToNext();
                }
            }
            return null;
        }
    }
}
