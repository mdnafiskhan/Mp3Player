package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.SongfullistViewAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class FavFragment extends Fragment {

    ArrayList<Songs> songs = new ArrayList<>();
    RecyclerView recyclerView ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listviewofsongs,container,false);
        Log.d("Size of fav",songs.size()+"");
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        if(songs!=null && songs.size()!=0) {
           new GetLikedSongs(getActivity()).execute();
        }
        else
        {
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.lin2);
            TextView t = new TextView(getActivity());
            t.setText("No Liked Songs");
            t.setTextSize(30);
            t.setTextColor(Color.BLACK);
            t.setGravity(Gravity.CENTER);
            linearLayout.addView(t);
            Toast.makeText(getContext(),"You didn't select any liked songs",Toast.LENGTH_LONG).show();
        }
        return v;
    }

    public class GetLikedSongs extends AsyncTask<Void,Void,Void>
    {
        Context context;
        ProgressDialog  progressDialog;
        public GetLikedSongs(Context context) {
            super();
            this.context= context;
            progressDialog =new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DataBaseClass db= new DataBaseClass(getContext());
          //  songs = db.getlikedsongs();
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setAdapter(new CustomRecyclerViewAdapter(getActivity(),songs));
            progressDialog.dismiss();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
        }
    }
}
