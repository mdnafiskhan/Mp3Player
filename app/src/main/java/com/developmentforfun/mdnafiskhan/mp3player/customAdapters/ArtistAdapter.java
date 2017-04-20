package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 16-01-2017.
 */

public class ArtistAdapter extends BaseAdapter {

    ArrayList<Artists> artistses = new ArrayList<>();
    Context context ;
    LayoutInflater layoutInflater ;
    static TextView artistname ;
    static TextView nofosongsnalbum ;
    static ImageView artistimage ;
    String song;
    String album;
    public ArtistAdapter(Context context , ArrayList<Artists> artistses) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.artistses = artistses;
    }

    @Override
    public int getCount() {
        return artistses.size();
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
           convertView= layoutInflater.inflate(R.layout.artistdetaillayout,parent,false);
            artistname = (TextView) convertView.findViewById(R.id.artistname);
            artistimage = (ImageView) convertView.findViewById(R.id.artistimage);
            nofosongsnalbum = (TextView) convertView.findViewById(R.id.song_n_album);
            artistname.setText(artistses.get(position).getArtistname());
            if(artistses.get(position).getNofosongs()>1)
                  song = "songs";
            else
                 song = "song";
        if(artistses.get(position).getNoalbums()>1)
            album = "Albums";
        else
            album = "Album";
            nofosongsnalbum.setText(""+artistses.get(position).getNofosongs()+" "+song+" in "+artistses.get(position).getNoalbums()+" "+album);
       //  new setartist(artistimage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return convertView ;
    }


    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public class setartist extends AsyncTask<Void,Void,Void>
    {

        ImageView im;
        Bitmap bitmap;
        public setartist(ImageView imageView) {
            super();
            this.im =imageView ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
             bitmap = getBitmapFromURL("http://www.whatsupguys.in/wp-content/uploads/2016/01/Arijit-Singh.jpg");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            im.setImageBitmap(bitmap);
        }
    }

}
