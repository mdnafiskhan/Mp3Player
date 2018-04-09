package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

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
import com.developmentforfun.mdnafiskhan.mp3player.Sorting.SortSongs;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter3;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 19/03/2018.
 */

public class RecentlyAdded extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Songs> songs = new ArrayList<>();
    CustomRecyclerViewAdapter3 customRecyclerViewAdapter3;
    public RecentlyAdded() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.songs = Mp3PlayerApplication.applicationSongsContent.getSongs();
        this.songs = SortSongs.sortByDateAdded(songs);
        /*for(int i=0;i<songs.size();i++)
        {
            Log.d("msg","data added "+songs.get(i).getDateAdded()+" "+songs.get(i).getTitle());
        }*/

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.listviewofsongs,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        customRecyclerViewAdapter3 = new CustomRecyclerViewAdapter3(getContext(),songs);
        recyclerView.setAdapter(customRecyclerViewAdapter3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customRecyclerViewAdapter3.unbindService();
    }
}
