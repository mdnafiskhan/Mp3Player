package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
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
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.AppDatabase;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.CloneOut;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.SongEntity;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter1;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class FavFragment extends Fragment {

    ArrayList<Songs> songs = new ArrayList<>();
    RecyclerView recyclerView ;
    AppDatabase db;
    CustomRecyclerViewAdapter1 customRecyclerViewAdapter1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listviewofsongs,container,false);
        Log.d("Size of fav",songs.size()+"");
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        db = Room.databaseBuilder(getContext(),
        AppDatabase.class, "database-name").build();
        new GetLikedSongs(getActivity()).execute();
        customRecyclerViewAdapter1 = new CustomRecyclerViewAdapter1(getActivity(),songs);
        recyclerView.setAdapter(customRecyclerViewAdapter1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customRecyclerViewAdapter1.unbindService();
    }

    public class GetLikedSongs extends AsyncTask<Void,Void,Void>
    {
        Context context;
        public GetLikedSongs(Context context) {
            super();
            this.context= context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<SongEntity> songEntities = db.userDao().getAll();
            for(int i=0;i<songEntities.size();i++)
            {
                songs.add(CloneOut.cloneFrom(songEntities.get(i)));
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
