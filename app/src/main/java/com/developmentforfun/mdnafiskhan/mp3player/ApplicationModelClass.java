package com.developmentforfun.mdnafiskhan.mp3player;

import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;

/**
 * Created by mdnafiskhan on 13/03/2018.
 */

public class ApplicationModelClass {

    public Songs CurrentPlayingSong = new Songs();

    public Songs getCurrentPlayingSong() {
        return CurrentPlayingSong;
    }

    public void setCurrentPlayingSong(Songs currentPlayingSong) {
        CurrentPlayingSong = currentPlayingSong;
    }

    public ApplicationModelClass() {
        super();
    }
}
