package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mdnafiskhan on 16/01/2018.
 */

public class Artist_TracksFragment extends Fragment {

    ArrayList<Songs> give = new ArrayList<>();
    CustomRecyclerViewAdapter0 customRecyclerViewAdapter0;
    RecyclerView recyclerView;
    String artist,artistId;
    Map<String,ArrayList<Songs>> artistToSongs = new HashMap<>();

    public Artist_TracksFragment() {
        super();
        artistToSongs = Mp3PlayerApplication.applicationSongsContent.getArtistIdToSongs();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artist =(String) getArguments().get("artist");
        artistId =(String) getArguments().get("artistId");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.artistactivity,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.artistlistview);
        new loadsong().execute();
        return v;
    }


    public class loadsong extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("note","in exicute method");
        }

        @Override
        protected Void doInBackground(Void... params) {
            give = artistToSongs.get(artistId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("note","going to set the list view adapter");
            customRecyclerViewAdapter0 = new CustomRecyclerViewAdapter0(getActivity(),give);
            recyclerView.setAdapter(customRecyclerViewAdapter0);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customRecyclerViewAdapter0.unbindService();
    }
}
