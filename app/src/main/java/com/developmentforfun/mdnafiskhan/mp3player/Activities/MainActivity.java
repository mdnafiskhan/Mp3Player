package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.AlbumDetailFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.NavigationControler;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityViewModel.MainActivityViewModel;
import com.developmentforfun.mdnafiskhan.mp3player.ApplicationModelClass;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.AlbumFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.ArtistFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.FavFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.FolderFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.GenresFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.MostPlayedFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.Playlists;
import com.developmentforfun.mdnafiskhan.mp3player.Fragments.TracksFragment;
import com.developmentforfun.mdnafiskhan.mp3player.Helpers.SongHelper;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.OnBackPress;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Albums;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Genre;
import com.developmentforfun.mdnafiskhan.mp3player.Models.SearchedContent;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.Service.MusicService;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomSearchRecycler;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private MainActivityViewModel mainActivityViewModel;
    private String[] mPlanetTitles={"Tracks","Album","Artist","Genre","Folders","Playlist"};
    private String[] mPlanetTitles2={"MostPlayed","Favorites","Playlist"};
    static public DrawerLayout drawer;
    SharedPreferences sharedPreferences;
    public static ImageView nowPlayingImage;
    public static TextView nowPlaintName;
    public OnBackPress onBackPress;
    Cursor cursor;
    static Observer<Songs> songChangedObserver;
    static Observer<Boolean> fragmentRequestObserver;
    Fragment [] fragments = {new TracksFragment(),new AlbumFragment(), new ArtistFragment(),new GenresFragment(),new FolderFragment(),new Playlists()};
    Fragment [] fragments2 = {new MostPlayedFragment(),new FavFragment(), new Playlists()};
    int which =0;
    PagerAdapter pagerAdapter;
    PagerAdapter pagerAdapter2;
       ViewPager viewPager;
       public static CircleImageView im;
       int pos= -1 ;
       public static Context context;
       MusicService musicService;
           boolean mBound;
       SmartTabLayout tabLayout ;
       SongDetailLoader loader;
       public static Uri currentsonguri;
       ArrayList<Songs> give = new ArrayList<>();
    NavigationView navigationView;
    SharedPreferences.Editor editor;
    DisplayMetrics metrics;
    float width;
    float height;
    int albumindex,dataindex,titleindex,durationindex,artistindex;
    private final static String[] columns ={MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID};
    private final String where = "is_music AND duration > 10000 AND _size <> '0' ";
    private final String orderBy =  MediaStore.Audio.Media.TITLE;
    ConstraintLayout constraintLayout;
    RecyclerView recyclerView ;
    CustomSearchRecycler customSearchRecycler;
       protected void onCreate(final Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           Log.d("msg","main activity is created");
           setContentView(R.layout.activity_main);
           Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           if (toolbar != null) {
               setSupportActionBar(toolbar);
               getSupportActionBar().setDisplayHomeAsUpEnabled(true);
               toolbar.setNavigationIcon(R.drawable.nav);
           }
           mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
           subscribe();
           sharedPreferences = getSharedPreferences("lastplayed",Context.MODE_PRIVATE);
           Intent i = new Intent(MainActivity.this,MusicService.class);
           startService(i);
          // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          // setSupportActionBar(toolbar);
           drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
           ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                   this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
           drawer.setDrawerListener(toggle);
           toggle.syncState();
           loader  = new SongDetailLoader(getBaseContext());
           tabLayout = (SmartTabLayout) findViewById(R.id.tablayout);
           viewPager = (ViewPager) findViewById(R.id.viewPager);
           recyclerView = (RecyclerView) findViewById(R.id.SearchRecycler);
           context = getBaseContext();
           metrics = getResources().getDisplayMetrics();
           width = metrics.widthPixels;
           height = metrics.heightPixels;
           Log.d("size",""+width+"   "+height);
           pagerAdapter = new library(getSupportFragmentManager());
           pagerAdapter2 = new Playlist(getSupportFragmentManager());
           im  = (CircleImageView) findViewById(R.id.currentsong);
           constraintLayout = (ConstraintLayout) findViewById(R.id.content);
           im.setVisibility(View.VISIBLE);
                 // viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
                   viewPager.setAdapter(pagerAdapter);
                   viewPager.setOffscreenPageLimit(3);
                   tabLayout.setViewPager(viewPager);
           navigationView = (NavigationView) findViewById(R.id.nav_view);
          // searchView = (SearchView) findViewById(R.id.search);
           navigationView.setNavigationItemSelectedListener(this);
           nowPlayingImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nowplaying);
           nowPlaintName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nowplayingt);
           toolbar.setNavigationOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   drawer.openDrawer(Gravity.LEFT);
               }
           });
           im.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

             /* Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
              intent.setData(currentsonguri);
              intent.putExtra("flag",1);
              */
              NavigationControler.navigateToPlayerFragment(getSupportFragmentManager(),im);


          }

          });
           if(savedInstanceState == null) {
               Log.d("searching song","Current playing  song is null");
               new allsongs(getBaseContext(), currentsonguri).execute();
           }
          init();
        /*
           final Uri r= getIntent().getData();
              if(r!=null) {
                  currentsonguri = r;
                  Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                  intent.setData(r);
                  intent.putExtra("flag",0);
                  startActivity(intent);

              }
              */

          viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 4)
                {
                    onBackPress = ((FolderFragment)fragments[position]).set();
                }
                else
                    onBackPress=null;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
           }

    public void init()
    {
        if(!sharedPreferences.getString("uri","").equals(""))
        {
            String s = sharedPreferences.getString("uri","");
            if(!s.isEmpty()) {
                Uri u = Uri.parse(s);
                currentsonguri = u;
                new SetImage( u ,null,0,im).execute();
            }
        }
    }

    private void subscribe() {
         songChangedObserver = new Observer<Songs>() {
            @Override
            public void onChanged(@Nullable final Songs songs) {
                if(songs!=null)
                {
                    if(songs.getAlbumart()!=null)
                    {
                        Log.d("msg","in a main activity observer oberving the song changes"+songs.getAlbumart());
                        new SetImage(null,songs.getAlbumart(),1,im).execute();
                    }
                }
                else if(!sharedPreferences.getString("uri","").equals(""))
                {
                    String s = sharedPreferences.getString("uri","");
                    if(!s.isEmpty()) {
                        Uri u = Uri.parse(s);
                        currentsonguri = u;
                        new SetImage( u ,Mp3PlayerApplication.applicationModelClass.CurrentPlayingSong.getAlbumart(),0,im).execute();
                    }
                }
                else
                {

                }
                Log.d("msg","observers is called in main activity");
            }
        };

        mainActivityViewModel.getNowPlaying().observe(this, songChangedObserver);
        Log.d("msg","subscriber is set in main activity");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_view,menu);
        MenuItem menuItem = menu.findItem(R.id.SearchView);
       final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        MenuItemCompat.collapseActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("text change", "true");
                String text = newText.toLowerCase().trim();
                constraintLayout.setVisibility(View.VISIBLE);
                new getListsWithString(text).execute();
                /*
                ArrayList<Songs> songses = SongHelper.allsongs(getBaseContext(),"%"+text+"%");
                ArrayList<Artists> artistses = SongHelper.getArtist(getBaseContext(),"%"+text+"%");
                ArrayList<Genre> genres  = SongHelper.getGenres(getBaseContext(),"%"+text+"%");
                ArrayList<Albums> albumses = new ArrayList<Albums>();
               /* Log.d("genre","......");
                for(int i=0;i<genres.size();i++)
                    Log.d(""+i,""+genres.get(i).getNameGenre()+"");
                Log.d("songs","......");
                for(int i=0;i<songses.size();i++)
                    Log.d(""+i,""+songses.get(i).gettitle()+"");
                Log.d("artist","......");
                for(int i=0;i<artistses.size();i++)
                    Log.d(""+i,""+artistses.get(i).getArtistname()+"");
                recyclerView.setAdapter(new CustomSearchRecycler(getBaseContext(),songses,artistses,genres,albumses));
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));*/

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                constraintLayout.setVisibility(View.GONE);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.equaliser)
        {
            Intent i = new Intent(MainActivity.this,EqualiserAc.class);
            startActivity(i);
        }
        return true;
    }

    public class library extends FragmentStatePagerAdapter {

        library(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPlanetTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_UNCHANGED;
        }
    }


    public class Playlist extends FragmentStatePagerAdapter {

        Playlist(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments2[position];
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPlanetTitles2[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_UNCHANGED;
        }
    }

    public void setview(Bitmap bitmap)
    {
        if(im!=null)
        {
                if (bitmap != null) {
                    im.setImageBitmap(bitmap);
                    nowPlayingImage.setImageBitmap(bitmap);
                    nowPlaintName.setText(""+Mp3PlayerApplication.applicationModelClass.CurrentPlayingSong.getTitle());
                } else {
                    im.setImageResource(R.drawable.default_track_light);
                    Log.d("ic","ic_launcher setted");
                }
        }
    }


    public class getListsWithString extends AsyncTask<Void,Void,Void>
    {
        String what;
        ArrayList<Songs> songses;
        ArrayList<Artists> artistses;
        ArrayList<Genre> genres;
        ArrayList<Albums> albumses;
        SearchedContent searchedContent ;
        public getListsWithString(String s) {
            super();
            this.what = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customSearchRecycler = new CustomSearchRecycler(MainActivity.this,searchedContent);
            recyclerView.setAdapter(customSearchRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            songses = SongHelper.allsongs(getBaseContext(),"%"+what+"%");
            artistses = SongHelper.getArtist(getBaseContext(),"%"+what+"%");
            genres  = SongHelper.getGenres(getBaseContext(),"%"+what+"%");
            albumses = SongHelper.getAlbums(getBaseContext(),"%"+what+"%");
            /*Log.d("genre","......");
            for(int i=0;i<genres.size();i++)
                Log.d(""+i,""+genres.get(i).getNameGenre()+"");
            Log.d("songs","......");
            for(int i=0;i<songses.size();i++)
                Log.d(""+i,""+songses.get(i).gettitle()+"");
            Log.d("artist","......");
            for(int i=0;i<artistses.size();i++)
                Log.d(""+i,""+artistses.get(i).getArtistname()+"");
            Log.d("albums","......");
            for(int i=0;i<albumses.size();i++)
                Log.d(""+i,""+albumses.get(i).getAlbumName()+"");*/
            searchedContent = new SearchedContent(songses,artistses,albumses,genres);
            return null;
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


    public int allsongs(Uri uri)
    {
        int pos=0;
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
        dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        cursor.moveToFirst();
        Log.d("initial cusros size",""+cursor.getCount());
        for(int i=0;i<cursor.getCount();i++)
        {
            Songs song = new Songs();
            song.setalbum(cursor.getString(albumindex));
            song.settitle(cursor.getString(titleindex));
            song.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            song.setSonguri(Uri.parse(cursor.getString(dataindex)));
            song.setSongId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            song.setartist(cursor.getString(artistindex));
            song.setDuration(Long.decode(cursor.getString(durationindex)));
            song.setPosition(cursor.getPosition());
            song.setAlbumart(loader.albumartwithalbum(song.getalbum()));
            if(song.getSonguri().equals(uri)) {
                pos = i;
                Log.d("position found","i="+i);
            }
            this.give.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        return pos;

    }

    public class allsongs extends AsyncTask<Void,Void,Void>
    {
        Context context;
        int position= 0;
        Uri uri;
        public allsongs(Context context,Uri uri) {
            super();
            this.context = context;
            this.uri = uri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            position = allsongs(uri);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("action","setting playlist and pos ="+position);
//            musicService.setplaylist(give,position);
            Log.d("size of playlist", give.size()+"");
        }
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
        Log.d("msg","main activity is destroyed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("msg","main activity is onResume");
        Intent i = new Intent(this,MusicService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("msg","main activity is started");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("msg","main activity is stoped");
        if(customSearchRecycler!=null) {
            customSearchRecycler.unbindService();
        }
       try {
           if (mBound)
               unbindService(serviceConnection);
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }

    @Override
    public void onBackPressed() {
        Log.d("msg","onBackPressed");
        if(onBackPress!=null) {
          if (onBackPress.pressed()) {
              super.onBackPressed();
          }
      }
      else
      {
          Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
          if(fragment!=null)
          {
              getSupportFragmentManager().beginTransaction().remove(fragment).commit();
          }
          else
          {
              super.onBackPressed();
          }
      }
      //  new SetImage(null,Mp3PlayerApplication.applicationModelClass.CurrentPlayingSong.getAlbumart(),1,im).execute();
    }

    public class SetImage extends AsyncTask<Void,Void,Void>
    {
        Uri uri;
        String imagePath;
        int what;
        ImageView imageView;
        Bitmap bitmap;
        public SetImage(Uri uri,String imagePath,int what,ImageView imageView) {
            super();
            this.imagePath=imagePath;
            this.uri=  uri;
            this.what = what;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(bitmap!=null)
            {
                setview(bitmap);

                Log.d("bitmap","bitmap is set");

            }
            else
            {
                Log.d("bitmap","bitmap is null");
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
           Log.d("int setImage","what = "+what);
            if(what == 0)
            {
                MediaMetadataRetriever data = new MediaMetadataRetriever();
                try {
                    data.setDataSource(MainActivity.this, uri);
                    byte[] b = data.getEmbeddedPicture();
                    this.bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(what == 1)
            {
                Log.d("msg","image path ="+imagePath);
                this.bitmap  = BitmapFactory.decodeFile(imagePath);
            }

            return null;
        }
    }


}
