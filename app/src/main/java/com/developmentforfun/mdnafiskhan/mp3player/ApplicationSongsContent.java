package com.developmentforfun.mdnafiskhan.mp3player;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.SongIsLoaded;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Albums;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Genre;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mdnafiskhan on 16/03/2018.
 */

public class ApplicationSongsContent  {

    Context context ;
    static Boolean dataisLoaded = false;
    ArrayList<Songs> allSongs = new ArrayList<>();
    ArrayList<Albums> allAlbums = new ArrayList<>();
    ArrayList<Artists> allArtists = new ArrayList<>();
    ArrayList<Genre> allGenre = new ArrayList<>();

    Map<String, ArrayList<Songs>> GenreIdToSongs = new HashMap<>();
    Map<String, ArrayList<Albums>> GenreIdToAlbums = new HashMap<>();

    Map<String, ArrayList<Songs>> ArtistIdToSongs = new HashMap<>();
    Map<String, ArrayList<Albums>> ArtistIdToAlbums = new HashMap<>();

    public SongIsLoaded songIsLoaded;

    public Map<String, ArrayList<Songs>> getGenreIdToSongs() {
        return GenreIdToSongs;
    }

    public Map<String, ArrayList<Albums>> getGenreIdToAlbums() {
        return GenreIdToAlbums;
    }

    public Map<String, ArrayList<Songs>> getArtistIdToSongs() {
        return ArtistIdToSongs;
    }

    public Map<String, ArrayList<Albums>> getArtistIdToAlbums() {
        return ArtistIdToAlbums;
    }

    public void setInterface(SongIsLoaded songIsLoaded)
    {
        this.songIsLoaded = songIsLoaded;
    }


    final String[] columns3 = {MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME};
    final static String orderBy3 = MediaStore.Audio.Genres.NAME;
    public Cursor cursor3;

    final String[] columns1 = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
    final static String orderBy1 = MediaStore.Audio.Albums.ARTIST;
    public Cursor cursor1;

    final String[] columns2 = {"DISTINCT "+MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS,MediaStore.Audio.Albums.ALBUM,MediaStore.Audio.Albums.ARTIST};
    final String orderBy2 = MediaStore.Audio.Albums.ALBUM;

    private Cursor cursor ;
    private final static String[] columns ={MediaStore.Audio.Media.DATA,MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID,MediaStore.Audio.Media.ARTIST_ID,MediaStore.Audio.Media.DATE_ADDED};
    private final String where = "is_music AND duration > 10000 AND _size <> '0' ";
    private final String orderBy =  MediaStore.Audio.Media.TITLE;

    SongDetailLoader songDetailLoader ;


    public ApplicationSongsContent(Context context) {
        super();
        this.context = context;
        songDetailLoader = new SongDetailLoader(context);
        if(!dataisLoaded)
          new ProcessMediaStore().execute();
        else
        {
            songIsLoaded.songLoaded();
        }
    }

    public void startLoad()
    {

    }

    public ArrayList<Songs> getSongs()
    {
        return allSongs;
    }

    public ArrayList<Albums> getAlbums()
    {
        return allAlbums;
    }

    public ArrayList<Artists> getAllArtists() {
        return allArtists;
    }

    public ArrayList<Genre> getAllGenre() {
        return allGenre;
    }

    public void fillGenre()
    {
        cursor3 = context.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, columns3,null, null, orderBy3);
        if(cursor3!=null) {
            cursor3.moveToFirst();
            while(!cursor3.isAfterLast())
            {
                Genre gen = new Genre();
                //   Log.d("count of genressss",cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Genres._COUNT)));
                gen.setId(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Genres._ID)));
                gen.setNameGenre(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Genres.NAME)));
                Cursor count = context.getContentResolver().query(MediaStore.Audio.Genres.Members.
                        getContentUri("external", Long.parseLong(gen.getId())), null, null, null, null);
                if(count.getCount()>0)
                    allGenre.add(gen);
                cursor3.moveToNext();
            }

            cursor3.close();
        }
    }

    public void mapGenreToSongs()
    {
        final String[] col = {MediaStore.Audio.Genres.Members.AUDIO_ID,MediaStore.Audio.Genres.Members.GENRE_ID};

        for(int i=0;i<allGenre.size();i++)
        {
            String orderByx = MediaStore.Audio.Genres.Members.GENRE_ID;
            Cursor genreMemberCursor = context.getContentResolver().query(MediaStore.Audio.Genres.Members.getContentUri("external",Long.decode(allGenre.get(i).getId())), col, null,null, orderByx);
            ArrayList<Songs> songsWithGenre = new ArrayList<>();
            if(genreMemberCursor!=null)
            {
                genreMemberCursor.moveToFirst();
                while(!genreMemberCursor.isAfterLast())
                {
                    for(int j=0;j<allSongs.size();j++)
                    {
                        if(allSongs.get(j).getSongId().equals(genreMemberCursor.getString(genreMemberCursor.getColumnIndex(MediaStore.Audio.Genres.Members.AUDIO_ID))))
                        {
                            songsWithGenre.add(allSongs.get(j));
                        }
                    }
                    genreMemberCursor.moveToNext();
                }
                genreMemberCursor.close();
            }
            GenreIdToSongs.put(allGenre.get(i).getId(),songsWithGenre);
        }
    }

    public void mapGenreToAlbum()
    {
        for(int i=0;i<allGenre.size();i++)
        {
           ArrayList<Albums> albumsWithGenre = new ArrayList<>();
           ArrayList<Songs> songs = new ArrayList<>();
           songs = GenreIdToSongs.get(allGenre.get(i).getId());
           for(int j=0;j<songs.size();j++)
           {
               for(int k=0;k<allAlbums.size();k++)
               {
                   if(allAlbums.get(k).getAlbumId().equals(songs.get(j).getAlbumId()))
                   {
                       albumsWithGenre.add(allAlbums.get(k));
                   }
               }
           }
           GenreIdToAlbums.put(allGenre.get(i).getId(),albumsWithGenre);
        }
    }

    public void mapArtistToTracks()
    {

        for(int i=0;i<allArtists.size();i++)
        {
            ArrayList<Songs> songs = new ArrayList<>();
            for(int j=0;j<allSongs.size();j++)
            {
                if(allArtists.get(i).getArtistname().equals(allSongs.get(j).getArtist()))
                {
                    songs.add(allSongs.get(j));
                }
            }
            ArtistIdToSongs.put(allArtists.get(i).getArtistId(),songs);
        }
    }


    public void mapArtistToAlbums()
    {

        for(int i=0;i<allArtists.size();i++)
        {
            ArrayList<Albums> albumsWithArtist = new ArrayList<>();
            ArrayList<Songs> songs = new ArrayList<>();
            songs = ArtistIdToSongs.get(allArtists.get(i).getArtistId());
            for(int j=0;j<songs.size();j++)
            {
                for(int k=0;k<allAlbums.size();k++)
                {
                    if(allAlbums.get(k).getAlbumId().equals(songs.get(j).getAlbumId()))
                    {
                        albumsWithArtist.add(allAlbums.get(k));
                    }
                }
            }
            ArtistIdToAlbums.put(allArtists.get(i).getArtistId(),albumsWithArtist);
        }
    }


    public void allartist()
    {
        cursor1 = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, columns1, null, null, orderBy1);
        if(cursor1!=null) {
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                Artists art = new Artists();
                art.setArtistname(cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
                art.setNoalbums(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
                art.setNofosongs(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));
                art.setArtistId(cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Artists._ID)));
                this.allArtists.add(art);
                cursor1.moveToNext();
            }
            cursor1.close();
        }
    }

    public void getAllSongs()
    {
        int albumindex,dataindex,titleindex,durationindex,artistindex;
        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
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
            song.setArtistId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));
            song.setSonguri(Uri.parse(cursor.getString(dataindex)));
            song.setartist(cursor.getString(artistindex));
            song.setDuration(Long.decode(cursor.getString(durationindex)));
            song.setPosition(cursor.getPosition());
            song.setSongId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            song.setDateAdded(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
            song.setAlbumart(songDetailLoader.albumartwithalbum(song.getalbum()));
            this.allSongs.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("Size of tracks list",allSongs.size()+"");

    }


    public void getAllAlbums()
    {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns2, "1) GROUP BY (1", null, orderBy2);

        if(cursor!=null)
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Albums albums = new Albums();
                albums.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
                albums.setAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
                albums.setArtistName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize= 2;
                String art =cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                albums.setAlbumArt(art);
                allAlbums.add(albums);
                cursor.moveToNext();
            }
            cursor.close();
        }
    }



    public class ProcessMediaStore extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            getAllSongs();
            getAllAlbums();
            allartist();
            fillGenre();
            mapGenreToSongs();
            mapGenreToAlbum();
            mapArtistToTracks();
            mapArtistToAlbums();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("msg","songs is loaded");
            try {
                songIsLoaded.songLoaded();
                dataisLoaded = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }




}
