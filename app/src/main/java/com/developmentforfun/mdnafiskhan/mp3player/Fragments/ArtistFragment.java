package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ArtistActivity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.MainActivity;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.ArtistAdapter;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 16-01-2017.
 */

public class ArtistFragment extends Fragment {

    ListView listView ;
    ArrayList<Artists> aa = new ArrayList<>();
    final String[] columns3 = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
    final static String orderBy3 = MediaStore.Audio.Albums.ARTIST;
    public Cursor cursor3;

    public ArtistFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.listviewofsongs,container,false);

        listView  = (ListView) v.findViewById(R.id.listView);
        new artist().execute();
        return v;
    }

    public class artist extends AsyncTask<Void, Void ,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            allartist();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArtistAdapter artistAdapter = new ArtistAdapter(getActivity().getBaseContext(),aa);
            listView.setAdapter(artistAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i= new Intent(getActivity(), ArtistActivity.class);
                    i.putExtra("artist", aa.get(position).getArtistname());
                    i.putExtra("noofsongs",aa.get(position).getNofosongs());
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void setInitialSavedState(SavedState state) {
        super.setInitialSavedState(state);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void allartist()
    {
        cursor3 = getContext().getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, columns3, null, null, orderBy3);
        cursor3.moveToFirst();
        for(int i=0;i< cursor3.getCount() ;i++)
        {
            Artists art = new Artists();
            art.setArtistname(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
            art.setNoalbums(Integer.parseInt(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
            art.setNofosongs(Integer.parseInt(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));
            this.aa.add(art);
            cursor3.moveToNext();
        }
        cursor3.close();
    }
}

