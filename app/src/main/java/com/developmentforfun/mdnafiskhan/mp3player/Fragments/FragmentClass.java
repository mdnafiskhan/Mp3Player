package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.developmentforfun.mdnafiskhan.mp3player.CustomView.CircleImage;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mdnafiskhan on 06/01/2018.
 */

public class FragmentClass extends Fragment {

    public ImageView imageView;
    int position;
    MusicService musicService;
    Songs songs = new Songs();
    public FragmentClass()
    {
        super();
    }
    public FragmentClass(int pos) {
        super();
        position =  pos;
        musicService = new MusicService();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("Creating view","done....");
        View v= inflater.inflate(R.layout.fragment_layout_of_albumart,container,false);
        imageView = (ImageView) v.findViewById(R.id.image);
         try {
             String img = musicService.getPlaylist().get(position).getAlbumart();
             Glide.with(this).load(img).placeholder(R.mipmap.ic_defalut).into(imageView);
             Log.d("msg","setting image with the glide");
         }
         catch (Exception e)
         {
             e.printStackTrace();
             imageView.setImageResource(R.mipmap.ic_defalut);
             Log.d("msg","setting default image");

         }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Destroy","true");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Detach","true");
    }
}
