package com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.MediaEvents2;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter0;

import java.util.ArrayList;

import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import jp.wasabeef.blurry.Blurry;

public class NowPlayingSongList_Fragment extends Fragment implements MediaEvents2 {

    MusicService musicService;
    boolean mBound;
    RecyclerView recyclerView;
    ArrayList<Songs> songses = new ArrayList<>();
    ImageView imageView;
    MediaMetadataRetriever data = new MediaMetadataRetriever();

    public static NowPlayingSongList_Fragment getInstance()
    {
        return new NowPlayingSongList_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_now_playing_,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView  = (RecyclerView) view.findViewById(R.id.nowplayingRecyclerView);
        imageView = (ImageView) view.findViewById(R.id.backimage2);
        Intent i = new Intent(getContext(),MusicService.class);
        getContext().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
          }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService  =  binder.getService();
            mBound =true;
            musicService.setCallbacks2(NowPlayingSongList_Fragment.this);
            if(mBound)
            {
                songses = MusicService.songs_current_playlist;
                recyclerView.setAdapter(new CustomRecyclerViewAdapter0(getContext(),songses));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                setBlurBackdround();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound =false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Album_Activity","Distroyed");
        try {
            getContext().unbindService(serviceConnection);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void setBlurBackdround()  /// CALL WHEN EVER SONG GETS CHANGED...
    {
        //   updatePlayerBar(bitmap);
        try {
            data.setDataSource(getContext(),musicService.Current_playing_song.getSonguri());
            byte [] a = data.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(a,0,a.length);
            Blurry.with(getContext()).radius(8)
                    .sampling(5).color(Color.argb(0, 255, 255, 0))
                    .async()
                    .animate(500).from(bitmap).into(imageView);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                    Palette.Swatch swatch2 = palette.getLightMutedSwatch();
                    if(swatch2!=null)
                    {

                    }
                    if (swatch == null)
                        swatch = palette.getDominantSwatch(); // Sometimes vibrant swatch is not available
                    if (swatch != null) {
                        // Set the background color of the player bar based on the swatch color
                        // Update the track's title with the proper title text color
                        // Update the artist name with the proper body text color
                        // albumname.setTextColor(palette.getDarkVibrantColor(Color.parseColor("#cccccc")));
                        //albumname.setTextColor(palette.getLightVibrantColor(Color.parseColor("#cccccc")));
                        //album.setTextColor(palette.getMutedColor(Color.WHITE));
                        // songTitle.setTextColor(palette.getLightVibrantColor(Color.parseColor("#cccccc")));
                        //constraintLayout.setBackground(initTitile(palette.getLightVibrantColor(Color.parseColor("#cccccc")),palette.getDominantColor(Color.parseColor("#cccccc"))));
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void update() {
        setBlurBackdround();
    }
}
