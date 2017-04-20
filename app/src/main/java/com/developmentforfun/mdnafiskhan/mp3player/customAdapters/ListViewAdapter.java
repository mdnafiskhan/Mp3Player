package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 03-01-2017.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater ;
    ArrayList<Songs> songlist = new ArrayList<>();
    ArrayList<Songs> newsonglist = new ArrayList<>();
    private String s;
    Cursor cursor;
    Bitmap bitmap ;
    private songDetailloader songloader = new songDetailloader();

    private Songs song = new Songs();


    private static class viewHolder {
        TextView songname;
        TextView albumname;
        TextView duration ;
        ImageView albumart ;

    }

    public ListViewAdapter(final Context context, ArrayList<Songs> list  ) {
        this.songlist = list ;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        }

    @Override
    public int getCount() {
        return songlist.size();
    }

    @Override
    public Object getItem(int position) {
        return songlist.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
       // Toast.makeText(context,"Data setChanged",Toast.LENGTH_SHORT).show();
    }
    public void setCursor()
    {
        this.cursor= songloader.getAlbumCursor(context);
        cursor.moveToFirst();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder v;
       if(convertView == null )
        {
            v= new viewHolder();
            convertView = layoutInflater.inflate(R.layout.trackscontentlayout,parent,false);
            v.songname = (TextView) convertView.findViewById(R.id.songname);
            v.albumname = (TextView) convertView.findViewById(R.id.albumname);
            v.duration = (TextView) convertView.findViewById(R.id.durr);
            v.albumart = (ImageView) convertView.findViewById(R.id.albumart);
            v.albumart.setImageResource(R.drawable.default_track_light);
         convertView.setTag(v);
        }else
       {
           v = (viewHolder) convertView.getTag();
       }

           v.songname.setText(songlist.get(position).gettitle());
           v.albumname.setText(songlist.get(position).getalbum());
           long a= songlist.get(position).getDuration();
           v.duration.setText(time(a));
        //   v.album_gridview.setImageBitmap(BitmapFactory.decodeFile(songlist.get(position).getAlbumart()));
   //     SharedPreferences sharedPreferences = context.getSharedPreferences("select",Context.MODE_PRIVATE);
     //   String s = sharedPreferences.getString("name","");
        if(v.albumart!=null)
        v.albumart.setImageResource(R.drawable.default_track_light);
        new getalbumart(v.albumart,position).execute();
        return convertView;
    }
    private String time(long a)
    {
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

    public class getalbumart extends AsyncTask<Void,Void,Void>{
        Bitmap bitmap;
        ImageView imageView ;
        int p;
        public getalbumart(ImageView v,int position)
        {
            this.imageView = v;
            this.p = position ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageView.setImageResource(R.drawable.default_track_light);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
        else {
            imageView.setImageResource(R.drawable.default_track_light);
        }

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize= 4;
                bitmap = BitmapFactory.decodeFile(songlist.get(p).getAlbumart(),options);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                bitmap = null ;
            }
        return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            imageView.setImageResource(R.drawable.default_track_light);

        }
    }

        }
