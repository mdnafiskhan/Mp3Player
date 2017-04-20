package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.Playlist;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 25-03-2017.
 */

public class Playlists extends Fragment {
    MusicService musicService = new MusicService();
    boolean mBound ;
    ListView listView;
    ArrayList<Songs> s = new ArrayList<>();
    ArrayList<String> playlists = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataBaseClass dataBaseClass = new DataBaseClass(getActivity().getBaseContext());
        View v=inflater.inflate(R.layout.listviewofsongs,container,false);
        listView = (ListView) v.findViewById(R.id.listView);
        playlists = dataBaseClass.findplaylist();
        listView.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,R.id.name,playlists));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), Playlist.class);
                i.putExtra("playlistname",playlists.get(position));
                startActivity(i);
            }
        });

        return v;
    }


}
