package com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.audiofx.Equalizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityViewModel.MainActivityViewModel;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.MainActivity;
import com.developmentforfun.mdnafiskhan.mp3player.CustomView.EqualizerSeekbar;
import com.developmentforfun.mdnafiskhan.mp3player.CustomView.VerticalSeekBar;
import com.developmentforfun.mdnafiskhan.mp3player.DataBase.DataBaseClass;
import com.developmentforfun.mdnafiskhan.mp3player.Models.EqualizerBands;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.AppDatabase;
import com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase.CloneEqualiser;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import it.beppi.knoblibrary.Knob;

/**
 * Created by mdnafiskhan on 14-03-2017.
 */

public class EqualiserAc extends Fragment {
    MusicService musicService = new MusicService();
    boolean mBound;
    EqualizerSeekbar band0,band1, band2, band3, band4, bass, vir;
    short cband0, cband1, cband2, cband3, cband4;
    short max, min;
    int diff;
    int now;
    AppCompatSpinner appCompatSpinner;
    Switch aSwitch;
    AppDatabase appDatabase;
    Knob knob, knob2;
    TextView tband0,tband1,tband2,tband3,tband4;
    TextView db0,db1,db2,db3,db4;
    ArrayList<Short> normal = new ArrayList<>();
    ArrayList<Short> Classical = new ArrayList<>();
    ArrayList<Short> Dance = new ArrayList<>();
    ArrayList<Short> Flat = new ArrayList<>();
    ArrayList<Short> Folk = new ArrayList<>();
    ArrayList<Short> Heavy_Metal = new ArrayList<>();
    ArrayList<Short> Hip_hop = new ArrayList<>();
    ArrayList<Short> Jazz = new ArrayList<>();
    ArrayList<Short> pop = new ArrayList<>();
    ArrayList<Short> rock = new ArrayList<>();
    int curr=0;
    boolean press = false;
    public boolean IsUserIsInteracting = false;
    public static EqualiserAc getInstance()
    {
        return new EqualiserAc();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         return inflater.inflate(R.layout.equalizer_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent i = new Intent(getActivity(), MusicService.class);
        getContext().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        appCompatSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        aSwitch = (Switch) view.findViewById(R.id.switch1);
        band0 = (EqualizerSeekbar) view.findViewById(R.id.band0);
        band1 = (EqualizerSeekbar) view.findViewById(R.id.band1);
        band2 = (EqualizerSeekbar) view.findViewById(R.id.band2);
        band3 = (EqualizerSeekbar) view.findViewById(R.id.band3);
        band4 = (EqualizerSeekbar) view.findViewById(R.id.band4);
        tband4 = (TextView) view.findViewById(R.id.tband4);
        tband3 = (TextView) view.findViewById(R.id.tband3);
        tband2 = (TextView) view.findViewById(R.id.tband2);
        tband1 = (TextView) view.findViewById(R.id.tband1);
        tband0 = (TextView) view.findViewById(R.id.tband0);
        db4 = (TextView) view.findViewById(R.id.db4);
        db3 = (TextView) view.findViewById(R.id.db3);
        db2 = (TextView) view.findViewById(R.id.db2);
        db1 = (TextView) view.findViewById(R.id.db1);
        db0 = (TextView) view.findViewById(R.id.db0);
        knob = (Knob) view.findViewById(R.id.knob);
        knob2 = (Knob) view.findViewById(R.id.knob2);
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
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

                if(!IsUserIsInteracting)
                {
                    IsUserIsInteracting = true;
                    Log.d("msg","I am stuck here");
                }
                else {
                   // if (i != 0)
                      //  new save().execute();
                    switch (i) {
                        case 0:
                               if(curr!=0 && curr!=-1) {
                                   curr=0;
                                   ArrayList<Short> a = new ArrayList<>();
                                   a.add(Mp3PlayerApplication.equalizerBands.getBand0());
                                   a.add(Mp3PlayerApplication.equalizerBands.getBand1());
                                   a.add(Mp3PlayerApplication.equalizerBands.getBand2());
                                   a.add(Mp3PlayerApplication.equalizerBands.getBand3());
                                   a.add(Mp3PlayerApplication.equalizerBands.getBand4());
                                   Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(0);
                                   init(a);
                               }
                               else
                               {
                                   curr=0;
                                   Mp3PlayerApplication.equalizerBands.setBand0((short) (band0.getProgress()*3));
                                   Mp3PlayerApplication.equalizerBands.setBand1((short) (band1.getProgress()*3));
                                   Mp3PlayerApplication.equalizerBands.setBand2((short) (band2.getProgress()*3));
                                   Mp3PlayerApplication.equalizerBands.setBand3((short) (band3.getProgress()*3));
                                   Mp3PlayerApplication.equalizerBands.setBand4((short) (band4.getProgress()*3));
                                   Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(0);
                               }
                            break;
                        case 1:
                            curr = 1;
                            init(normal);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(1);
                            break;
                        case 2:
                            curr = 2;
                            init(Classical);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(2);
                            break;
                        case 3:
                            curr = 3;
                            init(Dance);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(3);
                            break;
                        case 4:
                            curr = 4;
                            init(Flat);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(4);
                            break;
                        case 5:
                            curr = 5;
                            init(Folk);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(5);
                            break;
                        case 6:
                            curr = 6;
                            init(Heavy_Metal);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(6);
                            break;
                        case 7:
                            curr = 7;
                            init(Hip_hop);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(7);
                            break;
                        case 8:
                            curr = 8;
                            init(Jazz);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(8);
                            break;
                        case 9:
                            curr = 9;
                            init(pop);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(9);
                            break;
                        case 10:
                            curr = 10;
                            init(rock);
                            Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(10);
                            break;
                    }
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
                Mp3PlayerApplication.equalizerBands.setBassBoost(a);
                Log.d("state", "" + state * 40);
                if(mBound)
                musicService.bass(a);
            }
        });
        knob2.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int i) {
                short a = (short) (i * 40);
                Mp3PlayerApplication.equalizerBands.setVisualizer(a);
                Log.d("state", "" + i * 40);
                if(mBound)
                musicService.vir(a);
            }
        });
        band0.setOnSeekBarChangeListener(new VerticalSeekBar(getContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                Mp3PlayerApplication.equalizerBands.setBand0(d);
                cband0 = d;
                Log.d("d", "" + d);
                //   editor.putFloat("band0",(d-1500));
                //  editor.putInt("band0",d-1500);
                db0.setText((d-1500)/100+"db");
                if(mBound)
                musicService.eq((short) 0, (short) (d - 1500));
                //  editor.apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("start","true");
                curr=-1;
                appCompatSpinner.setSelection(0);
                Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(0);
                press = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("stop","true");

            }

        });

        band1.setOnSeekBarChangeListener(new VerticalSeekBar(getContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                Mp3PlayerApplication.equalizerBands.setBand1(d);
                cband1 = d;
                Log.d("d", "" + d);
                db1.setText((d-1500)/100+"db");
                // editor.putInt("band1",d-1500);
                if(mBound)
                musicService.eq((short) 1, (short) (d - 1500));
                //   editor.apply();
//
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                curr=-1;
                appCompatSpinner.setSelection(0);
                Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(0);//   editor.apply();
                press = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        band2.setOnSeekBarChangeListener(new VerticalSeekBar(getContext()) {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                    Mp3PlayerApplication.equalizerBands.setBand2(d);
                cband2 = d;
                Log.d("d", "" + d);
                db2.setText((d-1500)/100+"db");
                //  editor.putInt("band2",d-1500);
                if(mBound)
                musicService.eq((short) 2, (short) (d - 1500));
                //  editor.apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                curr=-1;
                appCompatSpinner.setSelection(0);
                Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(0);
                press = true;
//   editor.apply();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        band3.setOnSeekBarChangeListener(new VerticalSeekBar(getContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                    Mp3PlayerApplication.equalizerBands.setBand3(d);
                Log.d("d", "" + d);
                cband3 = d;
                db3.setText((d-1500)/100+"db");
                // editor.putInt("band3",d-1500);
                //editor.apply();
                if(mBound)
                musicService.eq((short) 3, (short) (d - 1500));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                curr=-1;
                appCompatSpinner.setSelection(0);
                Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(0);

                press = true;
//   editor.apply();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        band4.setOnSeekBarChangeListener(new VerticalSeekBar(getContext()) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.d("progress", "" + progress);
                short d = (short) (progress * 3);
                    Mp3PlayerApplication.equalizerBands.setBand4(d);
                Log.d("d", "" + d);
                cband4 = d;
                // editor.putInt("band4",d-1500);
                db4.setText((d-1500)/100+"db");
                if(mBound)
                musicService.eq((short) 4, (short) (d - 1500));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                curr=-1;
                appCompatSpinner.setSelection(0);
                Mp3PlayerApplication.equalizerBands.setCustomEqualizerValue(0);
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
                ArrayList<Short> a = new ArrayList<>();
                a.add(Mp3PlayerApplication.equalizerBands.getBand0());
                a.add(Mp3PlayerApplication.equalizerBands.getBand1());
                a.add(Mp3PlayerApplication.equalizerBands.getBand2());
                a.add(Mp3PlayerApplication.equalizerBands.getBand3());
                a.add(Mp3PlayerApplication.equalizerBands.getBand4());
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
                  //  new save().execute();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void set()
    {
        normal.add((short)1800);
        normal.add((short)1500);
        normal.add((short)1500);
        normal.add((short)1500);
        normal.add((short)1800);
        Classical.add((short)2000);
        Classical.add((short)1800);
        Classical.add((short)1300);
        Classical.add((short)1900);
        Classical.add((short)1900);
        Dance.add((short)2100);
        Dance.add((short)1500);
        Dance.add((short)1700);
        Dance.add((short)1900);
        Dance.add((short)1600);
        Flat.add((short)1500);
        Flat.add((short)1500);
        Flat.add((short)1500);
        Flat.add((short)1500);
        Flat.add((short)1500);
        Folk.add((short)1800);
        Folk.add((short)1500);
        Folk.add((short)1500);
        Folk.add((short)1700);
        Folk.add((short)1400);
        Heavy_Metal.add((short)1900);
        Heavy_Metal.add((short)1600);
        Heavy_Metal.add((short)2400);
        Heavy_Metal.add((short)1800);
        Heavy_Metal.add((short)1500);
        Hip_hop.add((short)2000);
        Hip_hop.add((short)1800);
        Hip_hop.add((short)1500);
        Hip_hop.add((short)1600);
        Hip_hop.add((short)1800);
        Jazz.add((short)1900);
        Jazz.add((short)1700);
        Jazz.add((short)1300);
        Jazz.add((short)1700);
        Jazz.add((short)2000);
        pop.add((short)1400);
        pop.add((short)1700);
        pop.add((short)2000);
        pop.add((short)1600);
        pop.add((short)1300);
        rock.add((short)2000);
        rock.add((short)1800);
        rock.add((short)1400);
        rock.add((short)1800);
        rock.add((short)2000);
    }

    private void init(ArrayList<Short> a) {

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
        EqualizerBands equalizerBands = new EqualizerBands();
        equalizerBands.setBand0(Mp3PlayerApplication.equalizerBands.getBand0());
        equalizerBands.setBand1(Mp3PlayerApplication.equalizerBands.getBand1());
        equalizerBands.setBand2(Mp3PlayerApplication.equalizerBands.getBand2());
        equalizerBands.setBand3(Mp3PlayerApplication.equalizerBands.getBand3());
        equalizerBands.setBand4(Mp3PlayerApplication.equalizerBands.getBand4());
        Log.d("msg",equalizerBands.getBand0()+" "+equalizerBands.getBand1()+" "+equalizerBands.getBand2()+" "+equalizerBands.getBand3()+" "+equalizerBands.getBand4()+" ");

        band0.setProgress(Math.round(cband0 / 3));
        band1.setProgress(Math.round(cband1 / 3));
        band2.setProgress(Math.round(cband2 / 3));
        band3.setProgress(Math.round(cband3 / 3));
        band4.setProgress(Math.round(cband4 / 3));
        Log.d("msg","2-> "+Mp3PlayerApplication.equalizerBands.getBand0()+" "+Mp3PlayerApplication.equalizerBands.getBand1()+" "+Mp3PlayerApplication.equalizerBands.getBand2()+" "+Mp3PlayerApplication.equalizerBands.getBand3()+" "+Mp3PlayerApplication.equalizerBands.getBand4()+" ");

        if(a.size()>5) {
            knob.setState(a.get(5) / 40,true);
            knob2.setState(a.get(6) / 40,true);
            musicService.bass(a.get(5));
            musicService.vir(a.get(6));
        }
        musicService.eq((short) 0, (short) (cband0 - 1500));
        musicService.eq((short) 1, (short) (cband1 - 1500));
        musicService.eq((short) 2, (short) (cband2 - 1500));
        musicService.eq((short) 3, (short) (cband3 - 1500));
        musicService.eq((short) 4, (short) (cband4 - 1500));

        Mp3PlayerApplication.equalizerBands.setBand0(equalizerBands.getBand0());
        Mp3PlayerApplication.equalizerBands.setBand1(equalizerBands.getBand1());
        Mp3PlayerApplication.equalizerBands.setBand2(equalizerBands.getBand2());
        Mp3PlayerApplication.equalizerBands.setBand3(equalizerBands.getBand3());
        Mp3PlayerApplication.equalizerBands.setBand4(equalizerBands.getBand4());
        // band4.setProgress(50);
        /*
        Mp3PlayerApplication.equalizerBands.setBand0(cband0);
        Mp3PlayerApplication.equalizerBands.setBand1(cband1);
        Mp3PlayerApplication.equalizerBands.setBand2(cband2);
        Mp3PlayerApplication.equalizerBands.setBand3(cband3);
        Mp3PlayerApplication.equalizerBands.setBand4(cband4);
        */
        //updateViewModelAndOtherForBandValueChange();
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            mBound = true;
            try {
                tband0.setText(musicService.equalizer.getCenterFreq((short) 0) / 1000 + "Hz");
                tband1.setText(musicService.equalizer.getCenterFreq((short) 1) / 1000 + "Hz");
                tband2.setText(musicService.equalizer.getCenterFreq((short) 2) / 1000 + "Hz");
                tband3.setText(musicService.equalizer.getCenterFreq((short) 3) / 1000 + "Hz");
                tband4.setText(musicService.equalizer.getCenterFreq((short) 4) / 1000 + "Hz");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        try {
           new SetBandValueInDb().execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBound && serviceConnection != null) {
            getContext().unbindService(serviceConnection);
            mBound = false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Intent i = new Intent(getActivity(), MusicService.class);
        getContext().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            EqualizerBands equalizerBands = Mp3PlayerApplication.equalizerBands;
            ArrayList<Short> a = new ArrayList<>();
            a.add(equalizerBands.getBand0());
            a.add(equalizerBands.getBand1());
            a.add(equalizerBands.getBand2());
            a.add(equalizerBands.getBand3());
            a.add(equalizerBands.getBand4());
            a.add(equalizerBands.getBassBoost());
            a.add(equalizerBands.getVisualizer());
            if(Mp3PlayerApplication.equalizerBands.getCustomEqualizerValue()!=0) {
                Log.d("msg","selection in start = "+Mp3PlayerApplication.equalizerBands.getCustomEqualizerValue());
                IsUserIsInteracting=true;
                knob.setState((equalizerBands.getBassBoost()/40),true);
                knob2.setState(equalizerBands.getVisualizer()/40,true);
                appCompatSpinner.setSelection(Mp3PlayerApplication.equalizerBands.getCustomEqualizerValue());
            }
            else
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
            appCompatSpinner.setSelection(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("pause", "ture");
        if (mBound && serviceConnection != null) {
            getContext().unbindService(serviceConnection);
            mBound = false;
        }
    }

    public class SetBandValueInDb extends AsyncTask<Void,Void,Void> {

        public SetBandValueInDb() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appDatabase =  Room.databaseBuilder(getContext(),
                    AppDatabase.class, "database-name").build();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDatabase.equaliserEntityInterface().clearDatabase();
            appDatabase.equaliserEntityInterface().insertOne(CloneEqualiser.CloneFromEqualiserBandToEntity(Mp3PlayerApplication.equalizerBands));
            Log.d("msg","equaliser val setting = "+Mp3PlayerApplication.equalizerBands.getBand0()+" "+Mp3PlayerApplication.equalizerBands.getBand1()+" "+Mp3PlayerApplication.equalizerBands.getBand2()+" "+Mp3PlayerApplication.equalizerBands.getBand3()+" "+Mp3PlayerApplication.equalizerBands.getBand4()+" ");
            return null;
        }
    }


}
