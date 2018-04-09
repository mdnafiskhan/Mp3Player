package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.Fragment_Container_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.SwipeDeleteInterface;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.PlaylistProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mdnafiskhan on 26/06/2017.
 */

public class CustomSwipeDeleteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public static ArrayList<Songs> songs = new ArrayList<>();
    static LayoutInflater layoutInflater;
    MusicService musicService;
    boolean mBound = false;
    AlertDialog dis;
    public static ArrayList<Songs> shufflePlayList = new ArrayList<>();
    public static int currentPos=-1;
    Mp3PlayerApplication mp3PlayerApplication;
    SwipeDeleteInterface swipeDeleteInterface;

    public CustomSwipeDeleteRecyclerViewAdapter() {
        super();
    }

    public CustomSwipeDeleteRecyclerViewAdapter(Context context , ArrayList<Songs> songs,SwipeDeleteInterface swipeDeleteInterface) {
        super();
        this.context = context;
        this.songs = songs;
        layoutInflater = LayoutInflater.from(context);
        this.swipeDeleteInterface  = swipeDeleteInterface;
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

            case 1:  v = layoutInflater.inflate(R.layout.swipe_delete_song_layout, parent, false);
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
        TextView duration ;
        ConstraintLayout constraintLayout;
        RelativeLayout bottomWrapper;




        public ViewHolder(View itemView) {
            super(itemView);
            songname = (TextView) itemView.findViewById(R.id.songname);
            albumname = (TextView) itemView.findViewById(R.id.albumname);
            duration = (TextView) itemView.findViewById(R.id.durr);
            bottomWrapper = (RelativeLayout) itemView.findViewById(R.id.bottom_wrapper);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.rl);
            bottomWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeDeleteInterface.DeleteTrackFromPlaylist(Long.parseLong(songs.get(getAdapterPosition()-1).getSongId().trim()));
                    Log.d("msg","song id "+Long.parseLong(songs.get(getAdapterPosition()-1).getSongId().trim()));
                    songs.remove(getAdapterPosition()-1);
                    remove(getAdapterPosition());
                }
            });
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   v.setBackgroundColor(Color.parseColor("#aaccdd"));
                    Log.d("Uri of ", "" + songs.get(getAdapterPosition()-1).getSonguri());
                    musicService.setplaylist(songs,getAdapterPosition()-1);
                    musicService.setMediaPlayer();
                    SharedPreferences sharedPreferences =context.getSharedPreferences("select",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("name",songs.get(getAdapterPosition()-1).gettitle());
                    editor.apply();
                }
            });
            constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View v =LayoutInflater.from(context).inflate(R.layout.select_dialog_layout,null);
                    builder.setView(v);
                    builder.setTitle(songs.get(getAdapterPosition()-1).gettitle()+"\n  "+songs.get(getAdapterPosition()-1).getalbum());
                    builder.create();
                    final AlertDialog d=builder.show();

                    //seting click listner.....
                    TextView play = (TextView) v.findViewById(R.id.dialogplay);
                    TextView playnext = (TextView) v.findViewById(R.id.dialogplaynext);
                    TextView queue = (TextView) v.findViewById(R.id.dialogqueue);
                    TextView fav = (TextView) v.findViewById(R.id.dialogaddtofav);
                    TextView album = (TextView) v.findViewById(R.id.dialogalbum);
                    TextView artist = (TextView) v.findViewById(R.id.dialogartist);
                    TextView playlist = (TextView) v.findViewById(R.id.dialogaddtoplaylsit);
                    TextView share = (TextView) v.findViewById(R.id.dialogshare);
                    TextView delete = (TextView) v.findViewById(R.id.dialogdelete);
                    TextView properties = (TextView) v.findViewById(R.id.dialogproperties);
                    play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File f= new File(songs.get(getAdapterPosition()-1).getSonguri().getLastPathSegment());
                            Log.d("LENGTH IS",""+f.length());
                            musicService.setplaylist(songs,getAdapterPosition()-1);
                            musicService.setMediaPlayer();
                            d.dismiss();
                        }
                    });
                    playnext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                            if(mBound == true)
                                musicService.SetNext(songs.get(getAdapterPosition()-1));

                        }
                    });
                    queue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                            musicService.insertinqueue(songs.get(getAdapterPosition()-1));
                        }
                    });

                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });

                    album.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                            Bundle b= new Bundle();
                            b.putString("albumId",songs.get(getAdapterPosition()-1).getAlbumId());
                            b.putString("albumName",songs.get(getAdapterPosition()-1).getalbum()+"");
                            b.putString("which","AlbumDetail");
                            Intent i = new Intent(context, Fragment_Container_Activity.class);
                            i.putExtras(b);
                            context.startActivity(i);
                        }
                    });

                    artist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                            Bundle b= new Bundle();
                            b.putString("ArtistId",songs.get(getAdapterPosition()-1).getArtistId());
                            b.putString("ArtistName",songs.get(getAdapterPosition()-1).getArtist());
                            b.putString("which","ArtistDetail");
                            Intent i = new Intent(context, Fragment_Container_Activity.class);
                            i.putExtras(b);
                            context.startActivity(i);

                        }
                    });
                    playlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);
                            final ArrayList<Playlist> list = PlaylistProvider.queryPlaylists(context.getContentResolver());
                            final ArrayList<String> play = new ArrayList<String>();
                            Log.d("playlist size",""+list.size());
                            for(int i=0;i<list.size();i++)
                            {
                                play.add(list.get(i).getName());
                            }
                            play.add("Add PlaylistProvider");
                            LayoutInflater layoutInflater = LayoutInflater.from(context);
                            View view = layoutInflater.inflate(R.layout.playlistdialog1,null);
                            final View view2 = layoutInflater.inflate(R.layout.playlistdialog2,null);
                            ListView l = (ListView) view.findViewById(R.id.listView);
                            final EditText e = (EditText) view2.findViewById(R.id.edittext);
                            l.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,play));
                            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                                    //add to playlist...
                                    dis.dismiss();
                                    if(p == play.size()-1)
                                    {
                                        builder.setTitle("Create PlaylistProvider");
                                        builder.setView(view2);
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String s = e.getText().toString();
                                                if(s.isEmpty())
                                                {
                                                    Toast.makeText(context,"Enter playlist name",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    PlaylistProvider.createPlaylist(context.getContentResolver(),s);
                                                }
                                                dialog.dismiss();

                                            }
                                        });
                                        builder.create();
                                        dis = builder.show();
                                    }
                                    else
                                    {
                                        //  Log.d("current song albart -> "+currentsong.gettitle(),"  "+currentsong.getAlbumart());
                                        ArrayList<Songs> arrayList = new ArrayList<Songs>();
                                        arrayList.add(songs.get(getAdapterPosition()-1));
                                        PlaylistProvider.addToPlaylist(context.getContentResolver(),Long.parseLong(list.get(p).getId().trim()),arrayList);
                                        //  new TracksFragment.insertintoplaylist(context,songs.get(getAdapterPosition()-1),play.get(p)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    }
                                }
                            });
                            if(play.size()>1)
                            {
                                builder.setTitle("Add to");
                                builder.setView(view);
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create();
                                dis =  builder.show();
                            }
                            else
                            {
                                builder.setTitle("Create new PlaylistProvider");
                                builder.setView(view2);
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String s = e.getText().toString();
                                        if(s.isEmpty())
                                        {
                                            Toast.makeText(context,"Enter playlist name",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            PlaylistProvider.createPlaylist(context.getContentResolver(),s);
                                        }
                                        dialog.dismiss();

                                    }
                                });
                                builder.create();
                                dis = builder.show();
                            }
                            d.dismiss();
                        }
                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(context);
                            b.setMessage("Audio '"+songs.get(getAdapterPosition()-1).gettitle()+"' will be deleted permanently !");
                            b.setTitle("Delete ?");
                            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    d.dismiss();
                                }
                            });
                            b.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String path=songs.get(getAdapterPosition()-1).getSonguri().getPath();
                                    File f= new File(path);
                                    boolean b = f.delete();
                                    Log.d("Is file exist",f.exists()+"");
                                    Log.d("File Lenth",""+f.length());
                                    Log.d("Return value",""+b);
                                    DataBaseClass db = new DataBaseClass(context);
                                    context.getContentResolver().delete(
                                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            MediaStore.MediaColumns.DATA + "='" + path + "'", null
                                    );

                                    // db.removeFromAllPlayList(songs.get(getAdapterPosition()-1));
                                    if(b)
                                    {
                                        songs.remove(getAdapterPosition()-1); // give is Arraylist of Songs(datatype);
                                        remove(getAdapterPosition()-1);
                                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Fail to Delete",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            b.create().show();
                            d.dismiss();

                        }
                    });
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("audio/*");
                            share.putExtra(Intent.EXTRA_STREAM, songs.get(getAdapterPosition()-1).getSonguri());
                            context.startActivity(Intent.createChooser(share, "Share Audio"));
                        }
                    });
                    properties.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(context);
                            File f= new File(songs.get(getAdapterPosition()-1).getSonguri().getPath());
                            long size = (f.length())/1024;
                            long mb= size/1024;
                            long kb= size%1024;

                            b.setMessage("Size:"+"\n"+"Size "+mb+"."+kb+" MB\n"+"Path:"+f.getAbsolutePath()+"\n");
                            b.setTitle(f.getName());
                            b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            b.create().show();
                            d.dismiss();
                        }
                    });

                    return true;
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

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d("msg","detached from recycler view");
    }


}
