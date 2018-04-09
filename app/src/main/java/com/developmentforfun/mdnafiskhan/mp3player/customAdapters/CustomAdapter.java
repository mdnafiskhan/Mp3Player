package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Fragments.FragmentClass;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

/**
 * Created by mdnafiskhan on 06/01/2018.
 */

public class CustomAdapter extends FragmentStatePagerAdapter {
    MusicService musicService;
    public CustomAdapter(FragmentManager fm) {
        super(fm);
        musicService = new MusicService();
        Log.d("onCreate","of custom adtaper");
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("item","geting item");
        Log.d("Pos of .... fragments"," "+position);
        return new FragmentClass(position);
    }

    @Override
    public int getCount() {
        Log.d("here......##","");
        Log.d("playlistSize",musicService.getPlaylist().size()+"");
        return musicService.getPlaylist().size();

    }


}

