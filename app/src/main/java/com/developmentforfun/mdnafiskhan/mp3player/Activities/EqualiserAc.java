package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import com.developmentforfun.mdnafiskhan.mp3player.CustomView.Equaliser;
import com.developmentforfun.mdnafiskhan.mp3player.CustomView.VerticalSeekBar;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 14-03-2017.
 */

public class EqualiserAc extends Activity {
    MusicService musicService = new MusicService();
    boolean mBound;
    VerticalSeekBar band0,band1,band2,band3,band4,bass,vir ;
    short cband0,cband1,cband2,cband3,cband4;
    short max,min;
    int diff;
    int now ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eqi_activity);
        Intent i = new Intent(EqualiserAc.this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        band0 = (VerticalSeekBar) findViewById(R.id.band0);
        band1 = (VerticalSeekBar) findViewById(R.id.band1);
        band2 = (VerticalSeekBar) findViewById(R.id.band2);
        band3 = (VerticalSeekBar) findViewById(R.id.band3);
        band4 = (VerticalSeekBar) findViewById(R.id.band4);
      //  bass = (SeekBar) findViewById(R.id.bass);
      //  vir = (SeekBar) findViewById(R.id.vir);
        band0.setMax(1000);
        band1.setMax(1000);
        band2.setMax(1000);
        band3.setMax(1000);
        band4.setMax(1000);
     //   bass.setMax(1000);
      //  vir.setMax(1000);
        init();
        band0.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              SharedPreferences sharedPreferences = getSharedPreferences("eq",Context.MODE_PRIVATE);
              final SharedPreferences.Editor editor = sharedPreferences.edit();
                  Log.d("progress",""+progress);
                  short d =(short) (progress*3) ;
                  Log.d("d",""+d);
                  editor.putInt("band0",d-1500);
                  musicService.eq((short)0,(short) (d-1500));
              editor.commit();

          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {

          }

    });

        band1.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences sharedPreferences = getSharedPreferences("eq",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();

                    Log.d("progress",""+progress);
                    short d =(short) (progress*3) ;
                    Log.d("d",""+d);
                editor.putInt("band1",d-1500);
                musicService.eq((short)1,(short) (d-1500));
                editor.commit();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        band2.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences sharedPreferences = getSharedPreferences("eq",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                    Log.d("progress",""+progress);
                    short d =(short) (progress*3) ;
                    Log.d("d",""+d);
                    editor.putInt("band2",d-1500);
                musicService.eq((short)2,(short) (d-1500));
                editor.commit();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        band3.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext())  {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences sharedPreferences = getSharedPreferences("eq",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                    Log.d("progress",""+progress);
                    short d =(short) (progress*3) ;
                    Log.d("d",""+d);
                    editor.putInt("band3",d-1500);
                    editor.commit();
                musicService.eq((short)3,(short) (d-1500));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        band4.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences sharedPreferences = getSharedPreferences("eq",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                    Log.d("progress",""+progress);
                    short d =(short) (progress*3) ;
                    Log.d("d",""+d);
                    editor.putInt("band4",d-1500);
                musicService.eq((short)4,(short) (d-1500));
                editor.commit();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
       /* bass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                   musicService.bass((short) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        vir.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    musicService.vir((short) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }

    private void init()
    {

        min = -1500;
        int b0,b1,b2,b3,b4;
        SharedPreferences sharedPreferences = getSharedPreferences("eq",Context.MODE_PRIVATE);
        b0 = sharedPreferences.getInt("band0",0);
        b1 = sharedPreferences.getInt("band1",0);
        b2 = sharedPreferences.getInt("band2",0);
        b3 = sharedPreferences.getInt("band3",0);
        b4 = sharedPreferences.getInt("band4",0);
        cband0 = (short)(b0-min);
        cband1 =  (short)(b1-min);
        cband2 =  (short)(b2-min);
        cband3 =  (short)(b3-min);
        cband4 =  (short)(b4-min);
        diff= 3000;

        Log.d("cband0 "," is "+cband0);
        Log.d("band 0 set to",""+((float)cband0/diff)*1000);

        Log.d("cband1 "," is "+cband1);
        Log.d("band 1 set to",""+((float)cband1/diff)*1000);

        Log.d("cband2 "," is "+cband2);
        Log.d("band 2 set to",""+((float)cband2/diff)*1000);

        Log.d("cband3 "," is "+cband3);
        Log.d("band 3 set to",""+((float)cband3/diff)*1000);

        Log.d("cband4 "," is "+cband4);
        Log.d("band 4 set to",""+((float)cband4/diff)*1000);

        band0.setProgress(Math.round(((float)cband0/diff)*1000));
        band1.setProgress(Math.round(((float)cband1/diff)*1000));
        band2.setProgress(Math.round(((float)cband2/diff)*1000));
        band3.setProgress(Math.round(((float)cband3/diff)*1000));
        band4.setProgress(Math.round(((float)cband4/diff)*1000));
       // band4.setProgress(50);
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
        if(mBound && serviceConnection!=null) {
            unbindService(serviceConnection);
            mBound = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(EqualiserAc.this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(EqualiserAc.this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        init();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mBound && serviceConnection!=null) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }
}
