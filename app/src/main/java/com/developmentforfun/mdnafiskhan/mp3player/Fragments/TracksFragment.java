package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.AlbumDetail;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.ArtistActivity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.MusicPlayerActivity;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.ListViewAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by mdnafiskhan on 03-01-2017.
 */

public class TracksFragment extends Fragment {
    songDetailloader loader = new songDetailloader();
    ArrayList<Songs> give = new ArrayList<>();
    public int pos = -1;
    MediaPlayer mp ;
    MusicService musicService;
    boolean mBound;
    ListViewAdapter listViewAdapter;
    AlertDialog dis;
    private Cursor cursor ;
    RecyclerView recyclerView ;
    int albumindex,dataindex,titleindex,durationindex,artistindex;
    private final static String[] columns ={MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID};
    private final String where = "is_music AND duration > 10000 AND _size <> '0' ";
    private final String orderBy =  MediaStore.Audio.Media.TITLE;
    public TracksFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fragment created","created");
        loader.set(getContext());



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v =inflater.inflate(R.layout.listviewofsongs,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        new allsongs().execute();
       // allsongs();
       // intlistview();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getActivity(),MusicService.class);
                getActivity().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        }).start();
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("recycler view cliked","true");
                int position = recyclerView.getChildAdapterPosition(v);
                Log.d("Uri of ",""+give.get(position).getSonguri());
                musicService.setplaylist(give,give.get(position).getPosition());
                musicService.setMediaPlayer();
                SharedPreferences sharedPreferences =getContext().getSharedPreferences("select",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("name",give.get(position).gettitle());
                editor.apply();
            }
        });

        recyclerView.setOnLongClickListener(new AdapterView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = recyclerView.getChildAdapterPosition(view);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v =LayoutInflater.from(getContext()).inflate(R.layout.select_dialog_layout,null);
                builder.setView(v);
                builder.setTitle(give.get(position).gettitle()+"\n  "+give.get(position).getalbum());
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
                        File f= new File(give.get(position).getSonguri().getLastPathSegment());
                        Log.d("LENGTH IS",""+f.length());
                        musicService.setplaylist(give,position);
                        musicService.setMediaPlayer();
                        d.dismiss();
                    }
                });
                playnext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();

                    }
                });
                queue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        musicService.insertinqueue(give.get(position));
                    }
                });

                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        DataBaseClass db = new DataBaseClass(getContext());
                        int i=db.insetintoliked(give.get(position));
                        if(i==1)
                        {
                            Toast.makeText(getContext(),"Added to Favorites",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getContext(),"Already in Favorites",Toast.LENGTH_SHORT).show();
                    }
                });

                album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        Intent i = new Intent( getActivity() , AlbumDetail.class);
                        Bundle b= new Bundle();
                        b.putCharSequence("album",give.get(position).getalbum());
                        i.putExtras(b);
                        startActivity(i);
                    }
                });

                artist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getActivity(),ArtistActivity.class);
                        i.putExtra("artist",give.get(position).getartist());
                        startActivity(i);
                        d.dismiss();

                    }
                });
                playlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
                        final DataBaseClass db = new DataBaseClass(getActivity().getBaseContext());
                        final ArrayList<String> list = db.findplaylist();
                        list.add("Add Playlist");
                        LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getBaseContext());
                        View view = layoutInflater.inflate(R.layout.playlistdialog1,null);
                        final View view2 = layoutInflater.inflate(R.layout.playlistdialog2,null);
                        ListView l = (ListView) view.findViewById(R.id.listView);
                        final EditText e = (EditText) view2.findViewById(R.id.edittext);
                        l.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,list));
                        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                                //add to playlist...
                                if(p == list.size()-1)
                                {
                                    dis.dismiss();
                                    builder.setTitle("Create Playlist");
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
                                                Toast.makeText(getActivity().getBaseContext(),"Enter playlist name",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                db.createTable(s);
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
                                    new insertintoplaylist(getActivity().getBaseContext(),give.get(position),list.get(p)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            }
                        });
                        if(list.size()>1)
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
                            builder.setTitle("Create Playlist");
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
                                        Toast.makeText(getContext(),"Enter playlist name",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        db.createTable(s);
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
                        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                            b.setMessage("Audio '"+give.get(position).gettitle()+"' will be deleted permanently !");
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
                                File f= new File(give.get(position).getSonguri().getPath());
                                boolean b = f.delete();
                                Log.d("Is file exist",f.exists()+"");
                                Log.d("File Lenth",""+f.length());
                                Log.d("Return value",""+b);
                                loader.set(getContext());
                                loader.deleteSong(getContext(),give.get(position).getPosition());
                                give.remove(position); // give is Arraylist of Songs(datatype);
                                listViewAdapter.notifyDataSetChanged();
                                if(b)
                                {
                                    Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Fail to Delete",Toast.LENGTH_SHORT).show();
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
                        share.putExtra(Intent.EXTRA_STREAM, give.get(position).getSonguri());
                        startActivity(Intent.createChooser(share, "Share Audio"));
                    }
                });
                properties.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                        File f= new File(give.get(position).getSonguri().getPath());
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
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("fragment","instance saved");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("Fragment","Instance Restored");
    }


    public void intlistview()
    {
        recyclerView.setAdapter(new CustomRecyclerViewAdapter(getActivity(),give));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment","Destroyed");
       try{
           getActivity().unbindService(serviceConnection);
       }catch (Exception e)
       {

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


    public void allsongs()
    {
        cursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
        dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++)
        {
            Songs song = new Songs();
            song.setalbum(cursor.getString(albumindex));
            song.settitle(cursor.getString(titleindex));
            song.setSonguri(Uri.parse(cursor.getString(dataindex)));
            song.setartist(cursor.getString(artistindex));
            song.setDuration(Long.decode(cursor.getString(durationindex)));
            song.setPosition(cursor.getPosition());
            song.setAlbumart(loader.albumartwithalbum(song.getalbum()));
            this.give.add(song);
            cursor.moveToNext();
        }
        cursor.close();

    }

    public class allsongs extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            allsongs();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            intlistview();
        }
    }
public static class insertintoplaylist extends AsyncTask<Void,Void,Void>
{
    Songs s = new Songs();
    ArrayList<Songs> songs = new ArrayList<>();
    Context context;
    String playlistname ;
    int flag=0;
    public insertintoplaylist(Context context ,Songs s ,String playlistname) {
        super();
        this.context = context;
        this.s=s;
        this.playlistname = playlistname;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("here1","  ");
        DataBaseClass db = new DataBaseClass(context);
        songs = db.getfromplaylist(playlistname);
        if(!songs.contains(s)) {
            Log.d("here","  ");
            db.insertintoplaylist(s, playlistname);
        }
        else
        {
            flag=1;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(flag == 1)
        {
            Toast.makeText(context,"Song already exist in playlist",Toast.LENGTH_SHORT).show();
        }
    }
}



}
