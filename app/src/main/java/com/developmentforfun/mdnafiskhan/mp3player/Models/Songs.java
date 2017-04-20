package com.developmentforfun.mdnafiskhan.mp3player.Models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by mdnafiskhan on 06-01-2017.
 */

public class Songs {

    String title ;
    Uri songuri;
    String album;
    int position;
    long Duration;
    String albumart;
    String artist;

    public void setSonguri( Uri songuri)
    {
        this.songuri= songuri;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }
    public int getPosition()
    {
        return this.position;
    }
    public void settitle(String title)
    {
        this.title = title;
    }
    public void setalbum(String album)
    {
       this.album =album;
    }
    public void setDuration(long duration)
    {

      this.Duration = duration;
    }

    public void setartist(String artist)
    {
      this.artist = artist;

    }

   public void setAlbumart(String albumart)
   {
       this.albumart = albumart;
   }

    public String getAlbumart()
    {
        return this.albumart;
    }

    public Uri getSonguri()
    {
        return this.songuri ;
    }

    public String gettitle()
    {
        return this.title ;
    }
    public String getalbum()
    {
        return this.album ;
    }
    public long getDuration()
    {

        return this.Duration ;
    }

    public String getartist()
    {
        return this.artist ;

    }



}
