package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.UpdatePlaylistInterface;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.PlaylistProvider;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.PlaylistRecyclerAdap;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 25-03-2017.
 */

public class Playlists extends Fragment implements UpdatePlaylistInterface {
    public RecyclerView recyclerView;
    public ArrayList<Songs> s = new ArrayList<>();
    public ArrayList<com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist> playlists = new ArrayList<>();
    private final static String[] columns ={"DISTINCT "+MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.DATA, MediaStore.Audio.Playlists.NAME,MediaStore.Audio.Playlists.DATE_ADDED, MediaStore.Audio.Playlists.DATE_MODIFIED};
    private final String where = "is_music AND duration > 10000 AND _size <> '0' ";
    private final String orderBy =  MediaStore.Audio.Playlists.NAME;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listviewofsongs, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new PlaylistRecyclerAdap(getContext(),playlists,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new getPalylist().execute();
        return v;
    }

    @Override
    public void reloadPlayList() {
        new getPalylist().execute();
    }


    public class getPalylist extends AsyncTask<Void,Void,Void>
    {
        Cursor cursor;
        public getPalylist() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            playlists.clear();
            cursor = getContext().getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.getAdapter().notifyDataSetChanged();
            Log.d("playlist size",playlists.size()+"");

        }

        @Override
        protected Void doInBackground(Void... params) {
          playlists.addAll(PlaylistProvider.queryPlaylists(getContext().getContentResolver()));
            return null;
        }
    }


}
