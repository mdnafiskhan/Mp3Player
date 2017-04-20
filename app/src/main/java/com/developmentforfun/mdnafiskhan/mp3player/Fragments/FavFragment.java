package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.SongfullistViewAdapter;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class FavFragment extends Fragment {

    ArrayList<Songs> songs = new ArrayList<>();
    MusicService musicService = new MusicService();
    boolean mBound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listviewofsongs,container,false);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        DataBaseClass db= new DataBaseClass(getContext());
        songs = db.getlikedsongs();
        Log.d("Size of fav",songs.size()+"");
        Intent i = new Intent(getActivity(),MusicService.class);
        getActivity().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        if(songs!=null && songs.size()!=0) {
            listView.setAdapter(new SongfullistViewAdapter(getContext(), songs));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    musicService.setplaylist(songs, position);
                    musicService.setMediaPlayer();
                }
            });
        }
        else
        {
            Toast.makeText(getContext(),"You didn't select any liked songs",Toast.LENGTH_LONG).show();
        }
        return v;
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
            getActivity().unbindService(serviceConnection);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }    }
}
