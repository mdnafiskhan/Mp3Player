package com.developmentforfun.mdnafiskhan.mp3player.SongLoader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 24/01/2018.
 */

public class PlaylistProvider {
    /**
     * Queries all the playlists known to the MediaStore.
     *
     * @param resolver A ContentResolver to use.
     * @return The queried cursor.
     */
    public static ArrayList<com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist> queryPlaylists(ContentResolver resolver)
    {
        ArrayList<com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist> playlists = new ArrayList<>();
        Uri media = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME, MediaStore.Audio.Playlists.DATE_MODIFIED, MediaStore.Audio.Playlists.DATE_ADDED, MediaStore.Audio.Playlists.DATA};
        String sort = MediaStore.Audio.Playlists.NAME;
        Cursor cursor = resolver.query(media, projection, null, null, sort);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist playlist = new com.developmentforfun.mdnafiskhan.mp3player.Models.Playlist();
                playlist.setId(cursor.getString(0));
                playlist.setName(cursor.getString(1));
                playlist.setDateAdded(cursor.getString(3));
                playlist.setLastModified(cursor.getString(2));
                playlist.setData(cursor.getString(4));
                if(!playlists.contains(playlist))
                {
                    playlists.add(playlist);
                }
                cursor.moveToNext();

            }
        }
        return playlists;
    }

    /**
     * Retrieves the id for a playlist with the given name.
     *
     * @param resolver A ContentResolver to use.
     * @param name The name of the playlist.
     * @return The id of the playlist, or -1 if there is no playlist with the
     * given name.
     */
    public static long getPlaylist(ContentResolver resolver, String name)
    {
        long id = -1;

        Cursor cursor = resolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Playlists._ID },
                MediaStore.Audio.Playlists.NAME + "=?",
                new String[] { name }, null);

        if (cursor != null) {
            if (cursor.moveToNext())
                id = cursor.getLong(0);
            cursor.close();
        }

        return id;
    }

    public static long createPlaylist(ContentResolver resolver, String name)
    {
        long id = getPlaylist(resolver, name);

        if (id == -1) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Audio.Playlists.NAME, name);
            Uri uri = resolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);
            id = Long.parseLong(uri.getLastPathSegment());
        } else {
            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id);
            resolver.delete(uri, null, null);
        }

        return id;
    }

    public static int addToPlaylist(ContentResolver resolver, long playlistId, ArrayList<Songs> songses)
    {
        if (playlistId == -1)
            return 0;

        // Find the greatest PLAY_ORDER in the playlist
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        String[] projection = new String[] { MediaStore.Audio.Playlists.Members.PLAY_ORDER };
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        int base = 0;
        if(cursor!=null) {
            if (cursor.moveToLast())
                base = cursor.getInt(0) + 1;
            cursor.close();
        }

        if (songses == null)
            return 0;

        int count = songses.size();
        if (count > 0) {
            ContentValues[] values = new ContentValues[count];
            for (int i = 0; i != count; ++i) {
                ContentValues value = new ContentValues(2);
                value.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(base + i));
                value.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, Long.parseLong(songses.get(i).getSongId().trim()));
                values[i] = value;
            }
            resolver.bulkInsert(uri, values);
        }
        return count;
    }

    /**
     * Delete the playlist with the given id.
     *
     * @param resolver A ContentResolver to use.
     * @param id The Media.Audio.Playlists id of the playlist.
     */
    public static void deletePlaylist(ContentResolver resolver, long id)
    {
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, id);
        String where = MediaStore.Audio.Playlists._ID + "="+ id ;
        resolver.delete(uri, where, null);
        Log.d("msg","playlist deleted");
    }

    /**
     * Rename the playlist with the given id.
     *
     * @param resolver A ContentResolver to use.
     * @param id The Media.Audio.Playlists id of the playlist.
     * @param newName The new name for the playlist.
     */
    public static void renamePlaylist(ContentResolver resolver, long id, String newName)
    {
        long existingId = getPlaylist(resolver, newName);
        // We are already called the requested name; nothing to do.
        if (existingId == id)
            return;
        // There is already a playlist with this name. Kill it.
        if (existingId != -1)
            deletePlaylist(resolver, existingId);

        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Audio.Playlists.NAME, newName);
        resolver.update(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values, "_id=" + id, null);
    }

    public static int deletePlaylistTracks(Context context, long playlistId,
                                           long audioId) {
        ContentResolver resolver = context.getContentResolver();
        int countDel = 0;
        try {
            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri(
                    "external", playlistId);
            String where = MediaStore.Audio.Playlists.Members.AUDIO_ID + "=?" ; // my mistake was I used .AUDIO_ID here

            String audioId1 = Long.toString(audioId);
            String[] whereVal = { audioId1 };
            countDel=resolver.delete(uri, where,whereVal);
            Log.d("TAG", "tracks deleted=" + countDel);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return countDel;

    }
}
