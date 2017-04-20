package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaActionSound;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.AlbumDetail;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.MainActivity;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.AlbumAdapter;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.albumdetailadapter;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 04-01-2017.
 */

public class AlbumFragment extends Fragment {
    songDetailloader songDetailloader  = new songDetailloader();
    public AlbumFragment() {
        super();
    }
    GridView gridView;
    AlbumAdapter a;
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
        gridView = (GridView) v.findViewById(R.id.gridview);
        //Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.grid_layout_anim);
        //GridLayoutAnimationController controller = new GridLayoutAnimationController(animation,0.2f,0.2f);
        //gridView.setLayoutAnimation(controller);
            a =new AlbumAdapter(getContext());
          a.setCursor();
        gridView.setAdapter(a);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        songDetailloader.set(getContext());
        Intent i = new Intent( getActivity() , AlbumDetail.class);
        Bundle b= new Bundle();
        b.putCharSequence("album",songDetailloader.album(position));
           i.putExtras(b);
        startActivity(i);
        }
        });
        return v;
        }


        }
