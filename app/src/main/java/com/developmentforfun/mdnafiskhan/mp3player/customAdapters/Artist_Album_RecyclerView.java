package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.NavigationControler;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Fragment_Container_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Scrolling_Album_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Albums;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;

import java.util.ArrayList;

import static com.developmentforfun.mdnafiskhan.mp3player.R.id.imageView;

/**
 * Created by mdnafiskhan on 16/01/2018.
 */

public class Artist_Album_RecyclerView extends RecyclerView.Adapter<Artist_Album_RecyclerView.ViewHolder> {

    ArrayList<Albums> albumses = new ArrayList<>();
    LayoutInflater layoutInflater;
    SongDetailLoader songDetailLoader ;
    Context context;
    public Artist_Album_RecyclerView(Context context,ArrayList<Albums> albumses) {
        super();
        this.context=context;
        this.albumses = albumses;
        this.layoutInflater = LayoutInflater.from(context);
        this.songDetailLoader = new SongDetailLoader(context);
    }

    @Override
    public Artist_Album_RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.album_gridview,parent,false);
        return new Artist_Album_RecyclerView.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Artist_Album_RecyclerView.ViewHolder holder, final int position) {
        new Artist_Album_RecyclerView.LoadAlbum(holder.albumImage,holder.albumname,holder.albumartist,context,position).execute();
        int x = holder.albumImage.getWidth();
        holder.albumImage.setMaxHeight(x);
        holder.albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b= new Bundle();
                b.putString("albumId",albumses.get(holder.getAdapterPosition()).getAlbumId());
                b.putString("albumName",albumses.get(holder.getAdapterPosition()).getAlbumName());
                b.putString("which","AlbumDetail");
                Intent i = new Intent(context, Fragment_Container_Activity.class);
                i.putExtras(b);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("size of view",albumses.size()+"");
        return albumses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage ;
        TextView albumname ;
        TextView albumartist ;
        public ViewHolder(View itemView) {
            super(itemView);
            albumImage = (ImageView) itemView.findViewById(imageView);
            albumname = (TextView) itemView.findViewById(R.id.albumname);
            albumartist = (TextView) itemView.findViewById(R.id.artist);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            albumImage.setMaxHeight(width/2);
        }
    }

    public class LoadAlbum extends AsyncTask<Void,Void,Void>
    {
        ImageView albumImage;
        TextView albumname;
        TextView artist;
        Context context;
        int position;
        Bitmap b;
        String s;
        String artistName;
        ArrayList<Songs> song = new ArrayList<>();
        public LoadAlbum(ImageView v,TextView t,TextView artist,Context context,int position) {
            super();
            this.context = context;
            albumImage = v;
            albumname  = t;
            this.artist  = artist;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumImage.setImageResource(R.drawable.default_album_small_light);
        }

        @Override
        protected Void doInBackground(Void... params) {
            s=albumses.get(position).getAlbumName();
            artistName=albumses.get(position).getArtistName();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize= 2;
            b=BitmapFactory.decodeFile(albumses.get(position).getAlbumArt(),options);
            if(b==null)
            {
                try {
                    song = songDetailLoader.getSongs("album", s, context);
                    Uri uri = song.get(0).getSonguri();
                    MediaMetadataRetriever data = new MediaMetadataRetriever();
                    data.setDataSource(context, uri);
                    byte[] bytes = data.getEmbeddedPicture();
                    b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(b!=null)
                albumImage.setImageBitmap(b);
            else
                albumImage.setImageResource(R.drawable.default_album_small_light);
            albumname.setText(s);
            artist.setText(artistName+"");
        }
    }
}
