package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Genre;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.ArtistRecyclerView;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.GenreRecyclerView;

import java.util.ArrayList;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 09/01/2018.
 */

public class GenresFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Genre> genre = new ArrayList<>();
    final String[] columns3 = {MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME};
    final static String orderBy3 = MediaStore.Audio.Genres.NAME;
    public Cursor cursor3;
    public GenresFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","genre");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.listviewofsongs,container,false);
        genre = Mp3PlayerApplication.applicationSongsContent.getAllGenre();
        recyclerView  = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new GenreRecyclerView(getActivity(),genre));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        return v;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
