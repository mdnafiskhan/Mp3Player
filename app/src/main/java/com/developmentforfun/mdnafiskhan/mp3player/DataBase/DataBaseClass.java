package com.developmentforfun.mdnafiskhan.mp3player.DataBase;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

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
    private static String TABLE4_NAME = "bandValue";

    private static String SONGTILE="title" ;
    private static String ARTISTNAME="artist" ;
    private static String DURATION ="duration";
    private static String POSITION ="position" ;
    private static String ALBUMART = "albumart";
    private static String SONGURI = "songuri";
    private static String ALBUM = "albumname";
    private static String ALBUMID = "albumId";
    private static String NOOFPLAY = "noofplay";
    private static String BAND0 = "band0";
    private static String BAND1 = "band1";
    private static String BAND2 = "band2";
    private static String BAND3 = "band3";
    private static String BAND4 = "band4";

    SQLiteDatabase db = getReadableDatabase();
    public DataBaseClass(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String query = "CREATE TABLE " +TABLE1_NAME
               +" ("+SONGTILE+" CHARACTER(40), "+ARTISTNAME+" CHARACTER(30), "+ALBUM+" CHARACTER(30),"
               +DURATION+" CHARACTER(12), "+POSITION+" INTEGER, "+ALBUMART+" CHARACTER(50), " +SONGURI+" CHARACTER(40), " +NOOFPLAY+" INTEGER , " +ALBUMID+" CHARACTER(40) );";
         db.execSQL(query);

        String query2 = "CREATE TABLE " +TABLE2_NAME
                +" ("+SONGTILE+" CHARACTER(40), "+ARTISTNAME+" CHARACTER(25), "+ALBUM+" CHARACTER(30),"
                +DURATION+" CHARACTER(12), "+POSITION+" INTEGER, "+ALBUMART+" CHARACTER(50), " +SONGURI+" CHARACTER(40)  , " +ALBUMID+" CHARACTER(40) );";
        db.execSQL(query2);

        String query3 = "CREATE TABLE " +TABLE3_NAME
                +" ("+SONGTILE+" CHARACTER(40), "+ARTISTNAME+" CHARACTER(25), "+ALBUM+" CHARACTER(30),"
                +DURATION+" CHARACTER(12), "+POSITION+" INTEGER, "+ALBUMART+" CHARACTER(50), " +SONGURI+" CHARACTER(40)  , " +ALBUMID+" CHARACTER(40) );";
        db.execSQL(query3);
        String query4 = "CREATE TABLE " +TABLE4_NAME
                +" ("+BAND0+" INTEGER, "+BAND1+" INTEGER, "+BAND2+" INTEGER,"
                +BAND3+" INTEGER, "+BAND4+" );";
        db.execSQL(query4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE3_NAME);
        onCreate(db);

    }

  /*  public void insetintomostplayed(Songs songs)
    {
       SQLiteDatabase d=getReadableDatabase();
       Cursor c = d.rawQuery("SELECT * FROM "+ TABLE1_NAME+" WHERE "+SONGTILE+" = '"+songs.gettitle()+"'",null);
        if(c.getCount() == 0) {
            int n=1;
            SQLiteDatabase db = getWritableDatabase();
            String query = "INSERT INTO " + TABLE1_NAME + " (" + SONGTILE + " ," + ARTISTNAME + ","+ALBUM+" ," + DURATION + " ," + POSITION + " ," + ALBUMART + " ," + SONGURI + " ,"+NOOFPLAY+","+ALBUMID+" ) VALUES ( \"" + songs.gettitle() + "\" , \"" + songs.getartist() + "\" ,\"" + songs.getalbum() + "\", \"" + songs.getDuration() + "\" , \"" + songs.getPosition() + "\" , \"" + songs.getAlbumart() + "\" , \"" + songs.getSonguri() + "\", "+n+", \"" + songs.getAlbumId() + "\" );" ;
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
            String query = "INSERT INTO " + TABLE2_NAME + " (" + SONGTILE + " ," + ARTISTNAME + ","+ALBUM+" ," + DURATION + " ," + POSITION + " ," + ALBUMART + " ," + SONGURI + " ," + ALBUMID + " ) VALUES ( \"" + songs.gettitle() + "\" , \"" + songs.getartist() + "\" ,\"" + songs.getalbum() + "\", \"" + songs.getDuration() + "\" , \"" + songs.getPosition() + "\" , \"" + songs.getAlbumart() + "\" , \"" + songs.getSonguri() + "\" , \"" + songs.getAlbumId() + "\");" ;
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
*/
    public void save(int band0,int band1,int band2,int band3,int band4)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE4_NAME);
        String query = "INSERT INTO " + TABLE4_NAME + " VALUES(" + band0 + " ," + band1 + ","+band2+" ," + band3 + " ," + band4 +");" ;
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Integer> getBands()
    {
        ArrayList<Integer> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase() ;
        String query = "SELECT * FROM "+TABLE4_NAME+" WHERE 1" ;
        Cursor c= db.rawQuery(query,null);
        Log.d("count =",c.getCount()+"");
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            arrayList.add(c.getInt(c.getColumnIndex(BAND0)));
            arrayList.add(c.getInt(c.getColumnIndex(BAND1)));
            arrayList.add(c.getInt(c.getColumnIndex(BAND2)));
            arrayList.add(c.getInt(c.getColumnIndex(BAND3)));
            arrayList.add(c.getInt(c.getColumnIndex(BAND4)));
            c.moveToNext();
        }
        c.close();
        return arrayList;

    }


    public void insetintorecentlyadded(Songs songs)
    {
        SQLiteDatabase db= getWritableDatabase();
        String query = "INSERT INTO "+TABLE1_NAME+" ("+SONGTILE+" ,"+ARTISTNAME+","+ALBUM+" ,"+DURATION+" ,"+POSITION+" ,"+ALBUMART+" ," +SONGURI+ "," +ALBUMID+ " ) VALUES ( '"+songs.gettitle()+"' , '"+songs.getartist()+"' , '"+songs.getDuration()+"' , '"+songs.getPosition()+"' , '"+songs.getAlbumart()+"' , '"+ songs.getSonguri()+"', '"+ songs.getAlbumId()+"' );";
        db.execSQL(query);
        db.close();
    }
/*
    public boolean insertintoplaylist(Songs songs,String plalistname)
    {
        plalistname = plalistname+"P";
        SQLiteDatabase db= getWritableDatabase();
        Log.d("song uri -> "+songs.gettitle(),"  "+songs.getSonguri());
        String sqlQuery = "SELECT * FROM " + plalistname + " WHERE " + SONGURI + " = " + "\""
                + songs.getSonguri() + "\"";

        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null && c.getCount() != 0) {
            c.close();
            return false;
        } else {
            if(c.getCount()==0 && c!=null)
                c.close();
            String query = "INSERT INTO '" + plalistname + "' (" + SONGTILE + " ," + ARTISTNAME + "," + ALBUM + " ," + DURATION + " ," + POSITION + " ," + ALBUMART + " ," + SONGURI + " ," + ALBUMID + " ) VALUES ( '" + songs.gettitle() + "' , '" + songs.getartist() + "' , '" + songs.getalbum() + "' , '" + songs.getDuration() + "' , '" + songs.getPosition() + "' , '" + songs.getAlbumart() + "' , '" + songs.getSonguri() + "', '" + songs.getAlbumId()+ "' );";
            db.execSQL(query);
            db.close();
            Log.d("Inserted into ", " " + plalistname + "P");
            return true;
        }
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
            s.settitle(c.getString(c.getColumnIndex(ALBUMID)));
            s.setartist(c.getString(c.getColumnIndex(ARTISTNAME)));
            s.setDuration(Long.decode(c.getString(c.getColumnIndex(DURATION))));
            s.setPosition(c.getInt(c.getColumnIndex(POSITION)));
            s.setAlbumart(c.getString(c.getColumnIndex(ALBUMART)));
            s.setalbum(c.getString(c.getColumnIndex(ALBUM)));
            s.setSongId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
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
            s.setalbum(c.getString(c.getColumnIndex(ALBUM)));
            s.setalbum(c.getString(c.getColumnIndex(ALBUMID)));
            s.setDuration(Long.decode(c.getString(c.getColumnIndex(DURATION))));
            s.setPosition(c.getInt(c.getColumnIndex(POSITION)));
            s.setAlbumart(c.getString(c.getColumnIndex(ALBUMART)));
            s.setSongId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
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
            s.setalbum(c.getString(c.getColumnIndex(ALBUM)));
            s.setalbum(c.getString(c.getColumnIndex(ALBUMID)));
            s.setPosition(c.getInt(c.getColumnIndex(POSITION)));
            s.setAlbumart(c.getString(c.getColumnIndex(ALBUMART)));
            s.setSongId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
            s.setSonguri(Uri.parse(c.getString(c.getColumnIndex(SONGURI))));
            songs.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return songs;
    }
    */

}
