package com.developmentforfun.mdnafiskhan.mp3player.Service;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by mdnafiskhan on 18/01/2018.
 */

public class MusicService2 {

    public ArrayList<Songs> currentPlayingList = new ArrayList<>();
    public ArrayList<Songs> suffleListSong = new ArrayList<>();
    public Stack<Songs> songsAlreadyPlayed = new Stack<>();
    public int currentPlayingPosition;
    public Songs currentPlayingSong;
    public Songs prevSong;
    public Songs nextSong;

    boolean shuffle;
    int repeatTime = 0;  // 0 -> no repeat ,1-> repeat once,2-> multipleRepeat ;



    public void setRepeatTime(int val)
    {
        this.repeatTime = val;
    }

    public void setShuffle(boolean val, ArrayList<Songs> shufflePlaylist)
    {
        this.shuffle = val ;
        if(shufflePlaylist!=null) {
            this.suffleListSong = shufflePlaylist;
            this.currentPlayingList = shufflePlaylist;
        }
        else
        {
            shufflePlaylist = currentPlayingList;
        }
    }

    public void setShuffleOff()
    {
        this.shuffle=false;
    }

    public void setCurrentPlayingList(ArrayList<Songs> songs , int position)
    {
        this.currentPlayingList = songs;
        this.currentPlayingPosition = position;
    }

    public Songs getNextSongToPlay()
    {
       Songs songs = new Songs();
          switch (repeatTime) {
              case 0:
                  if (currentPlayingPosition < currentPlayingList.size() - 1) {

                      songs = currentPlayingList.get(currentPlayingPosition + 1);
                      currentPlayingPosition++;
                  } else {
                      songs = currentPlayingList.get(0);
                      currentPlayingPosition = 0;
                      return null;
                  }
                  break;
              case 1:
                  if (currentPlayingPosition < currentPlayingList.size() - 1) {
                      songs = currentPlayingList.get(currentPlayingPosition + 1);
                      currentPlayingPosition++;
                  } else {
                      songs = currentPlayingList.get(0);
                      currentPlayingPosition = 0;
                  }
                  break;
              case 2:
                  songs = currentPlayingSong;
                  break;
          }
       return songs;
    }

    public Songs getPrevSongToPlay()
    {
        if(currentPlayingPosition==0)
        {
            return currentPlayingList.get(currentPlayingList.size()-1);
        }
        else
        {
           return currentPlayingList.get(--currentPlayingPosition);
        }
    }

    public static void shuffleList(ArrayList<Songs> songses) {
        int n = songses.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(songses, i, change);
        }
    }

    private static void swap(ArrayList<Songs> a, int i, int change) {
        Songs helper = a.get(i);
        a.set(i, a.get(change));
        a.set(change, helper);
    }



}
