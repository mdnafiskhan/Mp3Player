package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.TracksFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 26/06/2017.
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Songs> songs = new ArrayList<>();
    LayoutInflater layoutInflater;
    MusicService musicService;
    boolean mBound = false;
    songDetailloader loader = new songDetailloader();
    AlertDialog dis;



    public CustomRecyclerViewAdapter(final Context context , ArrayList<Songs> songs) {
        super();
        this.context = context;
        this.songs = songs;
        layoutInflater = LayoutInflater.from(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(context,MusicService.class);
                context.bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        }).start();
        loader.set(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.trackscontentlayout,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
             holder.songname.setText(songs.get(position).gettitle());
             holder.albumname.setText(songs.get(position).getalbum());
             holder.duration.setText(time(songs.get(position).getDuration()));
             new getalbumart(holder.albumart,position).execute();
             holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     Log.d("Uri of ",""+songs.get(position).getSonguri());
                     musicService.setplaylist(songs,songs.get(position).getPosition());
                     musicService.setMediaPlayer();
                     SharedPreferences sharedPreferences =context.getSharedPreferences("select",Context.MODE_PRIVATE);
                     SharedPreferences.Editor editor= sharedPreferences.edit();
                     editor.putString("name",songs.get(position).gettitle());
                     editor.apply();
                 }
             });
           holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {
                   final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   View v =LayoutInflater.from(context).inflate(R.layout.select_dialog_layout,null);
                   builder.setView(v);
                   builder.setTitle(songs.get(position).gettitle()+"\n  "+songs.get(position).getalbum());
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
                           File f= new File(songs.get(position).getSonguri().getLastPathSegment());
                           Log.d("LENGTH IS",""+f.length());
                           musicService.setplaylist(songs,position);
                           musicService.setMediaPlayer();
                           d.dismiss();
                       }
                   });
                   playnext.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           d.dismiss();
                           if(mBound == true)
                                musicService.SetNext(songs.get(position));

                       }
                   });
                   queue.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           d.dismiss();
                           musicService.insertinqueue(songs.get(position));
                       }
                   });

                   fav.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           d.dismiss();
                           DataBaseClass db = new DataBaseClass(context);
                           int i=db.insetintoliked(songs.get(position));
                           if(i==1)
                           {
                               Toast.makeText(context,"Added to Favorites",Toast.LENGTH_SHORT).show();
                           }
                           else
                               Toast.makeText(context,"Already in Favorites",Toast.LENGTH_SHORT).show();
                       }
                   });

                   album.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           d.dismiss();
                           Intent i = new Intent( context , AlbumDetail.class);
                           Bundle b= new Bundle();
                           b.putCharSequence("album",songs.get(position).getalbum());
                           i.putExtras(b);
                           context.startActivity(i);
                       }
                   });

                   artist.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent i=new Intent(context,ArtistActivity.class);
                           i.putExtra("artist",songs.get(position).getartist());
                           context.startActivity(i);
                           d.dismiss();

                       }
                   });
                   playlist.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);
                           final DataBaseClass db = new DataBaseClass(context);
                           final ArrayList<String> list = db.findplaylist();
                           list.add("Add Playlist");
                           LayoutInflater layoutInflater = LayoutInflater.from(context);
                           View view = layoutInflater.inflate(R.layout.playlistdialog1,null);
                           final View view2 = layoutInflater.inflate(R.layout.playlistdialog2,null);
                           ListView l = (ListView) view.findViewById(R.id.listView);
                           final EditText e = (EditText) view2.findViewById(R.id.edittext);
                           l.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list));
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
                                                   Toast.makeText(context,"Enter playlist name",Toast.LENGTH_SHORT).show();
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
                                       new TracksFragment.insertintoplaylist(context,songs.get(position),list.get(p)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                                           Toast.makeText(context,"Enter playlist name",Toast.LENGTH_SHORT).show();
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
                           AlertDialog.Builder b = new AlertDialog.Builder(context);
                           b.setMessage("Audio '"+songs.get(position).gettitle()+"' will be deleted permanently !");
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
                                   File f= new File(songs.get(position).getSonguri().getPath());
                                   boolean b = f.delete();
                                   Log.d("Is file exist",f.exists()+"");
                                   Log.d("File Lenth",""+f.length());
                                   Log.d("Return value",""+b);
                                   loader.set(context);
                                   loader.deleteSong(context,songs.get(position).getPosition());
                                   songs.remove(position); // give is Arraylist of Songs(datatype);
                                   remove(position);
                                   if(b)
                                   {
                                       Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
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
                           share.putExtra(Intent.EXTRA_STREAM, songs.get(position).getSonguri());
                           context.startActivity(Intent.createChooser(share, "Share Audio"));
                       }
                   });
                   properties.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           AlertDialog.Builder b = new AlertDialog.Builder(context);
                           File f= new File(songs.get(position).getSonguri().getPath());
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

    @Override
    public int getItemCount() {
        return songs.size();
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




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView songname;
        TextView albumname;
        TextView duration ;
        ImageView albumart ;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            songname = (TextView) itemView.findViewById(R.id.songname);
            albumname = (TextView) itemView.findViewById(R.id.albumname);
            duration = (TextView) itemView.findViewById(R.id.durr);
            albumart = (ImageView) itemView.findViewById(R.id.albumart);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl);

        }
    }
    public class getalbumart extends AsyncTask<Void,Void,Void> {
        Bitmap bitmap;
        ImageView imageView;
        int p;

        public getalbumart(ImageView v, int position) {
            this.imageView = v;
            this.p = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageView.setImageResource(R.drawable.default_track_light);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeFile(songs.get(p).getAlbumart(), options);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
            return null;
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

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        musicService.unbindService(serviceConnection);
    }


}
