package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.SongIsLoaded;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;

public class SplashActivity extends AppCompatActivity implements SongIsLoaded {


    public SplashActivity() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Mp3PlayerApplication.applicationSongsContent.setInterface(this);
    }

    @Override
    public void songLoaded() {
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
