package com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityViewModel.MainActivityViewModel;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.albumdetailadapter;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mdnafiskhan on 16/03/2018.
 */

public class AlbumDetailFragment extends Fragment {

    ArrayList<Songs> give = new ArrayList<>();
    ArrayList<Songs> shufflePlayList = new ArrayList<>();
    MediaPlayer mp = new MediaPlayer();
    MusicService musicService;
    boolean mBound;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String album;
    String albumName;
    public static AlbumDetailFragment newInstance(){
        return new AlbumDetailFragment();
    }

    public AlbumDetailFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b= getArguments();
        Log.d("msg",""+b.toString());
        if(b!=null)
        {
            album =(String) b.getCharSequence("albumId","ALBUM");
            albumName =(String) b.getCharSequence("albumName","ALBUM_NAME");
        }
        // final int pos = loader.getpositionofalbumwithalbum(album);
        Log.d("recived","Album name from d "+album);

    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService  =  binder.getService();
            mBound =true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound =false;
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_scrolling__album_,container,false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.toolbar_layout);
        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shufflePlayList = give;
                shuffleList(shufflePlayList);
                musicService.setplaylist(shufflePlayList,0);
                musicService.setMediaPlayer();
            }
        });
        SongDetailLoader loader = new SongDetailLoader(getContext());
        // final int pos = loader.getpositionofalbumwithalbum(album);
        Log.d("recived","Album name forn d "+album);
        String image = loader.albumartwithalbum(albumName);
        ImageView albumimage =  (ImageView) v.findViewById(R.id.albumart);
        final TextView albumname = (TextView) v.findViewById(R.id.albumname);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        give = loader.getSongs("album",album,getContext());
        albumdetailadapter albumdetailadapter = new albumdetailadapter(getContext(),give);
        listView.setAdapter(albumdetailadapter);
        Intent i = new Intent(getContext(),MusicService.class);
        getActivity().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        albumname.setText(albumName);
        Bitmap bitmap = BitmapFactory.decodeFile(image);
        if(image!=null)
            albumimage.setImageBitmap(bitmap);
        else
        {
            albumimage.setImageResource(R.drawable.default_album_small_light);
        }
        try {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch == null)
                        swatch = palette.getMutedSwatch(); // Sometimes vibrant swatch is not available
                    if (swatch != null) {
                        // Set the background color of the player bar based on the swatch color
                        // Update the track's title with the proper title text color
                        // Update the artist name with the proper body text color
                        // albumname.setTextColor(palette.getDarkVibrantColor(Color.parseColor("#cccccc")));
                        //albumname.setTextColor(palette.getLightVibrantColor(Color.parseColor("#cccccc")));
                        fab.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkVibrantColor(Color.parseColor("#000000"))));
                        //album.setTextColor(palette.getMutedColor(Color.WHITE));
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Log.d("album activity","give size"+give.size()+" position "+position);
                    musicService.setplaylist(give,position);
                    Log.d("msg","song albumart here in detail page = "+give.get(position).getAlbumart());
                    musicService.setMediaPlayer();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("","error occured");
                }


            }
        });
        return v;
    }

    public static void shuffleList(ArrayList<Songs> songses) {
        int n = songses.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(songses, i, change);
        }
    }

    private static void swap(ArrayList<Songs> a, int i, int change) {
        Songs helper = a.get(i);
        a.set(i, a.get(change));
        a.set(change, helper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unbindService(serviceConnection);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
