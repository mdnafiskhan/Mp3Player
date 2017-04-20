package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.Activity;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.developmentforfun.mdnafiskhan.mp3player.Fragments.AlbumFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.FavFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.MostPlayedFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Playlists;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.TracksFragment;
import com.developmentforfun.mdnafiskhan.mp3player.R;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class PlaylistActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlistactivity);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabTextColors(Color.CYAN,Color.WHITE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        pagerAdapter = new fragment(getSupportFragmentManager());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setAdapter(pagerAdapter);


    }

    public class fragment extends FragmentStatePagerAdapter {

        public fragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:  return new MostPlayedFragment();
                case 1:  return new FavFragment();
                case 2:  return new Playlists();
            }
            return new MostPlayedFragment();
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
                case 0 : s= "MostPlayed" ;
                    break;
                case 1: s= "Favorites" ;
                    break;
                case 2: s = "MyPlaylist" ;
            }
            return s;
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
