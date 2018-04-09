package com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Fragments.FavFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.MostPlayedFragment;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.List;

public class PlaylistFragmentActivity extends Fragment {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    SmartTabLayout tabLayout;
    private ViewPager mViewPager;

    String PageTitle[] = {"Favourite","Recently Added","Most Played"};

    public static PlaylistFragmentActivity getInstance()
    {
        return new PlaylistFragmentActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_playlist_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar3);
        if (toolbar != null) {
            ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
        }
        tabLayout = (SmartTabLayout) view.findViewById(R.id.tablayout3);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        tabLayout.setViewPager(mViewPager);

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            super.getPageTitle(position);
            return PageTitle[position];
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position)
            {
                case 0: return new FavFragment();

                case 1: return new FavFragment();

                case 2: return new FavFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
