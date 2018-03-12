package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 04-01-2017.
 */

public class albumdetailadapter extends BaseAdapter {
    Context context;
    ArrayList<Songs> songs = new ArrayList<>();
    ArrayList<String> song = new ArrayList<>();
    ArrayList<String> artist = new ArrayList<>();
    SongDetailLoader songloader = new SongDetailLoader();
    public albumdetailadapter(Context context,ArrayList<Songs> songwithalbum) {
        MediaMetadataRetriever data = new MediaMetadataRetriever();
        songs = songwithalbum;
        for(int i=0;i<songs.size() ;i++)
        {
            try {
                song.add(songs.get(i).gettitle());
                artist.add(songs.get(i).getartist());
            }
            catch (Exception e)
            {
                song.add("Unknown title");
                artist.add("Unknown artist");
            }

        }
        this.context  = context;

    }
    @Override
    public int getCount() {
        return songs.size();
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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if(convertView == null )
        convertView = layoutInflater.inflate(R.layout.layou_for_albumdetail,parent,false);
        TextView songname =(TextView) convertView.findViewById(R.id.songname);
        TextView artistname =(TextView) convertView.findViewById(R.id.artistname);
        songname.setText(""+song.get(position));
        artistname.setText(artist.get(position));
        return convertView;
    }
}
