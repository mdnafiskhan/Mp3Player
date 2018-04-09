package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
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

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityViewModel.MainActivityViewModel;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter1;

import java.util.ArrayList;

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
    CustomRecyclerViewAdapter1 customRecyclerViewAdapter;
    MainActivityViewModel mainActivityViewModel;
    static Observer<Boolean> songUpdate;
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
       // loader.set(getContext());
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        observer();
    }

    void observer()
    {
        songUpdate = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                   Log.d("msg","song is loaded");
                   Log.d("msg",""+Mp3PlayerApplication.applicationSongsContent.getSongs().size());
                   intlistview(Mp3PlayerApplication.applicationSongsContent.getSongs());
            }
        };

        mainActivityViewModel.getUpdateYourSelf().observe(this,songUpdate);
        Log.d("msg","observer is set");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v =inflater.inflate(R.layout.listviewofsongs,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
      //  recyclerView.setVisibility(View.GONE);
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter1(getActivity(),Mp3PlayerApplication.applicationSongsContent.getSongs());
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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


    public void intlistview(ArrayList<Songs> songs)
    {
        //this.give=songs;
       // customRecyclerViewAdapter0.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        Log.d("TrackFragment","onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("msg","TrackFragment onStop");
       /* try{
             customRecyclerViewAdapter0.unbindService();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        */

    }
}
