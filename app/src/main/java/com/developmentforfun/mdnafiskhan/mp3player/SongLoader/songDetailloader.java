package com.developmentforfun.mdnafiskhan.mp3player.SongLoader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mdnafiskhan on 03-01-2017.
 */

public class songDetailloader {
    final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.SIZE ,MediaStore.Audio.Media._ID};
    final String[] columns2 = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ALBUM};
    final String[] columns3 = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
    public  Cursor cursor;
    public Cursor cursor2;
    public Cursor cursor3;
    private Context context;
    int albumindex,dataindex,titleindex,durationindex,artistindex;
    int alalbumindex,noofsongs,albumartindex;
    final static String orderBy = MediaStore.Audio.Media.TITLE;
    final static String orderBy2 = MediaStore.Audio.Albums.ALBUM;
    final static String orderBy3 = MediaStore.Audio.Albums.ARTIST;
    final static String where= "is_music AND duration > 10000 AND _size <> '0' ";

    public songDetailloader(Context context) {
        super();
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
        noofsongs = cursor2.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
        albumartindex = cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
    }

    public songDetailloader() {
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
        noofsongs = cursor2.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
        albumartindex = cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);




    }

    //function return the cursor for the album database.......
   public Cursor getAlbumCursor(Context context)
   {
       final String[] columns2 = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ALBUM};
       final String orderBy2 = MediaStore.Audio.Albums.ALBUM;
       cursor2 = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns2, null, null, orderBy2);
       return cursor2;
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

    public ArrayList<Songs> getSongs(String withWhat,String content)
    {   Cursor c;
        ArrayList<Songs> s= new ArrayList<>();
        String  orderby = MediaStore.Audio.Media.TITLE;;
        boolean condition;
        String[]  col = {MediaStore.Audio.Albums._ID ,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.DURATION};
        final String where= "is_music AND duration > 10000";

        c= context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,col ,where ,null,orderby) ;
        for(int i=0 ;i< c.getCount() ; i++)
        { Songs songs = new Songs();
            c.moveToPosition(i);
            switch(withWhat)
            {
                case "album" : if(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)).equals(content))
                                   {
                                       songs.settitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                                       songs.setSonguri(Uri.parse(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))));
                                       songs.setartist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                                       songs.setalbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                                       songs.setDuration(Long.decode(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                                       songs.setPosition(i);
                                       songs.setAlbumart(albumartwithalbum(content));
                                       s.add(songs);
                                   }
                    break;

                case "artist" : if(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)).equals(content))
                {
                    songs.settitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    songs.setSonguri(Uri.parse(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))));
                    songs.setartist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    songs.setalbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
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
                        songs.setartist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        songs.setalbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
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


  //return next song of current playing song and this function is redundabt i don,t want it....
    public Songs nexttracks(int pos)
    {
        Songs song = new Songs();
        Log.d("pos_>",pos+"");

        if(!cursor.isLast())
        {
            cursor.moveToPosition(pos+1);
            Log.d("pos_>",pos+1+"");
          }
        else
        {

        }
        song.setalbum(cursor.getString(albumindex));
        song.settitle(cursor.getString(titleindex));
        song.setSonguri(Uri.parse(cursor.getString(dataindex)));
        song.setartist(cursor.getString(artistindex));
        song.setDuration(Integer.parseInt(cursor.getString(durationindex)));
        song.setPosition(cursor.getPosition());
        song.setAlbumart(albumartwithalbum(song.getalbum()));

        return song;
    }

    public Uri songuriwithalbum(String album)
    {
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            if(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)).equals(album))
            {
               return Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            }
        }
        return null;
    }

    // this function return the previous song and i don,t reqired it...
    public Songs prevtrack(int pos)
    {
       Songs song = new Songs();
        if(cursor.getPosition() > 0)
        {
            cursor.moveToPosition(pos-1);
            Log.d("pos_>",pos-1+"");
        }
        else
        {
        }
        song.setalbum(cursor.getString(albumindex));
        song.settitle(cursor.getString(titleindex));
        song.setSonguri(Uri.parse(cursor.getString(dataindex)));
        song.setartist(cursor.getString(artistindex));
        song.setDuration(Long.decode(cursor.getString(durationindex)));
        song.setPosition(cursor.getPosition());
        song.setAlbumart(albumartwithalbum(song.getalbum()));
        return song;

    }

    public String songalbumwithname(String name)
    {
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            if(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)).equals(name))
            {
                return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            }
        }
        return null;
    }

  // return the no of song in traks......
   public int gettotaltracks()
   {
       return cursor.getCount();
   }



    //reutrn song title ..........
    public String songtitle( int position )
    {
        cursor.moveToPosition(position);
        return cursor.getString(titleindex);

    }

    //return song album......
    public String songalbum( int position )
    {

            cursor.moveToPosition(position);
            return cursor.getString(albumindex);

               }
    public int getpositionofalbumwithalbum(String album)
    {
        int i=0;
        cursor2.moveToFirst();
        while(!cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM)).equals(album))
        {
            cursor2.moveToNext();
            i++;
        }
        return i;
    }


    //return song duration........
    public String songduration(int position)
    {

            cursor.moveToPosition(position);
            return cursor.getString(durationindex);


    }

    public Songs sonngwithuri(String uri) {
        Songs song = new Songs();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (i == 0)
                cursor.moveToFirst();
            else
                cursor.moveToNext();

            if ((cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))).equals(uri)) {
                song.setalbum(cursor.getString(albumindex));
                song.settitle(cursor.getString(titleindex));
                song.setSonguri(Uri.parse(cursor.getString(dataindex)));
                song.setartist(cursor.getString(artistindex));
                song.setDuration(Long.decode(cursor.getString(durationindex)));
                song.setPosition(cursor.getPosition());
                song.setAlbumart(albumartwithalbum(song.getalbum()));
                return song;
            }

        }
        return null;
    }



    // return song model at particular position///////
    public Songs songatposition(int position)
    {
        Songs song = new Songs();
        cursor.moveToPosition(position);
            song.setalbum(cursor.getString(albumindex));
            song.settitle(cursor.getString(titleindex));
            song.setSonguri(Uri.parse(cursor.getString(dataindex)));
            song.setartist(cursor.getString(artistindex));
            song.setDuration(Long.decode(cursor.getString(durationindex)));
            song.setPosition(cursor.getPosition());

        return song;
    }

   public int songpositionwithname(String name)
   { cursor.moveToFirst();
       while(!cursor.isAfterLast())
       {
           if(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)).equals(name))
           {
               return cursor.getPosition();
           }
       }
       return -1;
   }



    public  String albumart(int position)
    {
        cursor2.moveToPosition(position);
        return cursor2.getString(albumartindex);
    }

    public void deleteSong(Context context, int position) {
        cursor.moveToPosition(position);
        Log.d("delteing song",""+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        int _id =cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        Uri uri;
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(uri, MediaStore.Audio.Media._ID + " = " + _id, null);

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
                return s;
            }
        }
        return null;
    }
    public String album(int position)
    {
        cursor2.moveToPosition(position);
        return cursor2.getString(alalbumindex);
    }

    public String noofsongs(int position)
    {

        cursor2.moveToPosition(position);
        return cursor2.getString(noofsongs);
    }

    // return the songs with particular album....
    public ArrayList<Songs> songnamewithalbum(String album)
    {
        ArrayList<Songs> songaaraylist = new ArrayList<>();
       for(int i=0;i<cursor.getCount() ; i++)
       {
           if(i==0)
           {
               cursor.moveToFirst();
           }
           else
           {
               cursor.moveToNext();
           }
               if(cursor.getString(albumindex).equals(album))
               {   Songs song = new Songs();
                   song.setalbum(cursor.getString(albumindex));
                   song.settitle(cursor.getString(titleindex));
                   song.setSonguri(Uri.parse(cursor.getString(dataindex)));
                   song.setartist(cursor.getString(artistindex));
                   song.setDuration(Long.decode(cursor.getString(durationindex)));
                   song.setPosition(cursor.getPosition());
                   songaaraylist.add(song);
               }

       }

       return songaaraylist;
    }



// return all traks/.....
    public ArrayList<Songs> allsongs()
    {
        ArrayList<Songs> songaaraylist = new ArrayList<>();
        for(int i=0;i<cursor.getCount() ; i++)
        {
            if(i==0)
            {
                cursor.moveToFirst();
            }
            else
            {
                cursor.moveToNext();
            }
            Songs song = new Songs();
                song.setalbum(cursor.getString(albumindex));
                song.settitle(cursor.getString(titleindex));
                song.setSonguri(Uri.parse(cursor.getString(dataindex)));
                song.setartist(cursor.getString(artistindex));
                song.setDuration(Long.decode(cursor.getString(durationindex)));
                song.setPosition(cursor.getPosition());
                songaaraylist.add(song);

        }

        return songaaraylist;
    }



}
