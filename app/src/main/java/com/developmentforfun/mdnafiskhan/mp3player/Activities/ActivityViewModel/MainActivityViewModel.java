package com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.NowPlayingChanged;
import com.developmentforfun.mdnafiskhan.mp3player.Interface.SongIsLoaded;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Songs;
import com.developmentforfun.mdnafiskhan.mp3player.Mp3PlayerApplication;

/**
 * Created by mdnafiskhan on 15/03/2018.
 */

public class MainActivityViewModel extends ViewModel implements NowPlayingChanged,SongIsLoaded {

    private MutableLiveData<Songs> nowPlaying = new MutableLiveData<>();
    private MutableLiveData<Boolean> requestToOpenFragment = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateYourSelf = new MutableLiveData<>();

    public MainActivityViewModel() {
        super();
        Mp3PlayerApplication.setInterface(this);
        Mp3PlayerApplication.applicationSongsContent.setInterface(this);
        try {
            nowPlaying.postValue(Mp3PlayerApplication.applicationModelClass.CurrentPlayingSong);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void requestView(boolean request)
    {
        requestToOpenFragment.postValue(request);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("msg","live data is cleared");
    }

    public LiveData<Boolean> getRequestToOpenFragment()
    {
        return requestToOpenFragment;
    }

    public LiveData<Songs> getNowPlaying()
    {
        return nowPlaying;
    }

    public LiveData<Boolean> getUpdateYourSelf()
    {
        return updateYourSelf;
    }

    @Override
    public void setSong(Songs nowPlaying) {
        Log.d("msg","going to post value");
        Log.d("msg",""+nowPlaying.toString());
        this.nowPlaying.postValue(nowPlaying);
    }

    @Override
    public void songLoaded() {
        Log.d("msg","trigger is called");
        if(updateYourSelf.getValue()!=null)
        updateYourSelf.postValue(!updateYourSelf.getValue());
        else
        {
            updateYourSelf.postValue(true);
        }
    }
}

