package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Albums;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.Artist_Album_RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

/**
 * Created by mdnafiskhan on 16/01/2018.
 */

public class Artist_AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Albums> albumes = new ArrayList<>();
    String artist;
    String artistId;
    TextView noofoalbum;
    Artist_Album_RecyclerView adapter;
    Map<String,ArrayList<Albums>> artistToAlbum = new HashMap<>();

    public Artist_AlbumFragment() {
        super();
        artistToAlbum = Mp3PlayerApplication.applicationSongsContent.getArtistIdToAlbums();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.artist =(String) getArguments().get("artist");
        this.artistId =(String) getArguments().get("artistId");
        Log.d("msg","artistId = "+artistId);
        Log.d("msg","artist = "+artist);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.albumgridview,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.gridview);
        noofoalbum = (TextView) v.findViewById(R.id.NoOfAlbum);
        noofoalbum.setVisibility(View.VISIBLE);
        new LoadAlbumsForArtist().execute();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LoadAlbumsForArtist extends AsyncTask<Void,Void,Void>
    {

        public LoadAlbumsForArtist() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("album size",albumes.size()+"");
            adapter = new Artist_Album_RecyclerView(getActivity(),albumes);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            noofoalbum.setText(""+albumes.size()+" album");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (String name: artistToAlbum.keySet()){
                String value = artistToAlbum.get(name).toString();
                Log.d("msg",name +" album size = "+artistToAlbum.get(name).size()+ " " + value);
                if(name == artistId)
                  Log.d("msg","there ................. is ...................a ....match");
            }
            albumes = artistToAlbum.get(artistId);
            return null;
        }
    }

}
