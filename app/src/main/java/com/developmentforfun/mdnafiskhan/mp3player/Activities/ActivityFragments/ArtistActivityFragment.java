package com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.Artist_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Artist_AlbumFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Artist_TracksFragment;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
/**
 * Created by mdnafiskhan on 18/03/2018.
 */

public class ArtistActivityFragment extends Fragment {
    SmartTabLayout tabLayout ;
    SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager mViewPager;
    TextView artistHead;
    String artist,artistId;
    public ArtistActivityFragment() {
        super();
    }

    public static ArtistActivityFragment getNewInstance()
    {
        return new ArtistActivityFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_artist_,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artist =(String) getArguments().get("artist");
        artistId = (String) getArguments().get("artistId");
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        artistHead = (TextView) view.findViewById(R.id.artistHead);
        artistHead.setText(artist+"");
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.animate().alpha(1f).start();
        tabLayout = (SmartTabLayout) view.findViewById(R.id.tablayout);
        mViewPager.setOffscreenPageLimit(2);
        tabLayout.setViewPager(mViewPager);
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position)
            {
                case 0 :
                    Artist_AlbumFragment artist_albumFragment = new Artist_AlbumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("artist",artist);
                    bundle.putCharSequence("artistId",artistId);
                    artist_albumFragment.setArguments(bundle);
                    return artist_albumFragment;
                case 1 :
                    Artist_TracksFragment artist_tracksFragment = new Artist_TracksFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putCharSequence("artist",artist);
                    bundle2.putCharSequence("artistId",artistId);
                    artist_tracksFragment.setArguments(bundle2);
                    return artist_tracksFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALBUMS";
                case 1:
                    return "TRACKS";

            }
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
