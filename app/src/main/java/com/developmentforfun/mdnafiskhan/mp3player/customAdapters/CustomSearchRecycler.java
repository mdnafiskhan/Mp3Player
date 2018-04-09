package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.Artist_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Genre_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Scrolling_Album_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Albums;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Genre;
import com.developmentforfun.mdnafiskhan.mp3player.Models.SearchedContent;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 24/01/2018.
 */

public class CustomSearchRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater layoutInflater;
    SearchedContent searchedContent;
    Context context;
    String arr[]={"Songs","Albums","Artists","Genres"};
    MusicService musicService;
    boolean mBound = false;

    public CustomSearchRecycler(Context context, SearchedContent searchedContent) {
        super();
        this.context  =context;
        layoutInflater = LayoutInflater.from(context);
        this.searchedContent = searchedContent;
        Intent i = new Intent(context,MusicService.class);
        try {
            context.bindService(i, serviceConnection, Context.BIND_NOT_FOREGROUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch(viewType) {
            case 0: v = layoutInflater.inflate(R.layout.trackscontentlayout, parent, false); // for songs
                return new CustomSearchRecycler.ViewHolder0(v);

            case 1:  v = layoutInflater.inflate(R.layout.songslayot, parent, false); // for albums
                return new CustomSearchRecycler.ViewHolder1(v);

            case 2: v = layoutInflater.inflate(R.layout.artistdetaillayout,parent,false); // for artist and genres
                return new CustomSearchRecycler.ViewHolder2(v);

            case 3: v = layoutInflater.inflate(R.layout.textview,parent,false); // title
                return new CustomSearchRecycler.ViewHolder3(v);
        }
        return null;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           Log.d("onBindView","viewType="+holder.getItemViewType()+" position="+position);
           int pos;
            switch (holder.getItemViewType())
            {
                case 0:
                        pos = position-1;
                        ViewHolder0 viewHolder0 = (ViewHolder0)(holder);
                        viewHolder0.songname.setText(searchedContent.getSongses().get(pos).gettitle());
                        viewHolder0.albumname.setText(searchedContent.getSongses().get(pos).getalbum());
                        viewHolder0.duration.setText(time(searchedContent.getSongses().get(pos).getDuration()));
                        break;

                case 1:
                        pos = position-searchedContent.getValPairList().get(1).second-1;
                        ViewHolder1 viewHolder1 = (ViewHolder1)(holder);
                        viewHolder1.artist.setText(searchedContent.getAlbumses().get(pos).getArtistName());
                        viewHolder1.album.setText(searchedContent.getAlbumses().get(pos).getAlbumName());
                        new setImage(viewHolder1.imageView,searchedContent.getAlbumses().get(pos).getAlbumArt()).execute();
                        break;

                case 2:
                        ViewHolder2 viewHolder2 = (ViewHolder2)(holder);
                        if(searchedContent.getValPairList().get(2).second!=-1 && searchedContent.getValPairList().get(3).second!=-1)
                        {
                            if(position>searchedContent.getValPairList().get(2).second && position<searchedContent.getValPairList().get(3).second)
                            {
                                Log.d("here with","position="+position+" and viewType=2");
                                pos = position - searchedContent.getValPairList().get(2).second-1;
                                Log.d("pos=",pos+" and Name="+searchedContent.getArtistses().get(pos).getArtistname());
                                viewHolder2.artistName.setText(searchedContent.getArtistses().get(pos).getArtistname());
                            }
                            else
                            {
                                pos = position - searchedContent.getValPairList().get(3).second-1;
                                viewHolder2.artistName.setText(searchedContent.getGenres().get(pos).getNameGenre());
                            }
                        }
                        else if(searchedContent.getValPairList().get(2).second!=-1 && searchedContent.getValPairList().get(3).second==-1)
                        {
                            if(position>searchedContent.getValPairList().get(2).second) {
                                Log.d("postion",""+position);
                                Log.d("artist pos",""+searchedContent.getValPairList().get(2).second);
                                pos = position - searchedContent.getValPairList().get(2).second-1;
                                viewHolder2.artistName.setText(searchedContent.getArtistses().get(pos).getArtistname());
                            }
                        }
                        else if(searchedContent.getValPairList().get(2).second==-1 && searchedContent.getValPairList().get(3).second!=-1)
                        {
                            if(position>searchedContent.getValPairList().get(3).second) {
                                pos = position - searchedContent.getValPairList().get(3).second-1;
                                viewHolder2.artistName.setText(searchedContent.getGenres().get(pos).getNameGenre());
                            }
                        }

                        break;

                case 3:
                    ViewHolder3 viewHolder3 = (ViewHolder3)(holder);
                    if(position==searchedContent.getValPairList().get(0).second)
                    {
                        viewHolder3.title.setText("Songs");
                    }
                    if(position==searchedContent.getValPairList().get(1).second)
                    {
                        viewHolder3.title.setText("Albums");
                    }
                    if(position==searchedContent.getValPairList().get(2).second)
                    {
                        viewHolder3.title.setText("Artists");
                    }
                    if(position==searchedContent.getValPairList().get(3).second)
                    {
                        viewHolder3.title.setText("Genres");
                    }

                    break;
            }
    }

    @Override
    public int getItemCount() {
       Log.d("size",""+searchedContent.getSize());
       return searchedContent.getSize();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        int s = searchedContent.getValPairList().size();
        for(int i=0;i<s;i++)
        {
            if(searchedContent.getValPairList().get(i).second==position)
            {
                return 3;
            }
        }
        int i;
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<String> tag = new ArrayList<>();
        for(i=0;i<s;i++)
        {
            if(searchedContent.getValPairList().get(i).second!=-1 && searchedContent.getValPairList().get(i).first!=null)
            {
                pos.add(searchedContent.getValPairList().get(i).second);
                tag.add(searchedContent.getValPairList().get(i).first);
            }
        }
        for(i=0;i<pos.size()-1;i++)
        {
            if(position>pos.get(i) && position<pos.get(i+1))
            {
                if (tag.get(i).equals("Songs"))
                    return 0;
                else if (tag.get(i).equals("Albums"))
                    return 1;
                else if (tag.get(i).equals("Artists"))
                    return 2;
            }
        }
        if(position>pos.get(pos.size()-1))
        {
            if (tag.get(pos.size()-1).equals("Songs"))
                return 0;
            else if (tag.get(pos.size()-1).equals("Albums"))
                return 1;
            else
                return 2;
        }
            return 3;


    }

    public class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView songname;
        TextView albumname;
        TextView duration ;
        ConstraintLayout constraintLayout;
        public ViewHolder0(View itemView) {
            super(itemView);
            songname = (TextView) itemView.findViewById(R.id.songname);
            albumname = (TextView) itemView.findViewById(R.id.albumname);
            duration = (TextView) itemView.findViewById(R.id.durr);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.rl);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mBound) {
                        musicService.setplaylist(searchedContent.getSongses(), getAdapterPosition() - 1);
                        musicService.setMediaPlayer();
                    }
                }
            });
        }
    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView album;
        TextView artist;
        ConstraintLayout constraintLayout;
        public ViewHolder1(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.artistimage);
            album = (TextView) itemView.findViewById(R.id.artistname);
            artist = (TextView) itemView.findViewById(R.id.song_n_album);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayoutx);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition()-searchedContent.getValPairList().get(1).second-1;
                    Intent i = new Intent( context , Scrolling_Album_Activity.class);
                    Bundle b= new Bundle();
                    b.putCharSequence("albumId",searchedContent.getAlbumses().get(pos).getAlbumId());
                    b.putCharSequence("albumName",searchedContent.getAlbumses().get(pos).getAlbumName()+"");
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView artistName;
        TextView noOfSOngs;
        ConstraintLayout constraintLayout;
        public ViewHolder2(View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cl) ;
            artistName = (TextView) itemView.findViewById(R.id.artistName);
            noOfSOngs = (TextView) itemView.findViewById(R.id.nofosongs);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int pos;
                    if(searchedContent.getValPairList().get(2).second!=-1 && searchedContent.getValPairList().get(3).second!=-1)
                    {
                        if(position>searchedContent.getValPairList().get(2).second && position<searchedContent.getValPairList().get(3).second)
                        {
                            pos = position - searchedContent.getValPairList().get(2).second-1;
                            Intent i=new Intent(context,Artist_Activity.class);
                            i.putExtra("artist",searchedContent.getArtistses().get(pos).getArtistname());
                            unbindService();
                            context.startActivity(i);

                        }
                        else
                        {
                            pos = position - searchedContent.getValPairList().get(3).second-1;
                            Intent i=new Intent(context, Genre_Activity.class);
                            i.putExtra("genreName",searchedContent.getGenres().get(pos).getNameGenre());
                            i.putExtra("genreId",searchedContent.getGenres().get(pos).getId());
                            unbindService();
                            context.startActivity(i);
                        }
                    }
                    else if(searchedContent.getValPairList().get(2).second!=-1 && searchedContent.getValPairList().get(3).second==-1)
                    {
                        if(position>searchedContent.getValPairList().get(2).second) {
                            pos = position - searchedContent.getValPairList().get(2).second-1;
                            Intent i=new Intent(context,Artist_Activity.class);
                            i.putExtra("artist",searchedContent.getArtistses().get(pos).getArtistname());
                            unbindService();
                            context.startActivity(i);

                        }
                    }
                    else if(searchedContent.getValPairList().get(2).second==-1 && searchedContent.getValPairList().get(3).second!=-1)
                    {
                        if(position>searchedContent.getValPairList().get(3).second) {
                            pos = position - searchedContent.getValPairList().get(3).second-1;
                            Intent i=new Intent(context, Genre_Activity.class);
                            i.putExtra("genreName",searchedContent.getGenres().get(pos).getNameGenre());
                            i.putExtra("genreId",searchedContent.getGenres().get(pos).getId());
                            unbindService();
                            context.startActivity(i);
                        }
                    }

                }
            });
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder {
        TextView title;
        public ViewHolder3(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title2);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService  =  binder.getService();
            mBound =true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound =false;
        }
    };



    private String time(long a)
    {
        String s;
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
    public void unbindService()
    {
        try {
            if (mBound)
                context.unbindService(serviceConnection);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public class setImage extends AsyncTask<Void,Void,Void>{
        ImageView imageView;
        String image;
        Bitmap bitmap;
        public setImage(ImageView imageView,String image) {
            super();
            this.imageView = imageView;
            this.image = image;
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
            {
                imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bitmap = BitmapFactory.decodeFile(image);
            return null;
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
//            unbindService();
    }
}
