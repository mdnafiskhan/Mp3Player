package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class SongfullistViewAdapter extends BaseAdapter {

    LayoutInflater inflater ;
    Context context;
    ArrayList<Songs> s= new ArrayList<>();
    public SongfullistViewAdapter(Context context ,ArrayList<Songs> s) {
        this.context = context ;
        this.s= s;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return s.size();
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
         if(convertView==null)
              convertView = inflater.inflate(R.layout.songslayot,parent,false);
        ImageView i =(ImageView) convertView.findViewById(R.id.artistimage);
        TextView name=(TextView) convertView.findViewById(R.id.artistname);
        TextView artist=(TextView) convertView.findViewById(R.id.song_n_album);
        Bitmap b =BitmapFactory.decodeFile(s.get(position).getAlbumart());
        if(b!= null)
        i.setImageBitmap(b);
        else
        {
         i.setImageResource(R.drawable.default_track_light);
        }
        name.setText(s.get(position).gettitle());
        artist.setText(s.get(position).getartist());

        return convertView;
    }



}
