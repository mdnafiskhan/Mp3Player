package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Artist_AlbumFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Artist_TracksFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Genre_AlbumFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Genre_TracksFragment;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by mdnafiskhan on 16/01/2018.
 */

public class Genre_Activity extends AppCompatActivity {


    private Genre_Activity.SectionsPagerAdapter mSectionsPagerAdapter;
    SmartTabLayout tabLayout ;

    private ViewPager mViewPager;
    TextView artistHead;
    String Genre;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_);
        Genre =(String) getIntent().getCharSequenceExtra("genreName");
        id =(String) getIntent().getCharSequenceExtra("genreId");
        mSectionsPagerAdapter = new Genre_Activity.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        artistHead = (TextView) findViewById(R.id.artistHead);
        artistHead.setText(Genre+"");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.animate().alpha(1f).start();
        tabLayout = (SmartTabLayout) findViewById(R.id.tablayout);
        mViewPager.setOffscreenPageLimit(2);
        tabLayout.setViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Artist_Activity.PlaceholderFragment newInstance(int sectionNumber) {
            Artist_Activity.PlaceholderFragment fragment = new Artist_Activity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_artist_, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
              //  case 0 : return new Genre_AlbumFragment(id);
              //  case 1 : return new Genre_TracksFragment(id);

            }
            return Artist_Activity.PlaceholderFragment.newInstance(position + 1);
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
}