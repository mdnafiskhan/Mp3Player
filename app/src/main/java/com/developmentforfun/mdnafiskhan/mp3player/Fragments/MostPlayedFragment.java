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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.SongfullistViewAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class MostPlayedFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Songs> s = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.listviewofsongs,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        new setlikedsongs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return v;
    }

    public static MostPlayedFragment newInstance()
    {
        MostPlayedFragment mostPlayedFragment = new MostPlayedFragment();
        return mostPlayedFragment;
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
          //  songs= db.getmostplayedsongs();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            s=this.songs;
            if(s!=null) {
                recyclerView.setAdapter(new CustomRecyclerViewAdapter(getActivity(),songs));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
                recyclerView.setItemAnimator(new SlideInLeftAnimator());            }
            else {
                Toast.makeText(getActivity().getBaseContext(),"No song is played till now",Toast.LENGTH_LONG).show();
            }
        }

    }
}
