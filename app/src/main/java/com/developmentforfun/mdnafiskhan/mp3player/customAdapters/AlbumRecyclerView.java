package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.NavigationControler;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Fragment_Container_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Albums;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;

import java.util.ArrayList;

import static com.developmentforfun.mdnafiskhan.mp3player.R.id.imageView;

/**
 * Created by mdnafiskhan on 27/06/2017.
 */

public class AlbumRecyclerView extends RecyclerView.Adapter<AlbumRecyclerView.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    RelativeLayout relativeLayout;
    int width;
    int height;
    ArrayList<Albums> albumses = new ArrayList<>();
    public AlbumRecyclerView(Context context,int width,int height,ArrayList<Albums> albums) {
        super();
        this.context = context;
        this.width=width;
        this.height=height;
        this.albumses = albums;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.album_gridview,null);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout3);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
          new LoadAlbum(holder.albumImage,holder.albumname,holder.albumartist,context,albumses.get(position),holder.cardView).execute();
          int x = holder.albumImage.getWidth();
          holder.albumImage.setMaxHeight(x);

    }

    @Override
    public int getItemCount() {
        return albumses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage ;
        TextView albumname ;
        TextView albumartist ;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            albumImage = (ImageView) itemView.findViewById(imageView);
            albumname = (TextView) itemView.findViewById(R.id.albumname);
            albumartist = (TextView) itemView.findViewById(R.id.artist);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            albumImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b= new Bundle();
                    b.putString("albumId",albumses.get(getAdapterPosition()).getAlbumId());
                    b.putString("albumName",albumses.get(getAdapterPosition()).getAlbumName());
                    b.putString("which","AlbumDetail");
                    Intent i = new Intent(context, Fragment_Container_Activity.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }

    public class LoadAlbum extends AsyncTask<Void,Void,Void>
    {
        ImageView albumImage;
        TextView albumname;
        TextView artist;
        Context context;
        int position;
        CardView cardView;
        Albums albums = new Albums();
        Bitmap b;
        String s;
        String albumName;
        String artistName;
        public LoadAlbum(ImageView v,TextView t,TextView artist,Context context,Albums albums,CardView cardView) {
            super();
            this.context = context;
            albumImage = v;
            albumname  = t;
            this.albums = albums;
            this.artist  = artist;
            this.cardView=cardView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumImage.setImageResource(R.drawable.default_album_small_light);
            cardView.setBackgroundColor(Color.parseColor("#666666"));
        }

        @Override
        protected Void doInBackground(Void... params) {
            s=albums.getAlbumId();
            albumName=albums.getAlbumName();
            artistName=albums.getArtistName();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize= 2;
            String art =albums.getAlbumArt();
            b=BitmapFactory.decodeFile(art,options);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(b!=null) {
                albumImage.setImageBitmap(b);
               /* Palette.from(b).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getDominantSwatch();
                        if (swatch == null) swatch = palette.getMutedSwatch(); // Sometimes vibrant swatch is not available
                        if (swatch != null) {
                            // Set the background color of the player bar based on the swatch color
                            // Update the track's title with the proper title text color
                            // Update the artist name with the proper body text color
                             cardView.setBackgroundColor(palette.getDarkVibrantColor(Color.parseColor("#666666")));
                        }
                    }
                });*/
            }
            else
                albumImage.setImageResource(R.drawable.default_album_small_light);
            albumname.setText(albumName);

            artist.setText(artistName+"");
        }
    }
}
