package com.developmentforfun.mdnafiskhan.mp3player.Sorting;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mdnafiskhan on 18/03/2018.
 */

public class SortSongs {

    public static ArrayList<Songs> sortByDateAdded(ArrayList<Songs> songs)
    {
        ArrayList<Songs> songs1 = new ArrayList<>();
        for(int i=0;i<songs.size();i++)
            songs1.add(songs.get(i));
        Collections.sort(songs1,getCompByDateAdded());
        return songs1;
    }

   private static Comparator<Songs> getCompByDateAdded() {
        return new Comparator<Songs>() {
            @Override
            public int compare(Songs s1, Songs s2) {
                return s2.getDateAdded().compareTo(s1.getDateAdded());
            }
        };
    }

}
