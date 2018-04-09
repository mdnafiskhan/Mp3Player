package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.Artist_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Fragment_Container_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Scrolling_Album_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.ChosenSongs;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.SelectorChangeInterface;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.SetItemSelect;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by mdnafiskhan on 26/06/2017.
 */

public class ChooserRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public static ArrayList<Songs> songs = new ArrayList<>();
    public static ArrayList<Songs> Chosensongs = new ArrayList<>();
    public ArrayList<String> flags = new ArrayList<>();
    static LayoutInflater layoutInflater;
    MusicService musicService;
    boolean mBound = false;
    AlertDialog dis;
    public static ArrayList<Songs> shufflePlayList = new ArrayList<>();
    public static int currentPos=-1;
    Mp3PlayerApplication mp3PlayerApplication;
    ChosenSongs chosenSongs;
    public ChooserRecyclerViewAdapter() {
        super();
    }

    public ChooserRecyclerViewAdapter(Context context , final ArrayList<Songs> songs,ChosenSongs chosenSongs) {
        super();
        this.context = context;
        this.chosenSongs = chosenSongs;
        this.songs = songs;
        layoutInflater = LayoutInflater.from(context);
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
            case 0: v = layoutInflater.inflate(R.layout.suffle_layout, parent, false);
                return new ViewHolder0(v);

            case 1:  v = layoutInflater.inflate(R.layout.trackscontentlayout, parent, false);
                return new ViewHolder(v);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0 && songs.size()!=0)
            return 0;
        else
            return 1;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("sizze od list",""+songs.size());
        if(holder.getItemViewType()>0) {
            ViewHolder holder2 = (ViewHolder) holder;
            Log.d("sizze od list",""+songs.size());
            Log.d("position",""+position);
            if (position < songs.size() + 1 && songs.size() != 0) {
                holder2.songname.setText(songs.get(position - 1).gettitle());
                holder2.albumname.setText(songs.get(position - 1).getalbum());
                holder2.duration.setText(time(songs.get(position - 1).getDuration()));
                if(flags.size() > 0) {
                    if (flags.contains(songs.get(position - 1).getSongId()) ) {
                        holder2.constraintLayout.setBackgroundColor(Color.parseColor("#82014a"));
                        //holder2.songname.setTextColor(Color.parseColor("#000000"));
                        // holder2.albumname.setTextColor(Color.parseColor("#999999"));
                        //  holder2.duration.setTextColor(Color.parseColor("#999999"));
                    } else {
                        holder2.constraintLayout.setBackgroundColor(Color.parseColor("#000000"));
                        //  holder2.songname.setTextColor(Color.parseColor("#cccccc"));
                        //   holder2.albumname.setTextColor(Color.parseColor("#aaaaaa"));
                        //   holder2.duration.setTextColor(Color.parseColor("#aaaaaa"));
                    }
                }
              else
                {
                    holder2.constraintLayout.setBackgroundColor(Color.parseColor("#000000"));
                }
            } else {
                holder2.songname.setText("No song available");
                holder2.albumname.setText("");
                holder2.duration.setText("");
            }

        }

    }

    @Override
    public int getItemCount() {
        if(songs.size()==0) {
            return 1;
        }
        else
            return songs.size() + 1;

    }

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

    public void remove(int pos)
    {
        notifyItemRemoved(pos);
    }



    public class ViewHolder0 extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        ImageButton optionButton;
        public ViewHolder0(View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.shuffleCons);
            optionButton = (ImageButton) itemView.findViewById(R.id.optionButton);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shufflePlayList = songs;
                    shuffleList(shufflePlayList);
                    musicService.setplaylist(shufflePlayList,0);
                    musicService.setMediaPlayer();

                }
            });

            optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(context, v);
                    popup.getMenuInflater().inflate(R.menu.clipboard_popup, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.refresh:
                                    break;
                                case R.id.sortaz:
                                    break;
                                case R.id.sortza:
                                    break;
                                case R.id.sortalbumaz:
                                    break;
                                case R.id.sortalbumza:
                                    break;
                                case R.id.sortartistaz:
                                    break;
                                case R.id.sortartistza:
                                    break;
                                case R.id.sortdataadded:
                                    break;
                                case R.id.sortduration:
                                    break;
                                default:
                                    break;
                            }

                            return true;
                        }
                    });


                }
            });
        }


    }

    public static void shuffleList(ArrayList<Songs> songses) {
        int n = songses.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(songses, i, change);
        }
    }

    private static void swap(ArrayList<Songs> a, int i, int change) {
        Songs helper = a.get(i);
        a.set(i, a.get(change));
        a.set(change, helper);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songname;
        TextView albumname;
        TextView duration;
        ConstraintLayout constraintLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            songname = (TextView) itemView.findViewById(R.id.songname);
            albumname = (TextView) itemView.findViewById(R.id.albumname);
            duration = (TextView) itemView.findViewById(R.id.durr);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.rl);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flags.contains(songs.get(getAdapterPosition()-1).getSongId())) {
                        flags.remove(songs.get(getAdapterPosition() - 1).getSongId());
                        Chosensongs.remove(songs.get(getAdapterPosition()-1));
                    }
                    else {
                        flags.add(songs.get(getAdapterPosition()-1).getSongId());
                        Chosensongs.add(songs.get(getAdapterPosition()-1));
                    }
                    notifyItemChanged(getAdapterPosition());
                    chosenSongs.getChosenSongs(Chosensongs);
                }
            });

        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService  =  binder.getService();
            mBound =true;
            Log.d("msg","service is connected 0");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound =false;
        }
    };

    public void unbindService()
    {
        context.unbindService(serviceConnection);
    }




}
