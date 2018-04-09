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
 * Created by mdnafiskhan on 17/01/2018.
 */

public class Genre_TracksFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Songs> give = new ArrayList<>();
    String genre,genreId;
    Map<String,ArrayList<Songs>> genreToSongs = new HashMap<>();
    CustomRecyclerViewAdapter0 customRecyclerViewAdapter0;
    public Genre_TracksFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.genre =(String) getArguments().get("genre");
        this.genreId =(String) getArguments().get("genreId");
        genreToSongs = Mp3PlayerApplication.applicationSongsContent.getGenreIdToSongs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.artistactivity,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.artistlistview);
        new loadsong().execute();
    }

    public class loadsong extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("note","in exicute method");
        }

        @Override
        protected Void doInBackground(Void... params) {
            give = genreToSongs.get(genreId);
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

