package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Fragments.AlbumFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.ArtistFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.TracksFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;
import com.developmentforfun.mdnafiskhan.mp3player.ViewpagerAnimation.ZoomOutPageTransformer;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.NavigationDrawerAdapter;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[] mPlanetTitles={"Tracks","Album","Artist"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    SharedPreferences sharedPreferences;
    Fragment [] fragments = {new TracksFragment(),new AlbumFragment(), new ArtistFragment()};
    PagerAdapter pagerAdapter;
       ViewPager viewPager;
       public static ImageView im;
       int pos= -1 ;
       public static Context context;
       MusicService musicService;
       boolean mBound;
       TabLayout tabLayout ;
       public static Uri currentsonguri;
       protected void onCreate(final Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           sharedPreferences = getSharedPreferences("lastplayed",Context.MODE_PRIVATE);

           Intent i = new Intent(MainActivity.this,MusicService.class);
           startService(i);
           mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
           mDrawerList = (ListView) findViewById(R.id.left_drawer);
           mDrawerList.setAdapter(new NavigationDrawerAdapter(this));
           mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   if(position == 3)
                   {
                       Intent i = new Intent(MainActivity.this,PlaylistActivity.class);
                        startActivity(i);
                   }
                   if(position == 4)
                   {
                     /*  Intent intent = new Intent();
                       intent.setAction("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
                       if((intent.resolveActivity(getPackageManager()) != null)) {
                           startActivity(intent);
                       } else {
                           // No equalizer found :(
                           Toast.makeText(getBaseContext(),"No Equaliser Found",Toast.LENGTH_LONG).show();
                       }*/
                       Intent i = new Intent(MainActivity.this,EqualiserAc.class);
                       startActivity(i);

                   }

               }
           });

           tabLayout = (TabLayout) findViewById(R.id.tablayout);
           viewPager = (ViewPager) findViewById(R.id.viewPager);
           tabLayout.setTabTextColors(Color.WHITE,Color.CYAN);
           tabLayout.setTabMode(TabLayout.MODE_FIXED);
           context = getBaseContext();
           pagerAdapter = new myfragment(getSupportFragmentManager());
           im  = (ImageView) findViewById(R.id.currentsong);
           im.setVisibility(View.VISIBLE);
                 // viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
                   viewPager.setAdapter(pagerAdapter);
                   tabLayout.setupWithViewPager(viewPager,true);

           SharedPreferences.Editor editor= sharedPreferences.edit();
           if(sharedPreferences.getInt("count",0)==0)
           {
               editor.putInt("count",1);
           }
           else
           {
               int c= sharedPreferences.getInt("count",0);
               Log.d("Uses count",c+"");
               editor.putInt("count",c++);
               editor.apply();
           }
           if(!sharedPreferences.getString("uri","").equals(""))
           {
              String s = sharedPreferences.getString("uri","");
               if(!s.isEmpty()) {
                   Uri u = Uri.parse(s);
                   currentsonguri = u;
                   MediaMetadataRetriever data = new MediaMetadataRetriever();
                   try {
                       data.setDataSource(MainActivity.this, u);
                       byte[] b = data.getEmbeddedPicture();
                       Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                       bitmap = getRoundedCornerBitmap(bitmap);
                       im.setImageBitmap(bitmap);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

                   try {
                       musicService.setsongbyuri(u, getBaseContext());
                       musicService.setMediaPlayer();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }
           else
           {

           }
           editor.apply();
           im.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
              intent.setData(currentsonguri);
              intent.putExtra("flag",1);
              ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, im, "zoom");
              startActivity(intent,options.toBundle());
          }
      });



           final Uri r= getIntent().getData();
              if(r!=null) {
                  currentsonguri = r;
                  Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                  intent.setData(r);
                  intent.putExtra("flag",0);
                  startActivity(intent);

              }
           }

    public class myfragment extends FragmentPagerAdapter {

        myfragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String s = "non";
            switch (position)
            {
                case 0 : s= "Tracks" ;
                    break;
                case 1: s= "Albums" ;
                    break;
                case 2: s= "Artist" ;
                    break;
            }
            return s;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void setview(byte [] b, int position,Uri uri)
    {
        currentsonguri = uri;
        Bitmap bitmap;
        Log.d("position in set view",""+position);
        Log.d("fail","i am here");
        if(im!=null)
        {
            if(b!=null)
            {
                 bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                im.setImageBitmap(bitmap);
            }
            else {
                songDetailloader loader = new songDetailloader(context);
                String s = loader.albumartwithalbum(loader.songalbum(position));
                bitmap = BitmapFactory.decodeFile(s);
            }
            try {
                bitmap = getRoundedCornerBitmap(bitmap);
            }
            catch (Exception e)
            {
                Log.d("bitmap error","round bitmap is null");
            }
                if (bitmap != null) {
                    im.setImageBitmap(bitmap);
                } else {
                    im.setImageResource(R.drawable.default_track_light);
                    Log.d("ic","ic_launcher setted");
                }
            }
    }



    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 100;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
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
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","Get distoryed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);

    }
}
