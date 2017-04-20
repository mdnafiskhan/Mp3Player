package com.developmentforfun.mdnafiskhan.mp3player.DataBase;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 12-01-2017.
 */

public class DataBaseClass extends SQLiteOpenHelper {
    private Context context ;
    private static String DATABASE_NAME = "mydatabase" ;
    private static String TABLE1_NAME  = "mostplayedsongs" ;
    private static String TABLE2_NAME = "likedsongs" ;
    private static String TABLE3_NAME = "recentlyadded";

    private static String SONGTILE="title" ;
    private static String ARTISTNAME="artist" ;
    private static String DURATION ="duration";
    private static String POSITION ="position" ;
    private static String ALBUMART = "albumart";
    private static String SONGURI = "songuri";
    private static String NOOFPLAY = "noofplay";

    public DataBaseClass(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String query = "CREATE TABLE " +TABLE1_NAME
               +" ("+SONGTILE+" CHARACTER(40), "+ARTISTNAME+" CHARACTER(25), "
               +DURATION+" CHARACTER(12), "+POSITION+" INTEGER, "+ALBUMART+" CHARACTER(50), " +SONGURI+" CHARACTER(40), " +NOOFPLAY+" INTEGER );";
         db.execSQL(query);

        String query2 = "CREATE TABLE " +TABLE2_NAME
                +" ("+SONGTILE+" CHARACTER(40), "+ARTISTNAME+" CHARACTER(25), "
                +DURATION+" CHARACTER(12), "+POSITION+" INTEGER, "+ALBUMART+" CHARACTER(50), " +SONGURI+" CHARACTER(40) );";
        db.execSQL(query2);

        String query3 = "CREATE TABLE " +TABLE3_NAME
                +" ("+SONGTILE+" CHARACTER(40), "+ARTISTNAME+" CHARACTER(25), "
                +DURATION+" CHARACTER(12), "+POSITION+" INTEGER, "+ALBUMART+" CHARACTER(50), " +SONGURI+" CHARACTER(40) );";
        db.execSQL(query3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE3_NAME);
        onCreate(db);

    }

    public void insetintomostplayed(Songs songs)
    {
       SQLiteDatabase d=getReadableDatabase();
       Cursor c = d.rawQuery("SELECT * FROM "+ TABLE1_NAME+" WHERE "+SONGTILE+" = '"+songs.gettitle()+"'",null);
        if(c.getCount() == 0) {
            int n=1;
            SQLiteDatabase db = getWritableDatabase();
            String query = "INSERT INTO " + TABLE1_NAME + " (" + SONGTILE + " ," + ARTISTNAME + "," + DURATION + " ," + POSITION + " ," + ALBUMART + " ," + SONGURI + " ,"+NOOFPLAY+" ) VALUES ( \"" + songs.gettitle() + "\" , \"" + songs.getartist() + "\" , \"" + songs.getDuration() + "\" , \"" + songs.getPosition() + "\" , \"" + songs.getAlbumart() + "\" , \"" + songs.getSonguri() + "\", "+n+" );" ;
            db.execSQL(query);
        }
        else
        {
            c.moveToPosition(0);
            int a = c.getInt(c.getColumnIndex(NOOFPLAY))+1;
            String query = "UPDATE "+TABLE1_NAME+" SET "+NOOFPLAY+"="+a+" WHERE "+SONGTILE+" = '"+songs.gettitle()+"'";
            SQLiteDatabase s= getWritableDatabase();
            s.execSQL(query);
        }
        d.close();
        c.close();
    }
    public int insetintoliked(Songs songs)
    {
        SQLiteDatabase d=getReadableDatabase();
        Cursor c = d.rawQuery("SELECT * FROM "+ TABLE2_NAME+" WHERE "+SONGTILE+" = '"+songs.gettitle()+"'",null);
        if(c.getCount() == 0) {
            SQLiteDatabase db = getWritableDatabase();
            String query = "INSERT INTO " + TABLE2_NAME + " (" + SONGTILE + " ," + ARTISTNAME + "," + DURATION + " ," + POSITION + " ," + ALBUMART + " ," + SONGURI + " ) VALUES ( \"" + songs.gettitle() + "\" , \"" + songs.getartist() + "\" , \"" + songs.getDuration() + "\" , \"" + songs.getPosition() + "\" , \"" + songs.getAlbumart() + "\" , \"" + songs.getSonguri() + "\");" ;
            db.execSQL(query);
            c.close();
            db.close();
            d.close();
            return 1;
        }
        else
        {
            c.close();
            d.close();
            return 0;
        }
    }


    public void insetintorecentlyadded(Songs songs)
    {
        SQLiteDatabase db= getWritableDatabase();
        String query = "INSERT INTO "+TABLE1_NAME+" ("+SONGTILE+" ,"+ARTISTNAME+","+DURATION+" ,"+POSITION+" ,"+ALBUMART+" ," +SONGURI+ " ) VALUES ( '"+songs.gettitle()+"' , '"+songs.getartist()+"' , '"+songs.getDuration()+"' , '"+songs.getPosition()+"' , '"+songs.getAlbumart()+"' , '"+ songs.getSonguri()+"' );";
        db.execSQL(query);
        db.close();
    }

    public void insertintoplaylist(Songs songs,String plalistname)
    {
        plalistname = plalistname+"P";
        SQLiteDatabase db= getWritableDatabase();
        Log.d("album art -> "+songs.gettitle(),"  "+songs.getAlbumart());
        String query = "INSERT INTO '"+plalistname+"' ("+SONGTILE+" ,"+ARTISTNAME+","+DURATION+" ,"+POSITION+" ,"+ALBUMART+" ," +SONGURI+ " ) VALUES ( '"+songs.gettitle()+"' , '"+songs.getartist()+"' , '"+songs.getDuration()+"' , '"+songs.getPosition()+"' , '"+songs.getAlbumart()+"' , '"+ songs.getSonguri()+"' );";
        db.execSQL(query);
        db.close();
        Log.d("Inserted into "," "+plalistname+"P");
    }

    public ArrayList<Songs> getlikedsongs()
    {
        ArrayList<Songs> songs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase() ;
        String query = "SELECT * FROM "+TABLE2_NAME+" WHERE 1 ORDER BY "+SONGTILE +" ASC" ;
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            Songs s = new Songs();
            s.settitle(c.getString(c.getColumnIndex(SONGTILE)));
            s.setartist(c.getString(c.getColumnIndex(ARTISTNAME)));
            s.setDuration(Long.decode(c.getString(c.getColumnIndex(DURATION))));
            s.setPosition(c.getInt(c.getColumnIndex(POSITION)));
            s.setAlbumart(c.getString(c.getColumnIndex(ALBUMART)));
            s.setSonguri(Uri.parse(c.getString(c.getColumnIndex(SONGURI))));
            songs.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return songs;
    }

    public ArrayList<Songs> getmostplayedsongs()
    {
        ArrayList<Songs> songs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase() ;
        String query = "SELECT * FROM "+TABLE1_NAME+" WHERE 1 ORDER BY "+NOOFPLAY+" DESC" ;
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            if(c.getPosition()>15)
            {
                break;
            }
            Songs s = new Songs();
            s.settitle(c.getString(c.getColumnIndex(SONGTILE)));
            s.setartist(c.getString(c.getColumnIndex(ARTISTNAME)));
            s.setDuration(Long.decode(c.getString(c.getColumnIndex(DURATION))));
            s.setPosition(c.getInt(c.getColumnIndex(POSITION)));
            s.setAlbumart(c.getString(c.getColumnIndex(ALBUMART)));
            s.setSonguri(Uri.parse(c.getString(c.getColumnIndex(SONGURI))));
            songs.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return songs;
    }

    public ArrayList<Songs> getrecentlyadded()
    {
        ArrayList<Songs> songs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase() ;
        String query = "SELECT * FROM "+TABLE2_NAME+" WHERE 1" ;
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            Songs s = new Songs();
            s.settitle(c.getString(c.getColumnIndex(SONGTILE)));
            s.setartist(c.getString(c.getColumnIndex(ARTISTNAME)));
            s.setDuration(Long.decode(c.getString(c.getColumnIndex(DURATION))));
            s.setPosition(c.getInt(c.getColumnIndex(POSITION)));
            s.setAlbumart(c.getString(c.getColumnIndex(ALBUMART)));
            s.setSonguri(Uri.parse(c.getString(c.getColumnIndex(SONGURI))));
            songs.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return songs;
    }

    public void createTable(String Tablename)
    {
        Tablename = Tablename+"P";
        SQLiteDatabase db= getWritableDatabase();
        String query = "CREATE TABLE '" +Tablename
                +"' ("+SONGTILE+" CHARACTER(40), "+ARTISTNAME+" CHARACTER(25), "
                +DURATION+" CHARACTER(12), "+POSITION+" INTEGER, "+ALBUMART+" CHARACTER(50), " +SONGURI+" CHARACTER(40), " +NOOFPLAY+" INTEGER );";
        db.execSQL(query);

    }

    public int findtables(String tablename)
    {
        int flag = 0 ;
        SQLiteDatabase db= getReadableDatabase();
        String query = "select name from sqlite_master where type='table'";
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            String s = c.getString(c.getColumnCount()-1);
            Log.d("table "+c.getPosition()," name "+s);
            if(s.equals(tablename))
                flag = 1;

            c.moveToNext();
        }
        c.close();
        return flag ;
    }

    public ArrayList<Songs> getfromplaylist(String playlistname)
    {
        playlistname = playlistname+"P";
        ArrayList<Songs> songs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from '"+playlistname+"' where 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            Songs s = new Songs();
            s.settitle(c.getString(c.getColumnIndex(SONGTILE)));
            s.setartist(c.getString(c.getColumnIndex(ARTISTNAME)));
            s.setDuration(Long.decode(c.getString(c.getColumnIndex(DURATION))));
            s.setPosition(c.getInt(c.getColumnIndex(POSITION)));
            s.setAlbumart(c.getString(c.getColumnIndex(ALBUMART)));
            s.setSonguri(Uri.parse(c.getString(c.getColumnIndex(SONGURI))));
            songs.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return songs ;

    }



    public ArrayList<String> findplaylist()
    {
        ArrayList<String> playlist = new ArrayList<>();
        SQLiteDatabase db= getReadableDatabase();
        String query = "select name from sqlite_master where type='table'";
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            String s = c.getString(c.getColumnCount()-1);
            Log.d("table "+c.getPosition()," name "+s);
            if(s.endsWith("P")) {
                s =  s.substring(0,s.length()-1);
                playlist.add(s);
            }
            c.moveToNext();
        }
        c.close();
        return playlist;
    }

}
