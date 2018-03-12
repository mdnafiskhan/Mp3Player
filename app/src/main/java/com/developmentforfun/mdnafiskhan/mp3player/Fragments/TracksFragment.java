package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 03-01-2017.
 */

public class TracksFragment extends Fragment {
    SongDetailLoader loader = new SongDetailLoader();
    ArrayList<Songs> give = new ArrayList<>();
    public int pos = -1;
    MediaPlayer mp ;
    MusicService musicService;
    boolean mBound;
    AlertDialog dis;
    private Cursor cursor ;
    RecyclerView recyclerView ;
    int albumindex,dataindex,titleindex,durationindex,artistindex;
    private final static String[] columns ={MediaStore.Audio.Media.DATA,MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID};
    private final String where = "is_music AND duration > 10000 AND _size <> '0' ";
    private final String orderBy =  MediaStore.Audio.Media.TITLE;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    public TracksFragment() {

    }
    public static TracksFragment newInstance()
    {
      TracksFragment tracksFragment = new TracksFragment();
      return tracksFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fragment created","created");
        loader.set(getContext());
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(getActivity(),give);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v =inflater.inflate(R.layout.listviewofsongs,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setVisibility(View.GONE);
        new allsongs().execute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getActivity(),MusicService.class);
                getActivity().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        }).start();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("fragment","instance saved");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("Fragment","Instance Restored");
    }


    public void intlistview()
    {
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment","Destroyed");
       try{
           getActivity().unbindService(serviceConnection);
       }catch (Exception e)
       {
            e.printStackTrace();
       }
        try{
            customRecyclerViewAdapter.unbindService();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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


    public void allsongs()
    {
        cursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
        dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++)
        {
            Songs song = new Songs();
            song.setalbum(cursor.getString(albumindex));
            song.settitle(cursor.getString(titleindex));
            song.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            song.setSonguri(Uri.parse(cursor.getString(dataindex)));
            song.setartist(cursor.getString(artistindex));
            song.setDuration(Long.decode(cursor.getString(durationindex)));
            song.setPosition(cursor.getPosition());
            song.setSongId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            song.setAlbumart(loader.albumartwithalbum(song.getalbum()));
            this.give.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("Size of tracks list",give.size()+"");

    }

    public class allsongs extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            give.clear();
           // customRecyclerViewAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            allsongs();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            intlistview();
        }
    }
public static class insertintoplaylist extends AsyncTask<Void,Void,Void>
{
    Songs s = new Songs();
    ArrayList<Songs> songs = new ArrayList<>();
    Context context;
    String playlistname ;
    boolean inserted=true;
    public insertintoplaylist(Context context ,Songs s ,String playlistname) {
        super();
        this.context = context;
        this.s=s;
        this.playlistname = playlistname;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("here1","  ");
        DataBaseClass db = new DataBaseClass(context);
        //inserted = db.insertintoplaylist(s, playlistname);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(!inserted)
        {
            Toast.makeText(context,"Song already exist in this playlist",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,"Added successfully in playlist",Toast.LENGTH_SHORT).show();
        }
    }
}

    @Override
    public void onStop() {
        super.onStop();
        try{
             customRecyclerViewAdapter.unbindService();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
