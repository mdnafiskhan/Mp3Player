package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.R;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 11-01-2017.
 */

public class NavigationDrawerAdapter extends BaseAdapter {
    Context context ;
    String [] s={"NAVIGATION","Now Playing","Library","PlaylistProvider","Equalizer","Settings"};
    LayoutInflater inflator ;
    public NavigationDrawerAdapter(Context context) {
        this.context = context ;
        inflator = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return s.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0 || position == 6 || position==12) {
            convertView = inflator.inflate(R.layout.imageview, parent, false);
            TextView t =(TextView) convertView.findViewById(R.id.navigation) ;
            t.setText(s[position]);
        }
        else
        {
            convertView = inflator.inflate(R.layout.style,parent,false);
            TextView t= (TextView) convertView.findViewById(R.id.textView);
            ImageView imageView = convertView.findViewById(R.id.optionButton2);
            imageView.setVisibility(View.GONE);
            t.setText(s[position]);
        }

        return convertView;
    }
}
