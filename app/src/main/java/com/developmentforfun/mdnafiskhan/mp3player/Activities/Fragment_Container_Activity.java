package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.NavigationControler;
import com.developmentforfun.mdnafiskhan.mp3player.R;

/**
 * Created by mdnafiskhan on 23/03/2018.
 */

public class Fragment_Container_Activity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_activity);
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            String which = b.getString("which");
            switch (which) {
                case "PlayerFragment" :
                    NavigationControler.navigateToPlayerFragment(getSupportFragmentManager());
                    break;
                case "Equaliser" :
                    NavigationControler.navigateToEqualiser(getSupportFragmentManager());
                    break;
                case "AlbumDetail" :
                    Bundle bundle= new Bundle();
                    bundle.putCharSequence("albumName",b.getString("albumName"));
                    bundle.putCharSequence("albumId",b.getString("albumId"));
                    NavigationControler.navigateToAlbumDetailFragment(getSupportFragmentManager(),bundle);
                    break;
                case "ArtistDetail" :
                    NavigationControler.navigateToArtistActivityFragment(getSupportFragmentManager(),b.getString("ArtistName"),b.getString("ArtistId"));
                    break;
                case "GenreDetail" :
                    NavigationControler.navigateToGenreActivityFragment(getSupportFragmentManager(),b.getString("GenreName"),b.getString("GenreId"));
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
