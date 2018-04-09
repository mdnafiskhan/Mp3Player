package com.developmentforfun.mdnafiskhan.mp3player.Fragments;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.FIle;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.OnBackPress;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.CustomRecyclerViewAdapter0;
import com.developmentforfun.mdnafiskhan.mp3player.customAdapters.FolderAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 14/01/2018.
 */

public class FolderFragment extends Fragment implements FIle,OnBackPress {
    RecyclerView recyclerView;
    SongDetailLoader loader = new SongDetailLoader();
    ArrayList<File> folders = new ArrayList<>();
    ArrayList<Integer> count = new ArrayList<>();
    ArrayList<Songs> songses=  new ArrayList<>();
    File parentCur ;
    Cursor cursor;
    int albumindex,dataindex,titleindex,durationindex,artistindex;
    private final static String[] columns ={MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID};
    private final String orderBy =  MediaStore.Audio.Media.TITLE;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            CustomRecyclerViewAdapter0 customRecyclerViewAdapter0;
    FolderAdapter folderAdapter;
    public FolderFragment() {
        super();
    }
    int stage =0;
    boolean goback = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader.set(getContext());
    }

    public OnBackPress  set()
    {
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.folder_fragment_layout,container,false);
        recyclerView  = (RecyclerView) v.findViewById(R.id.folderRecycler);
        customRecyclerViewAdapter0 = new CustomRecyclerViewAdapter0(getContext(),songses);
        folderAdapter = new FolderAdapter(getContext(),folders,this,count);
        recyclerView.setAdapter(folderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        fill(0,0);
        return v;
    }
    public void getSubDir(File f)
    {
        parentCur  =f;
        File[] files = f.listFiles();
        if(files!=null && f.isDirectory()) {
            setSongs(f);
            recyclerView.setAdapter(new CustomRecyclerViewAdapter0(getContext(),songses));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.getAdapter().notifyDataSetChanged();
            //recyclerView.animate().alpha(1f).start();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
        try{
            customRecyclerViewAdapter0.unbindService();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(int position) {
        try {
            fill(1,position);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        goback=false;
    }

    void fill(int what,int position)
    {
        String[] projection = new String[]{"COUNT(" + MediaStore.Files.FileColumns.DATA + ") AS totalFiles",
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO +
                ") GROUP BY (" + MediaStore.Files.FileColumns.PARENT;

        String sortOrder = MediaStore.Files.FileColumns.DISPLAY_NAME + " ASC";

        //CursorLoader cursorLoader = new CursorLoader(getActivity(), MediaStore.Files.getContentUri("external"), projection, selection, null, sortOrder);
        Cursor c = getActivity().getContentResolver().query( MediaStore.Files.getContentUri("external"), projection, selection, null, sortOrder);
        if(c!=null)
        {
           if(what==0) {
               folders.clear();
               c.moveToFirst();
               while (!c.isAfterLast()) {
                   folders.add(new File(c.getString(3)).getParentFile());
                   count.add(Integer.parseInt(c.getString(0).trim()));
                   Log.d("name :",""+new File(c.getString(3)).getParentFile().getName()+"");
                   c.moveToNext();
               }
               recyclerView.setAdapter(folderAdapter);
               recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
               recyclerView.getAdapter().notifyDataSetChanged();
           }
           else
           {
               c.moveToPosition(position);
               getSubDir(new File(c.getString(3)).getParentFile());
           }

        }
    }

    void setSongs(File file)
    {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String where = "is_music AND duration > 10000 AND _size <> '0' AND "+MediaStore.Audio.Media.DATA +" like ? ";
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = getContext().getContentResolver().query(uri, projection, where, new String[]{"%"+file.getName()+"%"}, sortOrder);
        Log.d("uri",""+file.getName());
        if(cursor!=null) {
            dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            cursor.moveToFirst();
            Log.d("count = ", cursor.getCount() + "");
            songses.clear();
            for (int i = 0; i < cursor.getCount(); i++) {
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
                this.songses.add(song);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d("Size of tracks list",songses.size()+"");
    }


    @Override
    public boolean pressed() {
        if(goback)
        {
            return goback;
        }
        else
        {
            fill(0,0);
            goback=true;
            return false;
        }
    }
}
