package com.developmentforfun.mdnafiskhan.mp3player.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.MusicPlayerActivity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.MainActivity;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;

import java.util.ArrayList;


public class MusicService extends Service implements MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener ,AudioManager.OnAudioFocusChangeListener {


    Uri currentSonguri;
    static int song_position_in_arraylist ;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    AudioManager mAudioManager;
    ComponentName mRemoteControlResponder;
    MusicPlayerActivity musicPlayerActivity = new MusicPlayerActivity();
    MainActivity main =new MainActivity();
    ArrayList<Songs> albumsonglist = new ArrayList<>();
    ArrayList<Uri> listofsongs = new ArrayList<>();
    ArrayList<Songs> priorityqueue = new ArrayList<>();
    Intent intent;
    NotificationManager notificationManager;
    RemoteViews smallView ,largeView;
    private final IBinder mBinder = new LocalBinder();
    public Songs Current_playing_song = new Songs();
    Context context;
    songDetailloader loader ;
    int serviceisrunnig =0;
    public int queuePosition = 0;
    public static MediaMetadataRetriever data = new MediaMetadataRetriever();

    /***/ MediaSessionCompat mediaSession ;
    /***/public static Equalizer equalizer ;
         public static BassBoost bassBoost ;
         public static Virtualizer virtualizer ;

    //********************************************************************************************************************************************************
    //new costans   ;
    int positionOfCurrentSong ;
    public ArrayList<Songs> songs_current_playlist = new ArrayList<>();
    public ArrayList<Songs> prioritysonglist = new ArrayList<>();



    //*********************************************************************************************************************************************************

    public class LocalBinder extends Binder {
       public MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
    //    if(mediaPlayer!=null)
      //       mediaPlayer.stop();
        Log.d("service is binded","to activity or fragment");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public MusicService() {

        super();
    }

   public void cancelnotification()
   {
       notificationManager.cancel(1);
   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = "";
        Log.d("","service is started");
        if(intent != null)
         action = intent.getAction();
        Log.d("action is :",action+"");
        if(action != null) {
            if (!action.isEmpty()) {
                if (action == "play/pause") {
                    Log.d("action", "play.pause");
                    play_pause();
                }
                if (action == "prev") {
                    Log.d("action", "prev");
                    playprev();
                }
                if (action == "next") {
                    Log.d("action", "next");
                    playnext();
                }
            }
        }
        else
        {
            if(mediaPlayer!=null)
            {
                mediaPlayer.start();
            }
        }

        //media sesion compat ;
        {

        }


        Log.d("running activity",""+musicPlayerActivity.isactivityisrenning);
        if(musicPlayerActivity.isactivityisrenning)
        musicPlayerActivity.updateyourself(Current_playing_song);
        updatenotification();
        return START_STICKY ;

    }
//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    //new function...


    //setting the list ......
    public void setplaylist(ArrayList<Songs> songslist, int position)
    {
        this.songs_current_playlist = songslist ;
        this.positionOfCurrentSong = position;
        this.Current_playing_song  = songslist.get(position);
    }

   public Songs getCurrentsong()
   {
       return this.Current_playing_song;
   }


    public void updatedatabase()
    {
        DataBaseClass d= new DataBaseClass(getBaseContext());
        d.insetintomostplayed(this.Current_playing_song);
    }




//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    @Override
    public void onCreate() {
        Log.d("","Service created");
        context = this;
        loader = new songDetailloader(getBaseContext());
        serviceisrunnig =1;
        super.onCreate();


    }

    public boolean isplaying()
    {
     if(mediaPlayer!=null)
     return mediaPlayer.isPlaying();
        else return false;

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Toast.makeText(getBaseContext(),"Service is destoryed",Toast.LENGTH_SHORT).show();
        serviceisrunnig = 0;

    }

    public void setonlyonesong(Songs s ,Context c)
    {
        Current_playing_song = s;
        mediaPlayer =MediaPlayer.create(c,s.getSonguri());
        mediaPlayer.start();
    }

    public void setsongbyuri(Uri u,Context c)
    {
        Current_playing_song = loader.sonngwithuri(u.toString());

    }

   public void setMediaPlayer()
   {
       if(mediaSession!=null) {
           if (mediaSession.isActive()) {
               mediaSession.release();
           }
       }
       setCLient();
       byte b[] ;
       if(mediaPlayer!=null)
          {
              mediaPlayer.stop();
          }
       Log.d("songtoplayposition",song_position_in_arraylist+"");
      try {
          mediaPlayer = MediaPlayer.create(this, Current_playing_song.getSonguri());
          equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
          equalizer.setEnabled(true);
          bassBoost = new BassBoost(0,mediaPlayer.getAudioSessionId());
          bassBoost.setEnabled(true);
          virtualizer = new Virtualizer(0,mediaPlayer.getAudioSessionId());
          virtualizer.setEnabled(true);
          BassBoost.Settings bassBoostSetting = new BassBoost.Settings();
          bassBoostSetting.strength= 1000;
          Virtualizer.Settings virtualiserSetting = new Virtualizer.Settings();
          virtualiserSetting.strength = 1000;
          showlevel();
      //    Log.d("...", equalizer.getNumberOfBands() + "and 2->" + equalizer.getBandLevel((short) 2));
     //
     //     equalizer.setBandLevel((short) 2, (short) (2 * equalizer.getCenterFreq((short) 2)));
      //    Log.d("...", equalizer.getNumberOfBands() + "and 2->" + equalizer.getBandLevel((short) 2));
//
          start();
          Log.d("", "1");
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }



       try {
           data.setDataSource(context,Current_playing_song.getSonguri());
           b = data.getEmbeddedPicture();
       }
       catch(Exception e) {
           b=null;
              }
       setnotification();
       //this method set the floating imageview
       Log.d("Send position",""+Current_playing_song.getPosition());
       main.setview(b,Current_playing_song.getPosition(),Current_playing_song.getSonguri());
       SharedPreferences sharedPreferences =getBaseContext().getSharedPreferences("lastplayed",Context.MODE_PRIVATE);
       SharedPreferences.Editor editor= sharedPreferences.edit();
       editor.putString("uri",Current_playing_song.getSonguri().toString());
       editor.apply();
       SharedPreferences sharedPreferences2 =getBaseContext().getSharedPreferences("eq",Context.MODE_PRIVATE);
       SharedPreferences.Editor editor2= sharedPreferences2.edit();
       editor2.putInt("band0",equalizer.getBandLevel((short)0));
       editor2.putInt("band1",equalizer.getBandLevel((short)1));
       editor2.putInt("band2",equalizer.getBandLevel((short)2));
       editor2.putInt("band3",equalizer.getBandLevel((short)3));
       editor2.putInt("band4",equalizer.getBandLevel((short)4));
       editor2.apply();
       Log.d("this","broadcast send");
       if(mediaPlayer != null) {
           if(mediaPlayer!= null) {
               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                   @Override
                   public void onCompletion(MediaPlayer mp) {
                       Log.d("complet", "Listner get called");
                       equalizer.setEnabled(false);
                       if(!mediaPlayer.isPlaying())
                       playnext();

                   }
               });
               mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                   @Override
                   public void onPrepared(MediaPlayer mp) {
                       mediaPlayer.start();
                       updatenotification();
                       if (musicPlayerActivity.isactivityisrenning)
                           musicPlayerActivity.updateyourself(Current_playing_song);
                       Log.d("media player","prepared");
                   }
               });
               mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                   @Override
                   public boolean onError(MediaPlayer mp, int what, int extra) {
                       mediaPlayer.stop();
                       return true;
                   }
               });
           }

       }
       else
       {
           playnext();
       }
       songDetailloader loader = new songDetailloader(context);
       String s = loader.albumartwithalbum(Current_playing_song.getalbum());
       Current_playing_song.setAlbumart(s);
       updatedatabase();
   }

   public void bass(short progress)
   {
       bassBoost.setStrength(progress);
       Log.d("bass new level"," "+bassBoost.getProperties().strength);
   }
    public void vir(short progress)
    {
        virtualizer.setStrength(progress);
        Log.d("vir new level"," "+virtualizer.getProperties().strength);
    }
    //function for equaliser settings...
    public void eq(short bandNo , short precentage )
    {
      int min = equalizer.getBandLevelRange()[0];
      int max = equalizer.getBandLevelRange()[1];
         // short b = (short)((precentage/100)*max);
       equalizer.setBandLevel(bandNo, precentage);
        Log.d("percentage"," "+precentage);
       Log.d("New level of band "+bandNo," is "+equalizer.getBandLevel(bandNo));
    }
   public void showlevel()
   {
       Log.d("Current preset",""+equalizer.getCurrentPreset());
       Log.d("Most affected band 450",""+equalizer.getBand(1800000));
       Log.d("No of bands",""+equalizer.getNumberOfBands());
       Log.d("freq range of band 0",""+equalizer.getBandFreqRange((short)0)[0]+"   "+equalizer.getBandFreqRange((short)0)[1]);
       Log.d("freq range of band 1",""+equalizer.getBandFreqRange((short)1)[0]+"   "+equalizer.getBandFreqRange((short)1)[1]);
       Log.d("freq range of band 2",""+equalizer.getBandFreqRange((short)2)[0]+"   "+equalizer.getBandFreqRange((short)2)[1]);
       Log.d("freq range of band 3",""+equalizer.getBandFreqRange((short)3)[0]+"   "+equalizer.getBandFreqRange((short)3)[1]);
       Log.d("freq range of band 4",""+equalizer.getBandFreqRange((short)4)[0]+"   "+equalizer.getBandFreqRange((short)4)[1]);
       Log.d("Band level",""+equalizer.getBandLevel((short)0)+" "+equalizer.getBandLevel((short)1)+" "+equalizer.getBandLevel((short)2)+" "+equalizer.getBandLevel((short)3)+" "+equalizer.getBandLevel((short)4)+"");
       Log.d("Band level range",""+equalizer.getBandLevelRange()[0]+"   "+equalizer.getBandLevelRange()[1]);

   }

    public ArrayList<Short> getCurrMinMax()
    {
        ArrayList<Short> a = new ArrayList<>();
        a.add(equalizer.getBandLevel((short)0));
        a.add(equalizer.getBandLevel((short)1));
        a.add(equalizer.getBandLevel((short)2));
        a.add(equalizer.getBandLevel((short)3));
        a.add(equalizer.getBandLevel((short)4));
        a.add(equalizer.getBandLevelRange()[0]);
        a.add(equalizer.getBandLevelRange()[1]);
        return a;
    }


    public void setCLient()
    {
        MediaMetadataRetriever meta = new MediaMetadataRetriever();
        Log.d("current song","of client is "+Current_playing_song.gettitle());
    try {
        meta.setDataSource(getBaseContext(), Current_playing_song.getSonguri());
    }
    catch (Exception e)
    {
        e.printStackTrace();
        Toast.makeText(getBaseContext(),"Error in playing song",Toast.LENGTH_SHORT).show();
        playnext();
    }
        byte [] b = meta.getEmbeddedPicture();
        Bitmap bitmap;
        if(b!=null)
        {
            bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
        }
        else
        {
            bitmap  = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher,null) ;
        }
        ComponentName receiver = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());
        mediaSession = new MediaSessionCompat(this, "MusicService", receiver, null);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0)
                .setActions(  PlaybackStateCompat.ACTION_SEEK_TO |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_STOP)
                .build());
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST,Current_playing_song.getartist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM,Current_playing_song.getalbum())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE,Current_playing_song.gettitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,Current_playing_song.getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ART, bitmap)
                .build());

        mediaSession.setActive(true);
    }

    public void start()
    {
        if(mediaPlayer!=null) {
            if (!mediaPlayer.isPlaying()) {
                if (requestAudioFocusForMyApp(context)) {
                    //updatenotification();
                    mediaPlayer.start();

                }
            }
        }

    }


    public int getcurrentpositionofsongtime()
    {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekmediaplayer( int time)
    {
        mediaPlayer.seekTo(time);
    }

   /*   @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("complet","Listner egt called");
        mediaPlayer = null;
        updatenotification();
        Current_playing_song =loader.nexttracks(song_position_in_arraylist);
        setMediaPlayer(this.Current_playing_song);
        musicPlayerActivity.updateyourself(this.Current_playing_song);

    }

    */


    public void playnext()
    {

        if(priorityqueue.size() == 0 || queuePosition == priorityqueue.size()) {
            if (positionOfCurrentSong < (songs_current_playlist.size() - 1)) {
                Current_playing_song = songs_current_playlist.get(positionOfCurrentSong + 1);
                positionOfCurrentSong++;
            } else {
                Current_playing_song = songs_current_playlist.get(0);
                positionOfCurrentSong = 0;
            }

        }
        else
        {
            Current_playing_song = priorityqueue.get(queuePosition);
            queuePosition++;
        }
        setMediaPlayer();
        if (musicPlayerActivity.isactivityisrenning)
            musicPlayerActivity.updateyourself(this.Current_playing_song);
    }


    public void insertinqueue(Songs s)
    {
        priorityqueue.add(s);
        Log.d("song add ",""+priorityqueue.get(queuePosition).gettitle());
    }


    public void playprev()
    {
        if(positionOfCurrentSong >0)
        {
            Current_playing_song = songs_current_playlist.get(positionOfCurrentSong - 1);
            positionOfCurrentSong--;
        }
        else
        {
            Current_playing_song = songs_current_playlist.get(songs_current_playlist.size()-1);
            positionOfCurrentSong =songs_current_playlist.size()-1;
        }

        setMediaPlayer();
        if(musicPlayerActivity.isactivityisrenning)
            musicPlayerActivity.updateyourself(Current_playing_song);
    }
    public boolean isserviceisrunnig()
    {
        if(serviceisrunnig == 0)
              return false;
        else
              return true;
    }

   public void setnotification()
    {
       byte b[] =null;
        context=this;
        Bitmap image = null;
        Intent play = new Intent(getBaseContext(),MusicService.class);
        play.setAction("play/pause");
        PendingIntent play_p = PendingIntent.getService(this,0,play,0);
        Intent prev = new Intent(getBaseContext(),MusicService.class);
        prev.setAction("prev");
        PendingIntent prev_p = PendingIntent.getService(this,0,prev,0);
        Intent next = new Intent(getBaseContext(),MusicService.class);
        next.setAction("next");
        PendingIntent next_p = PendingIntent.getService(this,0,next,0);
        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =new  NotificationCompat.Builder(this);
        largeView=new RemoteViews(getPackageName(), R.layout.largenotification);
        smallView=new RemoteViews(getPackageName(),R.layout.smallnotification);
        smallView.setTextViewText(R.id.smallsong,Current_playing_song.gettitle());
        smallView.setTextViewText(R.id.smallalbum,Current_playing_song.getalbum());
        smallView.setImageViewResource(R.id.smallprev,android.R.drawable.ic_media_previous);
        smallView.setImageViewResource(R.id.smallprev,android.R.drawable.ic_media_previous);
        smallView.setImageViewResource(R.id.smallplay,android.R.drawable.ic_media_pause);
        smallView.setImageViewResource(R.id.smallnext,android.R.drawable.ic_media_next);
        largeView.setImageViewResource(R.id.imageButton8,android.R.drawable.ic_media_pause);
        largeView.setImageViewResource(R.id.imageButton9,android.R.drawable.ic_media_next);
        largeView.setImageViewResource(R.id.imageButton7,android.R.drawable.ic_media_previous);
        largeView.setTextViewText(R.id.textView3,Current_playing_song.getartist());
        largeView.setTextViewText(R.id.textView2,Current_playing_song.gettitle());
        largeView.setOnClickPendingIntent(R.id.imageButton8,play_p);
        if(positionOfCurrentSong < songs_current_playlist.size()-1)
        largeView.setTextViewText(R.id.nextsong,"Next : " +songs_current_playlist.get(positionOfCurrentSong+1).gettitle());
        else
            largeView.setTextViewText(R.id.nextsong,"Next : " );
        largeView.setTextViewText(R.id.currentno,positionOfCurrentSong+1+"/"+songs_current_playlist.size());
        smallView.setOnClickPendingIntent(R.id.smallplay,play_p);
        largeView.setOnClickPendingIntent(R.id.imageButton7,prev_p);
        largeView.setOnClickPendingIntent(R.id.imageButton9,next_p);
        smallView.setOnClickPendingIntent(R.id.smallprev,prev_p);
        smallView.setOnClickPendingIntent(R.id.smallnext,next_p);

        try{
            b = data.getEmbeddedPicture();
            image = BitmapFactory.decodeByteArray(b,0,b.length);
        }
        catch (Exception e)
        {
            String s =loader.albumartwithalbum(Current_playing_song.getalbum());
            if(s!=null)
                image =BitmapFactory.decodeFile(s);
            else
            {
              b=null;
            }
            e.printStackTrace();
        }
        if(b!=null)
        {
            smallView.setImageViewBitmap(R.id.smallicon,image);
            largeView.setImageViewBitmap(R.id.icon,image);

        }
        else
        {
            smallView.setImageViewResource(R.id.smallicon,R.drawable.default_track_light);
            largeView.setImageViewResource(R.id.icon,R.drawable.default_track_light);
        }
        builder.setLargeIcon(image);
        builder.setSmallIcon(R.drawable.default_track_light);
        builder.setColor(Color.parseColor("#00000f"));
        builder.setContentTitle("Music Player");
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setContentText(data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        builder.setCustomContentView(smallView);
        builder.setCustomBigContentView(largeView);
        if(mediaPlayer.isPlaying())
        {
            builder.setAutoCancel(true);
            builder.setOngoing(true);
        }
        else {
            builder.setAutoCancel(false);
            builder.setOngoing(false);
        }
        notificationManager.notify(1, builder.build());
        // startForeground(1,builder.build());

    }



      // this function is to update notification.......

    public void updatenotification()
    {

        byte b[] =null;
        context=this;
        Bitmap image = null;
        Intent play = new Intent(getBaseContext(),MusicService.class);
        play.setAction("play/pause");
        PendingIntent play_p = PendingIntent.getService(this,0,play,0);
        Intent prev = new Intent(getBaseContext(),MusicService.class);
        prev.setAction("prev");
        PendingIntent prev_p = PendingIntent.getService(this,0,prev,0);
        Intent next = new Intent(getBaseContext(),MusicService.class);
        next.setAction("next");
        PendingIntent next_p = PendingIntent.getService(this,0,next,0);
        // Intent n = new Intent(getBaseContext(),next.class);
      //  Intent p = new Intent(getBaseContext(),prev.class);
      //  PendingIntent play_next = PendingIntent.getService(getBaseContext(),0,n,0);
      //  PendingIntent play_prev = PendingIntent.getService(getBaseContext(),0,p,0);
        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =new  NotificationCompat.Builder(this);
        largeView=new RemoteViews(getPackageName(), R.layout.largenotification);
        smallView=new RemoteViews(getPackageName(),R.layout.smallnotification);
        smallView.setTextViewText(R.id.smallsong,Current_playing_song.gettitle());
        smallView.setTextViewText(R.id.smallalbum,Current_playing_song.getalbum());
        smallView.setImageViewResource(R.id.smallprev,android.R.drawable.ic_media_previous);
        smallView.setImageViewResource(R.id.smallprev,android.R.drawable.ic_media_previous);
        if(mediaPlayer == null || !mediaPlayer.isPlaying())
        {
            smallView.setImageViewResource(R.id.smallplay,android.R.drawable.ic_media_play);
            largeView.setImageViewResource(R.id.imageButton8,android.R.drawable.ic_media_play);
        }
        else {
            if(mediaPlayer.isPlaying())
            {
                smallView.setImageViewResource(R.id.smallplay,android.R.drawable.ic_media_pause);
                largeView.setImageViewResource(R.id.imageButton8,android.R.drawable.ic_media_pause);
            }
        }
     //  smallView.setOnClickPendingIntent(R.id.smallprev,play_prev);
      //  smallView.setOnClickPendingIntent(R.id.smallnext,play_next);
      //  largeView.setOnClickPendingIntent(R.id.imageButton7,play_prev);
      //  largeView.setOnClickPendingIntent(R.id.imageButton9,play_next);
        smallView.setImageViewResource(R.id.smallnext,android.R.drawable.ic_media_next);
        largeView.setImageViewResource(R.id.imageButton9,android.R.drawable.ic_media_next);
        largeView.setImageViewResource(R.id.imageButton7,android.R.drawable.ic_media_previous);
        largeView.setTextViewText(R.id.textView3,Current_playing_song.getartist());
        largeView.setTextViewText(R.id.textView2,Current_playing_song.gettitle());
        largeView.setOnClickPendingIntent(R.id.imageButton8,play_p);
        if(positionOfCurrentSong < songs_current_playlist.size()-1)
            largeView.setTextViewText(R.id.nextsong,"Next : " +songs_current_playlist.get(positionOfCurrentSong+1).gettitle());
        else
            largeView.setTextViewText(R.id.nextsong,"Next : " );
        largeView.setOnClickPendingIntent(R.id.imageButton7,prev_p);
        largeView.setOnClickPendingIntent(R.id.imageButton9,next_p);
        smallView.setOnClickPendingIntent(R.id.smallprev,prev_p);
        smallView.setOnClickPendingIntent(R.id.smallnext,next_p);
        largeView.setTextViewText(R.id.currentno,positionOfCurrentSong+1+"/"+songs_current_playlist.size()+" ");
        smallView.setOnClickPendingIntent(R.id.smallplay,play_p);

        try{
            b = data.getEmbeddedPicture();
            image = BitmapFactory.decodeByteArray(b,0,b.length);
        }
        catch (Exception e)
        {
            String s =loader.albumartwithalbum(Current_playing_song.getalbum());
            if(s!=null)
                image =BitmapFactory.decodeFile(s);
            else
            {
                b=null;
            }
            e.printStackTrace();
        }
        if(b!=null)
        {
            smallView.setImageViewBitmap(R.id.smallicon,image);
            largeView.setImageViewBitmap(R.id.icon,image);

        }
        else
        {
            smallView.setImageViewResource(R.id.smallicon,R.drawable.default_track_light);
            largeView.setImageViewResource(R.id.icon,R.drawable.default_track_light);
        }

        builder.setLargeIcon(image);
        builder.setSmallIcon(R.drawable.default_track_light);
        builder.setColor(Color.parseColor("#00000f"));
        builder.setContentTitle("Music Player");
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setContentText(data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        builder.setCustomContentView(smallView);
        builder.setCustomBigContentView(largeView);
        if(mediaPlayer.isPlaying())
       {
        builder.setAutoCancel(true);
        builder.setOngoing(true);
       }
        else {
        builder.setAutoCancel(false);
        builder.setOngoing(false);
         }

        notificationManager.notify(1, builder.build());
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }



    @Override
    public void onPrepared(MediaPlayer mp) {

    }



    public void pause()
    {
       if(mediaPlayer != null)
       {
           if(mediaPlayer.isPlaying())
               mediaPlayer.pause();

       }



    }

    public void play_pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
            updatenotification();

        }
    }




    public void stop()
    {
       if(mediaPlayer  != null)
       {
           if(mediaPlayer.isPlaying())
           {
               mediaPlayer.pause();
               mediaPlayer.stop();
               mediaPlayer.release();

           }
           else
           {
               mediaPlayer.stop();
               mediaPlayer.release();
           }

       }


    }




    @Override
    public void onAudioFocusChange(int focusChange) {

        Log.d("audio","Audio focus change");
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback

                if (!mediaPlayer.isPlaying()) {
                   //  mediaPlayer.start();
                     updatenotification();
                }
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                Log.d("audio","AUDIOFOCUS_LOSS");
                if (mediaPlayer.isPlaying()) {
                      pause();
                      updatenotification();
                }
                // song.release();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                Log.d("audio","AUDIOFOCUS_LOSS_TRANSIENT");
                // is likely to resume
                if (mediaPlayer.isPlaying()) {
                      pause();
                      updatenotification();
                }

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                Log.d("audio","AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                  mediaPlayer.setVolume(0.1f,0.1f);

                break;
        }


    }



    private boolean requestAudioFocusForMyApp(final Context context) {
        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
       // am.registerMediaButtonEventReceiver(new ComponentName(this, MediaButtonReceiver.class));

        // Request audio focus for playback
        int result = am.requestAudioFocus(this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d("AudioFocus", "Audio focus received");
            return true;
        } else {
            Log.d("AudioFocus", "Audio focus NOT received");
            return false;
        }
    }



}


