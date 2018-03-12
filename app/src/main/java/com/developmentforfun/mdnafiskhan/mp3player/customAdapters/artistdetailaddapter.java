package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mdnafiskhan on 05-02-2017.
 */

public class artistdetailaddapter extends BaseAdapter {

    ArrayList<Songs> give = new ArrayList<>();
    LayoutInflater layoutInflater;
    TextView Songname;
    TextView albumname;
    TextView duration ;
    Context context;

    public artistdetailaddapter(Context context , ArrayList<Songs> give ) {
        super();
       this.give = give ;
        this.context = context;
        this.layoutInflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.give.size() ;
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
        if(convertView == null)
        convertView = layoutInflater.inflate(R.layout.trackscontentlayout,parent,false);
        Songname = (TextView) convertView.findViewById(R.id.songname);
        albumname = (TextView) convertView.findViewById(R.id.albumname);
        duration = (TextView) convertView.findViewById(R.id.durr);
        Songname.setText(give.get(position).gettitle());
        albumname.setText(give.get(position).getalbum());
        duration.setText(time(give.get(position).getDuration()));
        return convertView;
    }

    private String time(long a)
    { String s;
        s="";
        long sec = a/1000;
        long min =sec/60;
        sec = sec - (min*60) ;
        if(sec<=9)
        {
            s= ""+min+":0"+sec;
        }
        else
            s= ""+min+":"+sec;
        return s;
    }

}
