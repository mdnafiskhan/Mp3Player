package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.Playlist;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.UpdatePlaylistInterface;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.PlaylistProvider;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 28/06/2017.
 */

public class PlaylistRecyclerAdap extends RecyclerView.Adapter<PlaylistRecyclerAdap.ViewHolder>{
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist> playlist  = new ArrayList<>();
    AlertDialog dis;
    private UpdatePlaylistInterface updatePlaylistInterface;
    public PlaylistRecyclerAdap(Context context, ArrayList<com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist> playlist,UpdatePlaylistInterface updatePlaylistInterface) {
        super();
        Log.d("in constructor","true");
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.playlist = playlist;
        this.updatePlaylistInterface = updatePlaylistInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("in recycler view","true");
        View v = layoutInflater.inflate(R.layout.style,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("binding view","true");
        if(playlist.size()!=position) {
            holder.textView.setText(playlist.get(position).getName());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, Playlist.class);
                    Bundle b = new Bundle();
                    b.putString("playlistName", playlist.get(position).getName());
                    b.putString("playlistId", playlist.get(position).getId());
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
            holder.optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(context, v);
                    popup.getMenuInflater().inflate(R.menu.playlist_option_menu, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.rename:

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(context,android.R.style.Theme_Holo_Dialog_NoActionBar);
                                    View v =LayoutInflater.from(context).inflate(R.layout.playlist_rename,null);
                                    builder.setView(v);
                                    builder.setTitle("Rename Playlist");
                                    builder.setCancelable(true);
                                    builder.create();
                                    final AlertDialog d=builder.show();
                                    final EditText newName = v.findViewById(R.id.newName);
                                    Button save = v.findViewById(R.id.Save);
                                    save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(newName.getText().toString().trim().isEmpty())
                                            {
                                                Toast.makeText(context,"Enter playlist name",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                new RenamePlaylist(newName.getText().toString().trim(),Long.parseLong(playlist.get(holder.getAdapterPosition()).getId()),1).execute();
                                                d.dismiss();
                                            }
                                        }
                                    });
                                    break;
                                case R.id.delete:

                                    new RenamePlaylist(playlist.get(holder.getAdapterPosition()).getName(),Long.parseLong(playlist.get(holder.getAdapterPosition()).getId()),0).execute();
                                    break;
                            }

                            return true;
                        }
                    });


                }
            });
        }
        else
        {
            holder.optionButton.setVisibility(View.GONE);
            holder.textView.setTextColor(Color.parseColor("#47cde8"));
            holder.textView.setText("ADD PLAYLIST");
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);
                    final View view2 = layoutInflater.inflate(R.layout.playlistdialog2,null);
                    final DataBaseClass db = new DataBaseClass(context);
                    builder.setTitle("Create new Playlist");
                    builder.setView(view2);
                    ListView l = (ListView) view.findViewById(R.id.listView);
                    final EditText e = (EditText) view2.findViewById(R.id.edittext);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String s = e.getText().toString().trim();
                            if(s.isEmpty())
                            {
                                Toast.makeText(context,"Enter playlist name",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                PlaylistProvider.createPlaylist(context.getContentResolver(),s);
                                updatePlaylistInterface.reloadPlayList();
                            }
                            dialog.dismiss();

                        }
                    });
                    builder.create();
                    dis = builder.show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {

        Log.d("size of views",playlist.size()+"");
             return playlist.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView optionButton;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            optionButton = (ImageView) itemView.findViewById(R.id.optionButton2);
        }
    }

    public class RenamePlaylist extends AsyncTask<Void,Void,Void>
    {
        String PlaylistName;
        Long PlaylistId;
        int what;
        public RenamePlaylist(String PlaylistName,Long PlaylistId,int what) {
            super();
            this.PlaylistName = PlaylistName;
            this.PlaylistId = PlaylistId;
            this.what = what;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updatePlaylistInterface.reloadPlayList();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(what==1) {
                PlaylistProvider.renamePlaylist(context.getContentResolver(), PlaylistId, PlaylistName);
            }
            if(what==0)
            {
                PlaylistProvider.deletePlaylist(context.getContentResolver(), PlaylistId);
            }
            return null;
        }
    }


}