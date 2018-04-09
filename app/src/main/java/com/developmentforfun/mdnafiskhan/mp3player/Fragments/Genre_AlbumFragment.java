package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.database.Cursor;
import android.net.Uri;
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

public class Genre_AlbumFragment extends Fragment {
        RecyclerView recyclerView;
        ArrayList<Albums> albumes = new ArrayList<>();
        String genre,genreId;
        TextView noofoalbum;
        Map<String,ArrayList<Albums>> genreToAlbum = new HashMap<>();
        public Genre_AlbumFragment() {
        super();
        }
      @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          this.genre =(String) getArguments().get("genre");
          this.genreId =(String) getArguments().get("genreId");
          genreToAlbum = Mp3PlayerApplication.applicationSongsContent.getGenreIdToAlbums();

        }

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.albumgridview,container,false);
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.gridview);
        noofoalbum = (TextView) view.findViewById(R.id.NoOfAlbum);
        noofoalbum.setVisibility(View.VISIBLE);
        new LoadAlbumsForArtist().execute();
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
        Artist_Album_RecyclerView adapter = new Artist_Album_RecyclerView(getActivity(),albumes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        noofoalbum.setText(""+albumes.size()+" album");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        albumes = genreToAlbum.get(genreId);
        return null;
    }
}

}
