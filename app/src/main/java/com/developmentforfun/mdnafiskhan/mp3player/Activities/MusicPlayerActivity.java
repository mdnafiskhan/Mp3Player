package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.ViewpagerAnimation.ZoomOutPageTransformer;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomAdapter;

import jp.wasabeef.blurry.Blurry;

public class MusicPlayerActivity extends AppCompatActivity{

    public static boolean isactivityisrenning ;
    public static RelativeLayout relativeLayout;
    public LinearLayout mainline;
    static ImageView play, prev,next,background;
    Button info;
    static SeekBar seekBar;
    byte[] a;
    boolean isClicked = false;
    public static TextView album,current_time,total;
    int x;
    float xcor=0;
    static ImageView albumart;
    static Bitmap image;
    static TextView name;
    MediaMetadataRetriever data = new MediaMetadataRetriever();
    public int seek_max;
    int currentsongposition;
    static Uri currentsonguri;
    int flag;
    SongDetailLoader loader;
    public static Context context;
    private int c= 0;
    Songs currentsong = new Songs();
   public static MusicService music= new MusicService();
   public  Animation zoomin, zoomout;
   boolean zoomingin;
    boolean mBound;
    AlertDialog dis ;
    int position;
    ViewPager viewPager;
        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.music_player_layout2);
            Log.d("here in ..........","onCreate");
            viewPager = (ViewPager) findViewById(R.id.ViewPager);
            viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
            viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager()));
            Intent recived = getIntent();
            currentsonguri = recived.getData();
            background = (ImageView) findViewById(R.id.backimage) ;
            flag = recived.getIntExtra("flag",0);
            Log.d("values","flag ="+flag);
            Log.d("currentsonguri",""+currentsonguri);
            Log.d("current playlist size",""+music.getPlaylist().size());
            Intent i = new Intent(this, MusicService.class);
            bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
            mBound = true;
            Log.d("activity", "Create");
            isactivityisrenning = true;
            context = getBaseContext();
            c=0;
             loader = new SongDetailLoader(getBaseContext());
            Log.d("mystatus", "" + isactivityisrenning);
            if(music.mediaPlayer.isPlaying() &&savedInstanceState==null) {
                if (mBound) {
                    Log.d("", "i think service is bounded");
                    music.setnotification();
                }
                updateyourself();
            }
            else
            {
                updateyourself();
            }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected","true");
                int p = MusicService.positionOfCurrentSong;
                Log.d(" p =",p+"");
                int max = music.getPlaylist().size()-1;
                if(p>=0 || p<=max)
                {
                    if(position-p==1)
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                music.playnext(true);
                            }
                        }).start();
                        updateyourself();
                    }
                    else if(p-position==1)
                        music.playprev();
                    currentsong = music.getCurrentsong();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        seekBar = (SeekBar) findViewById(R.id.seek);
        seekBar.setMax(1000);
        play = (ImageView) findViewById(R.id.play);
        album = (TextView) findViewById(R.id.songalbum_andartist);
        prev = (ImageView) findViewById(R.id.prev);
        next =(ImageView) findViewById(R.id.next);
        total =(TextView) findViewById(R.id.totaltime);
        current_time= (TextView) findViewById(R.id.currtime);
       // albumart = (ImageView) findViewById(R.id.albumart);
            name = (TextView) findViewById(R.id.heding);
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
                /// albumart.setImageBitmap(image);
                 setBlurBackdround(image);
                 Log.d("SET","i have set the album art");
                // updatePlayerBar(image);

       }
       catch (IllegalArgumentException e) {
           String s =loader.albumartwithalbum(currentsong.getalbum());
              if(s!=null)
         /* albumart.setImageBitmap(BitmapFactory.decodeFile(s));
             else
              {
               //   albumart.setImageResource(R.drawable.default_track_light);
              }*/
           Log.d("SET", "Exeption occur in imagesetting");

       }catch (Exception e)
      {
          e.printStackTrace();
      }
        //  Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
      //      album_gridview.startAnimation(animation);
      /*  albumart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_UP  :
                        Log.d("MotionEvent","Action up");

                        break;
                    case MotionEvent.ACTION_DOWN :
                        Log.d("MotionEvent","Action down");
                        xcor = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(xcor-event.getX()>2)
                        {
                            ObjectAnimator.ofFloat(albumart,"translationX", -100).setDuration(200).start();
                        }
                        if((xcor-event.getX())<2)
                        {
                            ObjectAnimator.ofFloat(albumart,"translationX", 0).setDuration(200).start();
                        }


                }
                return true;
            }
        });*/
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
      /*      like.setOnClickListener(new View.OnClickListener() {
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
            });*/

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    music.playnext(true);
                    currentsong = music.getCurrentsong();
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    music.playprev();
                    currentsong = music.getCurrentsong();
                    viewPager.setCurrentItem(viewPager.getCurrentItem()-1);

                }
            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int ct;
                int du=(int) currentsong.getDuration();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int t = (i*(int)music.Current_playing_song.getDuration())/1000;
                music.seekmediaplayer(t);
                current_time.setText(gettimeinformat(t));
            }


          @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            //   Log.d("in","Stop tracking touch");
               /* ct= music.getcurrentpositionofsongtime();
                Log.d("","Current position->>>>"+position);
                int d =(int) music.Current_playing_song.getDuration();
                int ford = position*(d/100);
                Log.d("du = ",""+du+" d="+d);
                Log.d("position = ",""+position);
                current_time.setText( gettimeinformat(ct));
                music.seekmediaplayer(ford);*/
            }
        });

        //   updateyourself(music.Current_playing_song);
    }

    public void updateyourself()
    {
       // startImageZoomAnimation();
        float time;
        Log.d("mystatus",""+isactivityisrenning);
        currentsong = music.Current_playing_song;
        Songs now = currentsong;
        currentsongposition =now.getPosition();
        int fraction=(int)  currentsong.getPosition()/(int)currentsong.getDuration();
        seekBar.setProgress(fraction);
        total.setText(gettimeinformat((int) now.getDuration()));
        current_time.setText(gettimeinformat(music.Current_playing_song.getPosition()));
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
           // albumart.setImageBitmap(image);
            setBlurBackdround(image);
           // updatePlayerBar(image);

        }
        catch (IllegalArgumentException e)
        {
            loader = new SongDetailLoader(context);
            String s =loader.albumartwithalbum(currentsong.getalbum());
            if(s!=null) {
                Bitmap b = BitmapFactory.decodeFile(s);
              //  albumart.setImageBitmap(b);
                setBlurBackdround(b);
            }
            else
            {
               // albumart.setImageResource(R.drawable.default_track_light);
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

            name.setText(now.getTitle());
            album.setText(now.getartist()+"-"+now.getalbum());

        } catch (Exception e) {
            name.setText("unknown title");
            album.setText("unknown artist - album");
        }

      //  seekUpdation(max);
        //Color update...

    }

    public void setBlurBackdround(Bitmap bitmap)
    {
     //   updatePlayerBar(bitmap);
        try {
            Blurry.with(context).radius(4)
                    .sampling(5).color(Color.argb(0, 255, 255, 0))
                    .async()
                    .animate(200).from(bitmap).into(background);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String gettimeinformat(long a)
    {
        long second = (a / 1000) % 60;
        long minute = (a / (1000 * 60)) % 60;

        String time = String.format("%02d:%02d", minute, second);
        return time;

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
                  //  name.setTextColor(palette.getDarkVibrantColor(Color.WHITE));
                 //   album.setTextColor(palette.getMutedColor(Color.WHITE));
                }
            }
        });
    }

    public void startImageZoomAnimation()
    {
       try {
           zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
           zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
           zoomin.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {

               }

               @Override
               public void onAnimationEnd(Animation animation) {
                   startImageZoomAnimation();
               }

               @Override
               public void onAnimationRepeat(Animation animation) {

               }
           });
           zoomout.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {

               }

               @Override
               public void onAnimationEnd(Animation animation) {
                   startImageZoomAnimation();
               }

               @Override
               public void onAnimationRepeat(Animation animation) {

               }
           });
           if (zoomingin) {
             //  albumart.setAnimation(zoomout);
               zoomingin = false;
           } else {
             //  albumart.setAnimation(zoomin);
               zoomingin = true;
           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }



    //Set the radius of the Blur. Supported range 0 < radius <= 25
    private static final float BLUR_RADIUS = 25f;

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
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
        updateyourself();

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


}




