package com.developmentforfun.mdnafiskhan.mp3player.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Albums;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Genre;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.SongDetailLoader;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 24/01/2018.
 */

public class SongHelper {


    public static ArrayList<Songs> allsongs(Context context, String like) {
        Cursor cursor;
        String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_RINGTONE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID};
        String where = "is_music AND duration > 10000 AND _size <> '0' AND "+MediaStore.Audio.Media.TITLE+" LIKE \""+like+"\"";
        String orderBy = MediaStore.Audio.Media.TITLE;
        int dataindex, albumindex, titleindex, durationindex, artistindex;
        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
        dataindex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        albumindex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        titleindex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        durationindex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        artistindex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        ArrayList<Songs> give = new ArrayList<>();
        SongDetailLoader loader = new SongDetailLoader();
        loader.set(context);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
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
            give.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("Size of tracks list", give.size() + "");
        return give;
    }

    public static ArrayList<Artists> getArtist(Context context, String like) {
        ArrayList<Artists> aa = new ArrayList<>();
        String where = ""+ MediaStore.Audio.Artists.ARTIST+" LIKE \""+like+"\"";
        final String[] columns3 = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
        final String orderBy3 = MediaStore.Audio.Albums.ARTIST;
        Cursor cursor3;
        cursor3 = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, columns3,where, null, orderBy3);
        cursor3.moveToFirst();
        for (int i = 0; i < cursor3.getCount(); i++) {
            Artists art = new Artists();
            art.setArtistname(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
            art.setNoalbums(Integer.parseInt(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
            art.setNofosongs(Integer.parseInt(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));
            aa.add(art);
            cursor3.moveToNext();
        }
        cursor3.close();
        return aa;
    }

    public static ArrayList<Genre> getGenres(Context context, String like) {
        ArrayList<Genre> genre = new ArrayList<>();
        final String[] columns3 = {MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME};
        final String orderBy3 = MediaStore.Audio.Genres.NAME;
        Cursor cursor3;
        String where = ""+MediaStore.Audio.Genres.NAME+" LIKE \""+like+"\"";
        cursor3 = context.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, columns3, where, null, orderBy3);
        if (cursor3 != null) {
            cursor3.moveToFirst();
            while (!cursor3.isAfterLast()) {
                Genre gen = new Genre();
                //   Log.d("count of genressss",cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Genres._COUNT)));
                gen.setId(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Genres._ID)));
                gen.setNameGenre(cursor3.getString(cursor3.getColumnIndex(MediaStore.Audio.Genres.NAME)));
                Cursor count = context.getContentResolver().query(MediaStore.Audio.Genres.Members.
                        getContentUri("external", Long.parseLong(gen.getId())), null, null, null, null);
                if (count != null) {
                    if (count.getCount() > 0)
                        genre.add(gen);
                }
                cursor3.moveToNext();
            }

            cursor3.close();
        }
        return genre;
    }

    public static ArrayList<Albums> getAlbums(Context context,String like)
    {
        ArrayList<Albums> albumses = new ArrayList<>();
        final String[] columns = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS,MediaStore.Audio.Albums.ALBUM,MediaStore.Audio.Albums.ARTIST};
        final String orderBy = MediaStore.Audio.Albums.ALBUM;
        String where = ""+MediaStore.Audio.Albums.ALBUM+" LIKE \""+like+"\"";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns, where, null, orderBy);
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
                albums.setAlbumArt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                if(!albumses.contains(albums))
                {
                    albumses.add(albums);
                }
                cursor.moveToNext();
            }
        }
       return albumses;
    }
}
