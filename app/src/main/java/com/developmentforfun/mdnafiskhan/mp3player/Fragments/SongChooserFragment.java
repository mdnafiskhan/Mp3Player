package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.ChosenSongs;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.PlaylistProvider;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.ChooserRecyclerViewAdapter;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter0;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 28/01/2018.
 */

public class SongChooserFragment extends DialogFragment implements ChosenSongs {
   ArrayList<Songs> ChosenSongs = new ArrayList<>();

    int albumindex,dataindex,titleindex,durationindex,artistindex;
    private final static String[] columns ={MediaStore.Audio.Media.DATA,MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID};
    private final String where = "is_music AND duration > 10000 AND _size <> '0' ";
    private final String orderBy =  MediaStore.Audio.Media.TITLE;
    private Cursor cursor ;
    SongDetailLoader loader = new SongDetailLoader();
    RecyclerView recyclerView;
    ArrayList<Songs> songses = new ArrayList<>();
    CardView addFinally;
    String playlistId;
    String playlistName;


    public SongChooserFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((AppCompatActivity) getActivity()).supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.playlist_song_layout,container,false);
        Toolbar toolbar  = (Toolbar) v.findViewById(R.id.toolbar);
        addFinally = (CardView) v.findViewById(R.id.addFinally);
        addFinally.setVisibility(View.VISIBLE);
        playlistId = getArguments().getString("playlistId");
        playlistName = getArguments().getString("playlistName");
        toolbar.setTitle("  Select Song");
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
             setHasOptionsMenu(true);;
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview3);
         new allsongs("").execute();
        recyclerView.setAdapter(new ChooserRecyclerViewAdapter(getActivity(),songses,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addFinally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new InsertIntoPlaylist().execute();
            }
        });
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("in onCreateOptionMenu","of fragment");
        inflater.inflate(R.menu.search_view,menu);
        MenuItem menuItem = menu.findItem(R.id.SearchView);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new allsongs(newText.toLowerCase().trim()).execute();
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void allsongs(String like)
    {
        final String where = "is_music AND duration > 10000 AND _size <> '0' AND "+MediaStore.Audio.Media.TITLE+" LIKE \"%"+like+"%\"";
        final String orderBy =  MediaStore.Audio.Media.TITLE;
        cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
        dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++)
        {
            Songs song = new Songs();
            song.setalbum(cursor.getString(albumindex));
            song.settitle(cursor.getString(titleindex));
            song.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            song.setSonguri(Uri.parse(cursor.getString(dataindex)));
            song.setartist(cursor.getString(artistindex));
            song.setDuration(Long.decode(cursor.getString(durationindex)));
            song.setPosition(cursor.getPosition());
            song.setSongId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            song.setAlbumart(loader.albumartwithalbum(song.getalbum()));
            this.songses.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("Size of tracks list",songses.size()+"");

    }

    @Override
    public void getChosenSongs(ArrayList<Songs> ChosenSongs) {
        this.ChosenSongs = ChosenSongs;
    }

    public class InsertIntoPlaylist extends AsyncTask<Void,Void,Void>
    {
        Boolean action;
        public InsertIntoPlaylist() {
            super();
            action = true;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(action)
            Toast.makeText(getContext(),"Songs are added to playlist",Toast.LENGTH_SHORT).show();
            else
            {
                Toast.makeText(getContext(),"No song selected",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(ChosenSongs.size()>0)
                PlaylistProvider.addToPlaylist(getContext().getContentResolver(),Long.parseLong(playlistId),ChosenSongs);
            else
                action=false;
            return null;
        }
    }


    public class allsongs extends AsyncTask<Void,Void,Void>
    {
        String like;
        public allsongs(String like) {
            super();
            this.like = like;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            songses.clear();
            loader.set(getActivity());
        }

        @Override
        protected Void doInBackground(Void... params) {
            allsongs(this.like);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      //  int width = getResources().getDimensionPixelSize(R.dimen.width);
     //   int height = getResources().getDimensionPixelSize(R.dimen.height);
        //getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setLayout(
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels
        );
    }
}
