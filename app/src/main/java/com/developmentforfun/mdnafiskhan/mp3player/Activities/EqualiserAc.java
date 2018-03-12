package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.CustomView.Equaliser;
import com.developmentforfun.mdnafiskhan.mp3player.CustomView.EqualizerSeekbar;
import com.developmentforfun.mdnafiskhan.mp3player.CustomView.VerticalSeekBar;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.Switch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.beppi.knoblibrary.Knob;

/**
 * Created by mdnafiskhan on 14-03-2017.
 */

public class EqualiserAc extends Activity {
    MusicService musicService = new MusicService();
    boolean mBound;
    EqualizerSeekbar band0,band1, band2, band3, band4, bass, vir;
    short cband0, cband1, cband2, cband3, cband4;
    short max, min;
    int diff;
    int now;
    DataBaseClass dataBaseClass;
    AppCompatSpinner appCompatSpinner;
    Switch aSwitch;
    Knob knob, knob2;
    TextView tband0,tband1,tband2,tband3,tband4;
    TextView db0,db1,db2,db3,db4;
    ArrayList<Integer> normal = new ArrayList<>();
    ArrayList<Integer> Classical = new ArrayList<>();
    ArrayList<Integer> Dance = new ArrayList<>();
    ArrayList<Integer> Flat = new ArrayList<>();
    ArrayList<Integer> Folk = new ArrayList<>();
    ArrayList<Integer> Heavy_Metal = new ArrayList<>();
    ArrayList<Integer> Hip_hop = new ArrayList<>();
    ArrayList<Integer> Jazz = new ArrayList<>();
    ArrayList<Integer> pop = new ArrayList<>();
    ArrayList<Integer> rock = new ArrayList<>();
    int curr=0;
    boolean press = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_layout);
        dataBaseClass = new DataBaseClass(getBaseContext());
        Intent i = new Intent(EqualiserAc.this, MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        appCompatSpinner = (AppCompatSpinner) findViewById(R.id.spinner);
        aSwitch = (Switch) findViewById(R.id.switch1);
        band0 = (EqualizerSeekbar) findViewById(R.id.band0);
        band1 = (EqualizerSeekbar) findViewById(R.id.band1);
        band2 = (EqualizerSeekbar) findViewById(R.id.band2);
        band3 = (EqualizerSeekbar) findViewById(R.id.band3);
        band4 = (EqualizerSeekbar) findViewById(R.id.band4);
        tband4 = (TextView) findViewById(R.id.tband4);
        tband3 = (TextView) findViewById(R.id.tband3);
        tband2 = (TextView) findViewById(R.id.tband2);
        tband1 = (TextView) findViewById(R.id.tband1);
        tband0 = (TextView) findViewById(R.id.tband0);
        db4 = (TextView) findViewById(R.id.db4);
        db3 = (TextView) findViewById(R.id.db3);
        db2 = (TextView) findViewById(R.id.db2);
        db1 = (TextView) findViewById(R.id.db1);
        db0 = (TextView) findViewById(R.id.db0);
        knob = (Knob) findViewById(R.id.knob);
        knob2 = (Knob) findViewById(R.id.knob2);
        List<String> list = new ArrayList<String>();
        list.add("Custom");
        list.add("Normal");
        list.add("Classical");
        list.add("Dance");
        list.add("Flat");
        list.add("Folk");
        list.add("Heavy Metal");
        list.add("Hip Hop");
        list.add("Jazz");
        list.add("Pop");
        list.add("Rock");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.textviewtwo);
        appCompatSpinner.setAdapter(dataAdapter);
        appCompatSpinner.getBackground().setColorFilter(Color.parseColor("#AAAAAA"), PorterDuff.Mode.SRC_ATOP);
        set();
        //  bass = (SeekBar) findViewById(R.id.bass);
        //  vir = (SeekBar) findViewById(R.id.vir);
        band0.setMax(1000);
        band1.setMax(1000);
        band2.setMax(1000);
        band3.setMax(1000);
        band4.setMax(1000);
        band0.setEnabled(true);
        band1.setEnabled(true);
        band2.setEnabled(true);
        band3.setEnabled(true);
        band4.setEnabled(true);
        band0.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
        band1.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
        band2.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
        band3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
        band4.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
        knob.setMaxAngle(330f);
        knob.setMinAngle(30f);
        knob2.setMaxAngle(330f);
        knob2.setMinAngle(30f);
        appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0)
                    new save().execute();
                switch(i)
                {
                    case 0 :
                        if(curr==0) {
                            ArrayList<Integer> a = new ArrayList<>();
                            a = dataBaseClass.getBands();
                            init(a);
                        }
                        break;
                    case 1 :
                        curr=1;
                        init(normal);
                        break;
                    case 2 :
                        curr=2;
                        init(Classical);
                        break;
                    case 3 :
                        curr=3;
                        init(Dance);
                        break;
                    case 4 :
                        curr=4;
                        init(Flat);
                        break;
                    case 5 :
                        curr=5;
                        init(Folk);
                        break;
                    case 6 :
                        curr=6;
                        init(Heavy_Metal);
                        break;
                    case 7 :
                        curr=7;
                        init(Hip_hop);
                        break;
                    case 8 :
                        curr=8;
                        init(Jazz);
                        break;
                    case 9 :
                        curr=9;
                        init(pop);
                        break;
                    case 10 :
                        curr=10;
                        init(rock);
                        break;
                }
                press = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        knob.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                short a = (short) (state * 40);
                Log.d("state", "" + state * 40);
                musicService.bass(a);
            }
        });
        knob2.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int i) {
                short a = (short) (i * 40);
                Log.d("state", "" + i * 40);
                musicService.vir(a);
            }
        });
        band0.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                cband0 = d;
                Log.d("d", "" + d);
                //   editor.putFloat("band0",(d-1500));
                //  editor.putInt("band0",d-1500);
                db0.setText((d-1500)/100+"db");
                musicService.eq((short) 0, (short) (d - 1500));
                //  editor.apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("start","true");
                appCompatSpinner.setSelection(0);
                press = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("stop","true");


            }

        });

        band1.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                cband1 = d;
                Log.d("d", "" + d);
                db1.setText((d-1500)/100+"db");
                // editor.putInt("band1",d-1500);
                musicService.eq((short) 1, (short) (d - 1500));
                //   editor.apply();
//
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                appCompatSpinner.setSelection(0);                //   editor.apply();
                press = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


        band2.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                cband2 = d;
                Log.d("d", "" + d);
                db2.setText((d-1500)/100+"db");
                //  editor.putInt("band2",d-1500);
                musicService.eq((short) 2, (short) (d - 1500));
                              //  editor.apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                appCompatSpinner.setSelection(0);
                press = true;
//   editor.apply();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


        band3.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                Log.d("d", "" + d);
                cband3 = d;
                db3.setText((d-1500)/100+"db");
                // editor.putInt("band3",d-1500);
                //editor.apply();
                musicService.eq((short) 3, (short) (d - 1500));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                appCompatSpinner.setSelection(0);
                press = true;
//   editor.apply();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


        band4.setOnSeekBarChangeListener(new VerticalSeekBar(getBaseContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                Log.d("d", "" + d);
                cband4 = d;
                // editor.putInt("band4",d-1500);
                db4.setText((d-1500)/100+"db");
                musicService.eq((short) 4, (short) (d - 1500));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                appCompatSpinner.setSelection(0);
                press = true;
//   editor.apply();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
        aSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                ArrayList<Integer> a = new ArrayList<>();
                a = dataBaseClass.getBands();
                if (checked) {

                    band0.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
                    band1.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
                    band2.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
                    band3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
                    band4.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.equaliser_bars, null));
                    band0.setEnabled(true);
                    band1.setEnabled(true);
                    band2.setEnabled(true);
                    band3.setEnabled(true);
                    band4.setEnabled(true);
                    knob.setAlpha(1f);
                    knob2.setAlpha(1f);
                    knob.setKnobCenterColor(Color.parseColor("#0acece"));
                    knob.setEnabled(true);
                    knob2.setKnobCenterColor(Color.parseColor("#0acece"));
                    knob2.setEnabled(true);
                    init(a);
                } else {
                    band0.setEnabled(false);
                    band1.setEnabled(false);
                    band2.setEnabled(false);
                    band3.setEnabled(false);
                    band4.setEnabled(false);
                    band0.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black, null));
                    band1.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black, null));
                    band2.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black, null));
                    band3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black, null));
                    band4.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black, null));
                    new save().execute();
                    knob.setKnobCenterColor(Color.parseColor("#aaaaaa"));
                    knob.setEnabled(false);
                    knob2.setKnobCenterColor(Color.parseColor("#aaaaaa"));
                    knob2.setEnabled(false);
                    knob.setAlpha(0.6f);
                    knob2.setAlpha(0.6f);
                    musicService.eq((short) 0, (short) (0));
                    musicService.eq((short) 1, (short) (0));
                    musicService.eq((short) 2, (short) (0));
                    musicService.eq((short) 3, (short) (0));
                    musicService.eq((short) 4, (short) (0));
                }
            }
        });
    }

    void set()
    {
        normal.add(1800);
        normal.add(1500);
        normal.add(1500);
        normal.add(1500);
        normal.add(1800);
        Classical.add(2000);
        Classical.add(1800);
        Classical.add(1300);
        Classical.add(1900);
        Classical.add(1900);
        Dance.add(2100);
        Dance.add(1500);
        Dance.add(1700);
        Dance.add(1900);
        Dance.add(1600);
        Flat.add(1500);
        Flat.add(1500);
        Flat.add(1500);
        Flat.add(1500);
        Flat.add(1500);
        Folk.add(1800);
        Folk.add(1500);
        Folk.add(1500);
        Folk.add(1700);
        Folk.add(1400);
        Heavy_Metal.add(1900);
        Heavy_Metal.add(1600);
        Heavy_Metal.add(2400);
        Heavy_Metal.add(1800);
        Heavy_Metal.add(1500);
        Hip_hop.add(2000);
        Hip_hop.add(1800);
        Hip_hop.add(1500);
        Hip_hop.add(1600);
        Hip_hop.add(1800);
        Jazz.add(1900);
        Jazz.add(1700);
        Jazz.add(1300);
        Jazz.add(1700);
        Jazz.add(2000);
        pop.add(1400);
        pop.add(1700);
        pop.add(2000);
        pop.add(1600);
        pop.add(1300);
        rock.add(2000);
        rock.add(1800);
        rock.add(1400);
        rock.add(1800);
        rock.add(2000);
    }

    private void init(ArrayList<Integer> a) {

        min = 1500;
        int b1, b2, b3, b4;
        float b0;
        b0 = a.get(0);
        b1 = a.get(1);
        b2 = a.get(2);
        b3 = a.get(3);
        b4 = a.get(4);
        Log.d("values", "b0 = " + b0 + " b1 =" + b1 + " b2=" + b2 + " b3 = " + b3 + " b4 =" + b4);
        cband0 = (short) (b0);
        cband1 = (short) (b1);
        cband2 = (short) (b2);
        cband3 = (short) (b3);
        cband4 = (short) (b4);
        diff = 3000;

        Log.d("cband0 ", " is " + cband0/3);

        Log.d("cband1 ", " is " + cband1/3);

        Log.d("cband2 ", " is " + cband2/3);

        Log.d("cband3 ", " is " + cband3/3);

        Log.d("cband4 ", " is " + cband4/3);

        band0.setProgress(Math.round(cband0 / 3));
        band1.setProgress(Math.round(cband1 / 3));
        band2.setProgress(Math.round(cband2 / 3));
        band3.setProgress(Math.round(cband3 / 3));
        band4.setProgress(Math.round(cband4 / 3));
        musicService.eq((short) 0, (short) (cband0 - 1500));
        musicService.eq((short) 1, (short) (cband1 - 1500));
        musicService.eq((short) 2, (short) (cband2 - 1500));
        musicService.eq((short) 3, (short) (cband3 - 1500));
        musicService.eq((short) 4, (short) (cband4 - 1500));
        // band4.setProgress(50);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            mBound = true;
            tband0.setText(musicService.equalizer.getCenterFreq((short)0)/1000+"Hz");
            tband1.setText(musicService.equalizer.getCenterFreq((short)1)/1000+"Hz");
            tband2.setText(musicService.equalizer.getCenterFreq((short)2)/1000+"Hz");
            tband3.setText(musicService.equalizer.getCenterFreq((short)3)/1000+"Hz");
            tband4.setText(musicService.equalizer.getCenterFreq((short)4)/1000+"Hz");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound && serviceConnection != null) {
            unbindService(serviceConnection);
            mBound = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(EqualiserAc.this, MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            ArrayList<Integer> a = new ArrayList<>();
            a = dataBaseClass.getBands();
            init(a);
        } catch (Exception e) {
            e.printStackTrace();
            band0.setProgress(500);
            band1.setProgress(500);
            band2.setProgress(500);
            band3.setProgress(500);
            band4.setProgress(500);
            musicService.eq((short) 0, (short) 0);
            musicService.eq((short) 1, (short) 0);
            musicService.eq((short) 2, (short) 0);
            musicService.eq((short) 3, (short) 0);
            musicService.eq((short) 4, (short) 0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(EqualiserAc.this, MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("pause", "ture");
        new save().execute();
        if (mBound && serviceConnection != null) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }

    public class save extends AsyncTask<Void, Void, Void> {
        public save() {
            super();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataBaseClass.save(cband0, cband1, cband2, cband3, cband4);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            {

            }
        }
    }
}
