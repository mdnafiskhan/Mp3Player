package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.EqualiserAc;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.MediaEvents;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.AppDatabase;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.CloneSong;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.SongEntity;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.ViewpagerAnimation.ZoomOutPageTransformer;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomAdapter;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.blurry.Blurry;


/**
 * Created by mdnafiskhan on 06/01/2018.
 */

public class PlayerActivity extends AppCompatActivity implements MediaEvents {

    public ViewPager viewPager;
    public SeekBar seekBar;
    public String currentTime;
    public String lengthOfSong;
    public ImageView next,playPause,prev,repeat;
    public TextView vcurrentTime,vlengthOfSong,vAlbum,vSongtitle;
    CustomAdapter customAdapter;
    public TextView songTitle ;
    MediaMetadataRetriever data = new MediaMetadataRetriever();
    Thread thread;
    Handler handler;
    Runnable runnable;
    TimerTask timertask;
    Timer t = new Timer();
    boolean mBound;
    ImageView background;
    public static MusicService music = new MusicService();
    ConstraintLayout constraintLayout;
    MediaEvents mediaEvents;
    boolean setBlurBackground = false;
    ImageView equalizer,share,showPlaylist;
    public static LikeButton likeButton;
    static AppDatabase db;

    public PlayerActivity() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_layout2);
        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        seekBar = (SeekBar) findViewById(R.id.seek);
        next = (ImageView) findViewById(R.id.next);
        playPause = (ImageView) findViewById(R.id.play);
        prev = (ImageView) findViewById(R.id.prev);
        repeat = (ImageView) findViewById(R.id.repeat);
        vcurrentTime = (TextView) findViewById(R.id.currtime);
        vlengthOfSong = (TextView) findViewById(R.id.totaltime);
        vAlbum = (TextView) findViewById(R.id.heding);
        vSongtitle = (TextView) findViewById(R.id.songalbum_andartist);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        background = (ImageView) findViewById(R.id.backimage);
        songTitle = (TextView) findViewById(R.id.songtitle);
        equalizer = (ImageView) findViewById(R.id.equaliser);
        share = (ImageView) findViewById(R.id.share);
        likeButton = (LikeButton) findViewById(R.id.star_button);
        showPlaylist = (ImageView) findViewById(R.id.showPlaylist);
        songTitle.setSelected(true);
        seekBar.setMax(1000);
        handler = new Handler();
//Make sure you update Seekbar on UI thread
              runnable = new Runnable() {

            @Override
            public void run() {
                updateYourSelf();
                //updateViewPager();
               // new RoomDatabaseAccess(null, 2).execute();  //********TO DO********//
                handler.postDelayed(this, 1000);
            }
        };
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        Intent i = new Intent(this, MusicService.class);
        bindService(i, serviceConnection, Context.BIND_ADJUST_WITH_ACTIVITY);
        mBound = true;
        customAdapter =new CustomAdapter(getSupportFragmentManager());
        viewPager.setAdapter(customAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    updateYourSelf();
                 //   setBlurBackdround();
                }
                if(msg.what == 2){

                    updateYourSelf();
                 //   setBlurBackdround();
                }
            }
        };
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, music.Current_playing_song.getSonguri());
                startActivity(Intent.createChooser(share, "Share Audio"));
            }
        });
        equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlayerActivity.this,EqualiserAc.class);
                startActivity(i);
            }
        });
        showPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                SongEntity songEntities = CloneSong.cloneInto(music.Current_playing_song);
                new RoomDatabaseAccess(songEntities,0).execute();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                SongEntity songEntities = CloneSong.cloneInto(music.Current_playing_song);
                new RoomDatabaseAccess(songEntities,1).execute();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int stateOfPage;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                 int p = MusicService.positionOfCurrentSong;
                int max = music.getPlaylist().size() - 1;
                Log.d("cordinate", "pos =" + position + " max =" + max+" p = "+p);
                if ((position >= 0 || position <= max && p!=position) && positionOffsetPixels < 150) {
                    if (position - p == 1) {
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                //music.playnext(true);
                                Intent i = new Intent(PlayerActivity.this,MusicService.class);
                                i.setAction("next");
                                i.putExtra("id","aa");
                                startService(i);
                                handler.sendEmptyMessage(1);
                            }
                        };
                        t.start();
                    } else if (p - position == 1) {
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                Intent i = new Intent(PlayerActivity.this,MusicService.class);
                                i.setAction("prev");
                                i.putExtra("id","aa");
                                startService(i);
                                handler.sendEmptyMessage(2);

                            }
                        };
                        t.start();
                    }
                }
            }


            @Override
            public void onPageSelected(int position) {
                /*int p = MusicService.positionOfCurrentSong;
                Log.d(" p =", p + "");
                int max = music.getPlaylist().size() - 1;
                Log.d("cordinate", "pos =" + position + " max =" + max);
                if (position >= 0 || position <= max && p!=position) {
                    if (position - p == 1) {
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                //music.playnext(true);
                                Intent i = new Intent(PlayerActivity.this,MusicService.class);
                                i.setAction("next");
                                i.putExtra("id","aa");
                                startService(i);
                                handler.sendEmptyMessage(1);
                            }
                        };
                        t.start();
                    } else if (p - position == 1) {
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                Intent i = new Intent(PlayerActivity.this,MusicService.class);
                                i.setAction("prev");
                                i.putExtra("id","aa");
                                startService(i);
                                handler.sendEmptyMessage(2);
                            }
                        };
                        t.start();
                    }
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                 stateOfPage = state;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pos;
            boolean man=false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                man = b;
                pos = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(man) {
                    music.seekmediaplayer((pos * (int) music.Current_playing_song.getDuration()) / 1000);
                    Log.d("pos", music.mediaPlayer.getCurrentPosition() + "");
                    vcurrentTime.setText(gettimeinformat(music.mediaPlayer.getCurrentPosition()) + "");
                    man=false;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // music.playnext(true);
                Intent i = new Intent(PlayerActivity.this,MusicService.class);
                i.setAction("next");
                i.putExtra("id","aa");
                startService(i);
               // mediaEvents.onNextPress();
                updateYourSelf();
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1,true);
                new RoomDatabaseAccess(null, 2).execute();


                // setBlurBackdround();
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(music!=null) {
                   if (music.repeatTime == 2)
                       music.setRepeatTime(0);
                   else
                       music.repeatTime++;
               }
                Toast.makeText(getBaseContext(),"repeatValue ="+music.repeatTime,Toast.LENGTH_SHORT).show();
            }

        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //music.playprev();
                Intent i = new Intent(PlayerActivity.this,MusicService.class);
                i.setAction("prev");
                i.putExtra("id","aa");
                startService(i);
                //mediaEvents.onPrevPress();
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1,true);
                updateYourSelf();
                new RoomDatabaseAccess(null, 2).execute();
                // setBlurBackdround();

            }
        });

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (music.isplaying())
                    playPause.setImageResource(R.mipmap.playbutton);
                else
                    playPause.setImageResource(R.mipmap.pause);
                //music.play_pause();
                //mediaEvents.onPlayPausePress();
                Intent i = new Intent(PlayerActivity.this,MusicService.class);
                i.setAction("play/pause");
                i.putExtra("id","aa");
                startService(i);
            }
        });
        Log.d("is Service binded",mBound+"");
       // updateYouSelf();
    }



    public void setBlurBackdround()  /// CALL WHEN EVER SONG GETS CHANGED...
    {
        //   updatePlayerBar(bitmap);
                try {
                    data.setDataSource(getBaseContext(),music.Current_playing_song.getSonguri());
                    byte [] a = data.getEmbeddedPicture();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(a,0,a.length);
                    Blurry.with(getBaseContext()).radius(4)
                            .sampling(5).color(Color.argb(0, 255, 255, 0))
                            .async()
                            .animate(500).from(bitmap).into(background);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                            Palette.Swatch swatch2 = palette.getLightMutedSwatch();
                            if(swatch2!=null)
                            {

                            }
                            if (swatch == null)
                                swatch = palette.getDominantSwatch(); // Sometimes vibrant swatch is not available
                            if (swatch != null) {
                                // Set the background color of the player bar based on the swatch color
                                // Update the track's title with the proper title text color
                                // Update the artist name with the proper body text color
                                // albumname.setTextColor(palette.getDarkVibrantColor(Color.parseColor("#cccccc")));
                                //albumname.setTextColor(palette.getLightVibrantColor(Color.parseColor("#cccccc")));
                                //album.setTextColor(palette.getMutedColor(Color.WHITE));
                               // songTitle.setTextColor(palette.getLightVibrantColor(Color.parseColor("#cccccc")));
                                //constraintLayout.setBackground(initTitile(palette.getLightVibrantColor(Color.parseColor("#cccccc")),palette.getDominantColor(Color.parseColor("#cccccc"))));
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                  e.printStackTrace();
                }


              setBlurBackground = true;
    }

    public void updateViewPager()
    {
        Log.d("position from service",""+MusicService.positionOfCurrentSong);
        viewPager.setCurrentItem(MusicService.positionOfCurrentSong);
    }
    public void updateYourSelf()
    {
        //Log.d("title",music.getCurrentsong().gettitle()+"");
        vAlbum.setText(music.Current_playing_song.getalbum()+"");
        vSongtitle.setText(music.Current_playing_song.getartist()+"");
        vcurrentTime.setText(gettimeinformat(music.mediaPlayer.getCurrentPosition()));
        vlengthOfSong.setText(gettimeinformat(music.Current_playing_song.getDuration()));
        songTitle.setText(music.Current_playing_song.getTitle()+"                 ");
        songTitle.setSelected(true);
        if(music.mediaPlayer.isPlaying())
        {
            playPause.setImageResource(R.mipmap.pause);
        }
        else
        {
            playPause.setImageResource(R.mipmap.playbutton);
        }
        try {
            seekBar.setProgress((int) (music.mediaPlayer.getCurrentPosition() * 1000) / (int) music.Current_playing_song.getDuration());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            seekBar.setProgress(0);
        }
           // setBlurBackdround();

    }

    public Drawable initTitile(int start,int end)
    {
        Drawable d = getResources().getDrawable(R.drawable.pd2,null);
        ColorFilter filter = new LightingColorFilter(Color.parseColor("#00000000"),end);
        d.setColorFilter(filter);
        return d;
    }


    public String gettimeinformat(long a)
    {
        long second = (a / 1000) % 60;
        long minute = (a / (1000 * 60)) % 60;

        String time = String.format("%02d:%02d", minute, second);
        return time;

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            music  =  binder.getService();
            updateYourSelf();
            updateViewPager();
            mBound =true;
            music.setCallbacks(PlayerActivity.this);
            try {
                setBlurBackdround();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound =false;
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound)
        {
            music.setCallbacks(null);
            unbindService(serviceConnection);
        }
        Log.d("activity","Destroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
       // updateYouSelf();
    }

    @Override
    protected void onPause() {
        super.onPause();
       handler.removeCallbacks(runnable);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setBlurBackdround();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PlayerActivity.this.runOnUiThread(runnable);
        Log.d("in start ","true2");
            Log.d("in start ","true");
        Log.d("title",music.getCurrentsong().getTitle()+"");
        updateYourSelf();
        updateViewPager();
        songTitle.setSelected(true);
        try {
            new RoomDatabaseAccess(null, 2).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void update() {
         setBlurBackdround();
         updateViewPager();
        new RoomDatabaseAccess(null, 2).execute();

    }

    public static class RoomDatabaseAccess extends AsyncTask<Void, Void, Void> {
        SongEntity songEntity;
        int what;
        List<SongEntity> songEntities;
        boolean show;
        public RoomDatabaseAccess(@Nullable  SongEntity songEntity , int what) {
            super();
            this.songEntity = songEntity;
            this.what = what;
            this.show=false;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(what == 2)
            {
                if(!show)
                {
                    likeButton.setLiked(false);
                    Log.d("liked","false");
                }
                else {
                    likeButton.setLiked(true);
                    Log.d("liked","true");

                }

            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(what==0)
            {
                try {
                    db.userDao().insertAll(songEntity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else if(what==1)
            {
                db.userDao().delete(songEntity);
            }
            else
            {
               // songEntities = db.userDao().getSongWithUri(music.Current_playing_song.getSongId());
                songEntities = db.userDao().getAll();
                for(int i=0;i<songEntities.size();i++)
                {
                    Log.d("songs",""+songEntities.get(i).getSongName());
                    if(songEntities.get(i).getSongId().equals(music.Current_playing_song.getSongId()))
                    {
                        Log.d("song found",""+songEntities.get(i).getSongName());
                        show=true;
                        break;
                    }
                }
            }
            Log.d("fav size",""+db.userDao().getAll().size());

            return null;
        }
    }
}
