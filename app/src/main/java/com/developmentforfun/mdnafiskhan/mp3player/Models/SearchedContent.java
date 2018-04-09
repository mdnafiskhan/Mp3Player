package com.developmentforfun.mdnafiskhan.mp3player.Models;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mdnafiskhan on 25/01/2018.
 */

public class SearchedContent {
    ArrayList<Songs> songses = new ArrayList<>();
    ArrayList<Artists> artistses = new ArrayList<>();
    ArrayList<Albums> albumses = new ArrayList<>();
    ArrayList<Genre> genres  =new ArrayList<>();
    HashMap<String,Integer> hashMap = new HashMap<>();
    ArrayList<Pair<String,Integer>> valPairList = new ArrayList<>();
    private int size=0;
    public SearchedContent(ArrayList<Songs> songses,ArrayList<Artists> artistses,ArrayList<Albums> albumses,ArrayList<Genre> genres) {
        super();
        this.songses=songses;
        this.artistses = artistses;
        this.albumses = albumses;
        this.genres = genres;
        set();
    }

    public ArrayList<Songs> getSongses() {
        return songses;
    }

    public void setSongses(ArrayList<Songs> songses) {
        this.songses = songses;
    }

    public ArrayList<Artists> getArtistses() {
        return artistses;
    }

    public void setArtistses(ArrayList<Artists> artistses) {
        this.artistses = artistses;
    }

    public ArrayList<Albums> getAlbumses() {
        return albumses;
    }

    public void setAlbumses(ArrayList<Albums> albumses) {
        this.albumses = albumses;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public ArrayList<Pair<String, Integer>> getValPairList() {
        return valPairList;
    }

    public void setValPairList(ArrayList<Pair<String, Integer>> valPairList) {
        this.valPairList = valPairList;
    }

    public HashMap<String, Integer> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Integer> hashMap) {
        this.hashMap = hashMap;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void set()
   {
       if(songses.size()!=0)
       {
           valPairList.add(new Pair<String, Integer>("Songs",0));
           size+=songses.size()+1;
       }
       else
       {
           valPairList.add(new Pair<String, Integer>("null",-1));
       }
       if(albumses.size()!=0)
       {
           if(songses.size()!=0)
           valPairList.add(new Pair<String, Integer>("Albums",songses.size()+1));
           else
           valPairList.add(new Pair<String, Integer>("Albums",0));
           size+=albumses.size()+1;

       }
       else
           valPairList.add(new Pair<String, Integer>("null",-1));
       if(artistses.size()!=0)
       {
          if(songses.size()!=0 && albumses.size()!=0)
           valPairList.add(new Pair<String, Integer>("Artists",songses.size()+albumses.size()+2));
          else if(songses.size()==0 && albumses.size()!=0)
          {
              valPairList.add(new Pair<String, Integer>("Artists",albumses.size()+1));
          }
          else if(songses.size()!=0 && albumses.size()==0)
          {
             // hashMap.put("Artists",songses.size()+1);
              valPairList.add(new Pair<String, Integer>("Artists",songses.size()+1));
          }
          else
          {
             // hashMap.put("Artists",0);
              valPairList.add(new Pair<String, Integer>("Artists",0));
          }
          size+=artistses.size()+1;
       }
       else
           valPairList.add(new Pair<String, Integer>("null",-1));
       if(genres.size()!=0)
       {
           if(songses.size()!=0 && albumses.size()!=0 && artistses.size()!=0)
             //  hashMap.put("Genres",songses.size()+albumses.size()+artistses.size()+3);
               valPairList.add(new Pair<String, Integer>("Genres",songses.size()+albumses.size()+artistses.size()+3));
           else if(songses.size()!=0 && albumses.size()!=0 && artistses.size()==0)
           {
              // hashMap.put("Genres",albumses.size()+songses.size()+2);
               valPairList.add(new Pair<String, Integer>("Genres",albumses.size()+songses.size()+2));

           }
           else if(songses.size()!=0 && albumses.size()==0 && artistses.size()==0)
           {
              // hashMap.put("Genres",songses.size()+1);
               valPairList.add(new Pair<String, Integer>("Genres",songses.size()+1));
           }
           else if(songses.size()==0 && albumses.size()!=0 && artistses.size()==0)
           {
              // hashMap.put("Genres",albumses.size()+1);
               valPairList.add(new Pair<String, Integer>("Genres",albumses.size()+1));
           }
           else if(songses.size()==0 && albumses.size()==0 && artistses.size()==0)
           {
              // hashMap.put("Genres",0);
               valPairList.add(new Pair<String, Integer>("Genres",0));
           }
           else if(songses.size()==0 && albumses.size()==0 && artistses.size()!=0)
           {
              // hashMap.put("Genres",artistses.size()+1);
               valPairList.add(new Pair<String, Integer>("Genres",artistses.size()+1));
           }
           else if(songses.size()==0 && albumses.size()!=0 && artistses.size()!=0)
           {
               //hashMap.put("Genres",artistses.size()+albumses.size()+2);
               valPairList.add(new Pair<String, Integer>("Genres",artistses.size()+albumses.size()+2));
           }
           else if(songses.size()!=0 && albumses.size()==0 && artistses.size()!=0 )
           {
             //  hashMap.put("Genres",artistses.size()+songses.size()+2);
               valPairList.add(new Pair<String, Integer>("Genres",artistses.size()+songses.size()+2));
           }
           size+=genres.size()+1;
       }
       else
           valPairList.add(new Pair<String, Integer>("null",-1));
   }
}
