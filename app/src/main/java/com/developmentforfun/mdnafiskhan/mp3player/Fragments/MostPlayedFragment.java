package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.AppDatabase;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.ConvertToSongFromMostPlayedSongEntity;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.MostPlayedEntity;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter2;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class MostPlayedFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Songs> songs = new ArrayList<>();
    AppDatabase appDatabase;
    CustomRecyclerViewAdapter2 customRecyclerViewAdapter2;
    public static MostPlayedFragment newInstance()
    {
        MostPlayedFragment mostPlayedFragment = new MostPlayedFragment();
        return mostPlayedFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listviewofsongs, container, false);
        appDatabase =  Room.databaseBuilder(getContext(),
                AppDatabase.class, "database-name").build();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        new GetMostPlayedSongs().execute();
        recyclerView.setAdapter(customRecyclerViewAdapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customRecyclerViewAdapter2 = new CustomRecyclerViewAdapter2(getActivity(),songs);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customRecyclerViewAdapter2.unbindService();
    }

    public class GetMostPlayedSongs extends AsyncTask<Void,Void,Void>{

        ArrayList<Songs> songinner = new ArrayList<>();

        public GetMostPlayedSongs() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<MostPlayedEntity> mostPlayedEntities = appDatabase.mostPlayed().getAll();
            songinner = ConvertToSongFromMostPlayedSongEntity.getSongsFromMostPlayed(mostPlayedEntities);
            for(int i=0;i<songinner.size();i++)
            {
                songs.add(songinner.get(i));
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           // Log.d("msg",""+songs.toString());
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }


}
