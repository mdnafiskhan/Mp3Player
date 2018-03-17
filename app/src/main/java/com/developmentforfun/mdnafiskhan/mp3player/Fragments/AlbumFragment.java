package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityViewModel.MainActivityViewModel;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.MainActivity;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.FragmentRequest;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.AlbumRecyclerView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

/**
 * Created by mdnafiskhan on 04-01-2017.
 */

public class AlbumFragment extends Fragment{
    SongDetailLoader songDetailloader  = new SongDetailLoader();
    public AlbumFragment() {
        super();
    }
    RecyclerView recyclerView;
    private static ArrayList<Bitmap> image = new ArrayList<>();
    LinearLayout linearLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =inflater.inflate(R.layout.albumgridview,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.gridview);
        Log.d("msg","album size = "+ Mp3PlayerApplication.applicationSongsContent.getAlbums().size());
        AlbumRecyclerView adapter = new AlbumRecyclerView(getContext(),recyclerView.getWidth(),recyclerView.getHeight(), Mp3PlayerApplication.applicationSongsContent.getAlbums());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setItemAnimator(new ScaleInTopAnimator());
        return v;
        }



}
