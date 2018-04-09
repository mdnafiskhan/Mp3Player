package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

/**
 * Created by mdnafiskhan on 23/03/2018.
 */

public class ViewPagerCustomAdapter extends PagerAdapter {
    MusicService musicService;
    LayoutInflater layoutInflater;
    Context context;
    public ViewPagerCustomAdapter(Context context)
    {
        musicService = new MusicService();
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v= layoutInflater.inflate(R.layout.image_layout,container,false);
        ImageView imageView = (ImageView) v.findViewById(R.id.songalbumart);
        String img = musicService.getPlaylist().get(position).getAlbumart();
        Glide.with(context).load(img).placeholder(R.mipmap.ic_defalut).into(imageView);
        Log.d("vp","instantiating item");
        Log.d("vp","position = "+position+" setting image with the glide");
        Log.d("vp","container child count "+container.getChildCount());
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // container.removeView((ViewGroup) object);
        Log.d("vp","Destroying the item at position ="+position);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        Log.d("vp","Update finished");
    }

    @Override
    public int getCount() {
      //  Log.d("vp","get Count returns "+musicService.getPlaylist().size());
        return musicService.getPlaylist().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
            Log.d("vp","observer is not null");
        }
        else
        {
            Log.d("vp","observer is null");
        }
    }
}
