package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developmentforfun.mdnafiskhan.mp3player.R;

/**
 * Created by mdnafiskhan on 03-01-2017.
 */

public class customPageadaptee extends PagerAdapter {
    LayoutInflater layoutInflater;
    Context context ;

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view =layoutInflater.inflate(R.layout.listviewofsongs,container,false);
        container.addView(view);
        return view;
    }

    public customPageadaptee(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


}
