package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.ListViewAdapter;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayerActivity extends Activity{

    public static boolean isactivityisrenning ;
    public static RelativeLayout relativeLayout,moving;
    static ImageButton play, prev,next;
    Button info;
    static SeekBar seekBar;
    byte[] a;
    boolean isClicked = false;
    public static TextView album,current_time,total,like,addtoplaylist;
    int x;
    static ImageView albumart;
    static Bitmap image;
    static TextView name;
    MediaMetadataRetriever data = new MediaMetadataRetriever();
    public int seek_max;
    int currentsongposition;
    static Uri currentsonguri;
    int flag;
    songDetailloader loader;
    public static Context context;
    private int c= 0;
    Songs currentsong = new Songs();
   public static MusicService music= new MusicService();
    boolean mBound;
    AlertDialog dis ;
    int position;
        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.music_player_layout);
            Intent recived = getIntent();
            currentsonguri = recived.getData();
            flag = recived.getIntExtra("flag",0);
            Log.d("values","flag ="+flag);
            Log.d("currentsonguri",""+currentsonguri);
            Intent i = new Intent(this, MusicService.class);
            bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.d("activity", "Create");
            isactivityisrenning = true;
            context = getBaseContext();
            c=0;
             loader = new songDetailloader(getBaseContext());
            Log.d("mystatus", "" + isactivityisrenning);
            if(flag == 0)
            {
                String s =currentsonguri.getPath();
                Log.d("floag ==0 and path",""+currentsonguri.getPath());
                currentsong = loader.sonngwithuri(s);
                Log.d("current song title",""+currentsong.gettitle());
                music.setonlyonesong(currentsong,this);
            }

          /*  if(flag == 0)
            {
                    if(music.mediaPlayer.isPlaying())
                    {
                        music.stop();
                    }
                    MediaPlayer mp =MediaPlayer.create(this,currentsonguri);
                    mp.start();

            }
            if(flag == 0) {

                data.setDataSource(this,currentsonguri);
            }
            else
            {*/
            if(flag!=0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s =currentsonguri.getPath();
                        Log.d("floag ==0 and path",""+currentsonguri.getPath());
                        currentsong = loader.sonngwithuri(s);
                        Log.d("Loader return song", "" + currentsong.gettitle() + " at " + currentsong.getPosition());
                        if (mBound) {
                            Log.d("", "i think service is bounded");
                            music.setnotification();
                        }
                    }
                }).run();
            }
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        moving = (RelativeLayout) findViewById(R.id.moving);
        seekBar = (SeekBar) findViewById(R.id.seek);
        play = (ImageButton) findViewById(R.id.play);
        like = (TextView) findViewById(R.id.liketext);
        addtoplaylist = (TextView) findViewById(R.id.playlist);
        album = (TextView) findViewById(R.id.songalbum_andartist);
        prev = (ImageButton) findViewById(R.id.prev);
        next =(ImageButton) findViewById(R.id.next);
        total =(TextView) findViewById(R.id.totaltime);
        current_time= (TextView) findViewById(R.id.currtime);
        albumart = (ImageView) findViewById(R.id.albumart);
         name = (TextView) findViewById(R.id.heding);
            Log.d("size of list",""+music.songs_current_playlist.size());
            seek_max= (int) currentsong.getDuration()/100; ///its nedds to to changed also...
            seekBar.setMax(100);
            seekBar.setProgress(0);
          //  seekUpdation(seek_max);
            play.setImageResource(android.R.drawable.ic_media_pause);
            prev.setImageResource(android.R.drawable.ic_media_previous);
            next.setImageResource(android.R.drawable.ic_media_next);

      try{
            Uri u =currentsong.getSonguri();
             if( u != null)
                 Log.d("SET","u is not null");
                 data.setDataSource(this,u);
                 a = data.getEmbeddedPicture();
                 image = BitmapFactory.decodeByteArray(a, 0, a.length);
                 albumart.setImageBitmap(image);
                 Log.d("SET","i have set the album art");
                // updatePlayerBar(image);

       }
       catch (IllegalArgumentException e) {
           String s =loader.albumartwithalbum(currentsong.getalbum());
              if(s!=null)
           albumart.setImageBitmap(BitmapFactory.decodeFile(s));
             else
              {
                  albumart.setImageResource(R.drawable.default_track_light);
              }
           Log.d("SET", "Exeption occur in imagesetting");

       }catch (Exception e)
      {
          e.printStackTrace();
      }
        try {

           // Bitmap blurredBitmap = blur(image);
           // BitmapDrawable ob = new BitmapDrawable(getResources(), blurredBitmap);
            //backg.setBackground(ob);
            current_time.setText(gettimeinformat(music.getcurrentpositionofsongtime()));
            name.setText(currentsong.gettitle());
            album.setText(currentsong.getartist()+" - "+currentsong.getalbum());
            total.setText(gettimeinformat((int) currentsong.getDuration()));

        } catch (Exception e) {
            name.setText("unknown title");
            album.setText("unknown artist - unknown album");
        }
        //  Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
      //      album_gridview.startAnimation(animation);
        albumart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                albumart.startAnimation(animation);
                return true;
            }
        });
            albumart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(c%2==0) {
                        moving.setTranslationY(50f);
                        moving.setAlpha(0f);
                        moving.setVisibility(View.VISIBLE);
                        moving.animate()
                                .setDuration(500)
                                .alpha(0.6f)
                                .translationY(0f)
                                .start();
                        c++;
                    }
                    else
                    {
                        moving.setVisibility(View.INVISIBLE);
                        c++;
                    }
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.play_pause();
                if(music.isplaying())
                {
                    play.setImageResource(android.R.drawable.ic_media_pause);
                }
                else
                {
                    play.setImageResource(android.R.drawable.ic_media_play);
                }


            }
        });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseClass db = new DataBaseClass(getBaseContext());
                    int r =db.insetintoliked(music.Current_playing_song);
                    if(r==1)
                    Toast.makeText(getBaseContext(),"Added to Favorite",Toast.LENGTH_SHORT).show();
                    else if(r == 0)
                        Toast.makeText(getBaseContext(),"Already in Favorite",Toast.LENGTH_SHORT).show();
                }
            });

            addtoplaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MusicPlayerActivity.this,R.style.MyAlertDialogStyle);
                    final DataBaseClass db = new DataBaseClass(getBaseContext());
                    final ArrayList<String> list = db.findplaylist();
                    list.add("Add Playlist");
                    LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
                    View view = layoutInflater.inflate(R.layout.playlistdialog1,null);
                    final View view2 = layoutInflater.inflate(R.layout.playlistdialog2,null);
                    ListView l = (ListView) view.findViewById(R.id.listView);
                    final EditText e = (EditText) view2.findViewById(R.id.edittext);
                    l.setAdapter(new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,list));
                    l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //add to playlist...
                            if(position == list.size()-1)
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
                                            Toast.makeText(getBaseContext(),"Enter playlist name",Toast.LENGTH_SHORT).show();
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
                                Log.d("current song albart -> "+currentsong.gettitle(),"  "+currentsong.getAlbumart());
                                new insertintoplaylist(getBaseContext(),currentsong,list.get(position)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                                    Toast.makeText(getBaseContext(),"Enter playlist name",Toast.LENGTH_SHORT).show();
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
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    music.playnext();
                    currentsong = music.getCurrentsong();
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    music.playprev();
                    currentsong = music.getCurrentsong();
                }
            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int ct;
                int du=(int) currentsong.getDuration();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               ct=music.getcurrentpositionofsongtime();
               position =i;
            }


          @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            //   Log.d("in","Stop tracking touch");
                ct= music.getcurrentpositionofsongtime();
            //    Log.d("","Current position->>>>"+position);
                int ford = position*(du/100);
                current_time.setText( gettimeinformat(ct));
                music.seekmediaplayer(ford);
            }
        });

        //   updateyourself(music.Current_playing_song);
    }

    public void updateyourself(Songs now)
    {   float time;
        Log.d("mystatus",""+isactivityisrenning);
        this.currentsong = now;
        currentsongposition =now.getPosition();
        int max= (int) currentsong.getDuration()/100;
        seekBar.setMax(100);
        total.setText(gettimeinformat((int) now.getDuration()));
        Log.d("is song is playing", now.getDuration()+"");
        if(music.isplaying())
            play.setImageResource(android.R.drawable.ic_media_pause);
        else
            play.setImageResource(android.R.drawable.ic_media_play);
        prev.setImageResource(android.R.drawable.ic_media_previous);
        next.setImageResource(android.R.drawable.ic_media_next);


        try{
            data.setDataSource(this,currentsong.getSonguri());
            a = data.getEmbeddedPicture();
            image = BitmapFactory.decodeByteArray(a, 0, a.length);
            albumart.setImageBitmap(image);
           // updatePlayerBar(image);

        }
        catch (IllegalArgumentException e)
        {
            loader = new songDetailloader(context);
            String s =loader.albumartwithalbum(currentsong.getalbum());
            if(s!=null)
                albumart.setImageBitmap(BitmapFactory.decodeFile(s));
            else
            {
                albumart.setImageResource(R.drawable.default_track_light);
            }
            Log.d("SET", "Exeption occur in imagesetting");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            // Bitmap blurredBitmap = blur(image);
            // BitmapDrawable ob = new BitmapDrawable(getResources(), blurredBitmap);
            //backg.setBackground(ob);

            name.setText(now.gettitle());
            album.setText(now.getartist()+"-"+now.getalbum());

        } catch (Exception e) {
            name.setText("unknown title");
            album.setText("unknown artist - album");
        }
        time=music.getcurrentpositionofsongtime();
        int t=0;
        if(max!=0) {
             t = (int) time / max;
        }
        current_time.setText(gettimeinformat((int)time));
        seekBar.setProgress(t);
        seek_max = max ;
      //  seekUpdation(max);



    }

    public String gettimeinformat(int a)
    {
        String s;
        String st;
        int min = (a/1000)/60;
        int sec = (a/1000)-(min*60);
        if(sec<=9)
            return ""+min+":"+"0"+sec;
        else
            return  ""+min+":"+sec;


    }


    public void updatePlayerBar(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getDominantSwatch();
                if (swatch == null) swatch = palette.getMutedSwatch(); // Sometimes vibrant swatch is not available
                if (swatch != null) {
                    // Set the background color of the player bar based on the swatch color
                    // Update the track's title with the proper title text color
                    // Update the artist name with the proper body text color
                    relativeLayout.setBackgroundColor(palette.getDominantColor(Color.BLACK));
                }
            }
        });
    }




    //Set the radius of the Blur. Supported range 0 < radius <= 25
    private static final float BLUR_RADIUS = 25f;

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }



 /*   Runnable run = new Runnable()
    { @Override public void run()
    {
        //  seekUpdation(m); }

    };

    public void seekUpdation(final int m)
    {


        float time;
        try {

            time=music.getcurrentpositionofsongtime();
            int t=(int)time/seek_max;
            current_time.setText(gettimeinformat((int)time));
            seekBar.setProgress(t);

        }
        catch (IllegalStateException e)
        {
        }
       // seekHandler.postDelayed(run,1000);
    }
*/
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            music  =  binder.getService();
            mBound =true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound =false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("activity","restarted");
        isactivityisrenning = true;
        updateyourself(music.Current_playing_song);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("activity","paused");


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("activity","start");
        isactivityisrenning = true;
        Intent i = new Intent(this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("activity","resume");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("activity","stop");
        isactivityisrenning = false;

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound)
            unbindService(serviceConnection);
        Log.d("activity","Destroy");
        isactivityisrenning = false;
    }


    public Bitmap imagewithalbum(String album)
    {
        String[] col = {MediaStore.Audio.Albums.ALBUM,MediaStore.Audio.Albums.ALBUM_ART};
        String where ="album = '"+album+"'" ;
        Cursor c=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,col ,where ,null,null);
        String image= c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        Bitmap b=BitmapFactory.decodeFile(image);
        return b;

    }

      public class insertintoplaylist extends AsyncTask<Void,Void,Void>
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




