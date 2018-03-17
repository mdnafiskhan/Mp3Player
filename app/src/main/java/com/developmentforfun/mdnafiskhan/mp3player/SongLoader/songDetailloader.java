package com.developmentforfun.mdnafiskhan.mp3player.SongLoader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 03-01-2017.
 */

public class SongDetailLoader {
    final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID};
    final String[] columns2 = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ALBUM};
    final String[] columns3 = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
    public  Cursor cursor;
    public Cursor cursor2;
    public Cursor cursor3;
    private Context context;
    int albumindex,dataindex,titleindex,durationindex,artistindex,albumId;
    int alalbumindex,noofsongs,albumartindex;
    final static String orderBy = MediaStore.Audio.Media.TITLE;
    final static String orderBy2 = MediaStore.Audio.Albums.ALBUM;
    final static String orderBy3 = MediaStore.Audio.Albums.ARTIST;
    final static String where= "is_music AND duration > 10000 AND _size <> '0' ";

    public SongDetailLoader(Context context) {
        super();
        this.context = context;
        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
        cursor2 = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns2, null, null, orderBy2);
        cursor3 = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, columns3, null, null, orderBy3);
        dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        albumId= cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        alalbumindex = cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
        artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        noofsongs = cursor2.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
        albumartindex = cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
    }

    public SongDetailLoader() {
        super();
    }

    public void set(final Context context) {
        this.context = context;
        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
        cursor2 = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns2, null, null, orderBy2);
        cursor3 = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, columns3, null, null, orderBy3);
        dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        alalbumindex = cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
        artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        noofsongs = cursor2.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
        albumartindex = cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);




    }

    //function return the cursor for the album database.......
    public Cursor getAlbumCursor(Context context)
    {
        final String[] columns2 = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS,MediaStore.Audio.Albums.ALBUM,MediaStore.Audio.Albums.ARTIST};
        final String orderBy2 = MediaStore.Audio.Albums.ALBUM;
        cursor2 = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns2, null, null, orderBy2);
        return cursor2;
    }

    public Cursor getSongCursor(Context context)
    {
        String  orderby = MediaStore.Audio.Media.TITLE;;
        String[]  col = {MediaStore.Audio.Albums._ID ,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM_ID};
        final String where= "is_music AND duration > 10000";
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,col ,where ,null,orderby) ;
    }


    public ArrayList<Artists> allartists()
    { ArrayList<Artists> a= new ArrayList<>();
        cursor3.moveToFirst();
        for(int i=0;i< cursor3.getCount() ;i++)
        {
            Artists art = new Artists();
            art.setArtistname(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
            art.setNoalbums(Integer.parseInt(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
            art.setNofosongs(Integer.parseInt(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));
            a.add(art);
            cursor3.moveToNext();
        }
        cursor3.close();
        return a;
    }

    public ArrayList<Songs> getSongs(String withWhat,String content,Context context)
    {   Cursor c;
        ArrayList<Songs> s= new ArrayList<>();
        String  orderby = MediaStore.Audio.Media.TITLE;;
        boolean condition;
        String[]  col = {MediaStore.Audio.Media.ALBUM_ID ,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media._ID};
        final String where= "is_music AND duration > 10000";

        c= context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,col ,where ,null,orderby) ;
        for(int i=0 ;i< c.getCount() ; i++)
        { Songs songs = new Songs();
            c.moveToPosition(i);
            switch(withWhat)
            {
                case "album" :
                    if(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).equals(content))
                    {
                        songs.settitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                        songs.setAlbumId(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                        songs.setSonguri(Uri.parse(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))));
                        songs.setSongId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
                        songs.setartist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        songs.setalbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        songs.setDuration(Long.decode(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                        songs.setPosition(i);
                        songs.setAlbumart(albumartwithalbum(songs.getAlbum()));
                        s.add(songs);
                    }
                    break;

                case "artist" : if(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)).equals(content))
                {
                    songs.settitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    songs.setSonguri(Uri.parse(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))));
                    songs.setartist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    songs.setSongId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
                    songs.setalbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                    songs.setAlbumId(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    songs.setDuration(Long.decode(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                    songs.setPosition(i);
                    songs.setAlbumart(albumartwithalbum(songs.getalbum()));
                    s.add(songs);
                }
                    break ;

                case "geners" :
                    if(c.getString(c.getColumnIndex(MediaStore.Audio.Media.COMPOSER)).equals(content))
                    {
                        songs.settitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                        songs.setSonguri(Uri.parse(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))));
                        songs.setSongId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
                        songs.setartist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        songs.setalbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        songs.setAlbumId(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                        songs.setDuration(Long.decode(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                        songs.setPosition(i);
                        songs.setAlbumart(albumartwithalbum(songs.getalbum()));
                        s.add(songs);
                    }
                    break;
                default:  return null ;
            }

        }
        for(int j=0 ;j< s.size();j++)
        {
            Log.d("positions--->",""+s.get(j).getPosition());
        }
        c.close();
        return s;
    }

    public String albumartwithalbum(String album)
    {
        for(int i=0;i<cursor2.getCount();i++)
        {
            if(i==0)
                cursor2.moveToFirst();
            else
                cursor2.moveToNext();

            if(cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM)).equals(album))
            {
                String s =cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                //cursor2.close();
                return s;
            }
        }
        return null;
    }

}
