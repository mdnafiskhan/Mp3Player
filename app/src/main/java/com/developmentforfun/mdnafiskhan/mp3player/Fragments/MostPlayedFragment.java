package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

public class MostPlayedFragment extends Fragment {
    MusicService musicService = new MusicService();
    boolean mBound ;
    ListView listView;
    ArrayList<Songs> s = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.listviewofsongs,container,false);
        listView = (ListView) v.findViewById(R.id.listView);
        new setlikedsongs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.d("recived","mostplayed songlist recived");
        Log.d("Noofsongs",""+s.size());
        Intent i = new Intent(getActivity(),MusicService.class);
        getActivity().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
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
        }
    }

    public class setlikedsongs extends AsyncTask<Void,Void,Void>
    {

        ArrayList<Songs> songs = new ArrayList<>();

        public setlikedsongs() {
            super();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DataBaseClass db= new DataBaseClass(getActivity().getBaseContext());
            songs= db.getmostplayedsongs();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            s=this.songs;
            if(s!=null) {
                listView.setAdapter(new SongfullistViewAdapter(getActivity().getBaseContext(), s));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        musicService.setplaylist(s, position);
                        musicService.setMediaPlayer();

                    }
                });
            }
            else {

                Toast.makeText(getActivity().getBaseContext(),"No song is played till now",Toast.LENGTH_LONG).show();
            }
        }

    }
}
