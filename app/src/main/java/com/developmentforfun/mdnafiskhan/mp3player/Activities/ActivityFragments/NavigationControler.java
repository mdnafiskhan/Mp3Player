package com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.developmentforfun.mdnafiskhan.mp3player.R;

/**
 * Created by mdnafiskhan on 16/03/2018.
 */

public class NavigationControler {

    public static void navigateToAlbumDetailFragment(FragmentManager fragmentManager, Bundle bundle){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AlbumDetailFragment albumDetailFragment = AlbumDetailFragment.newInstance();
        albumDetailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_container, albumDetailFragment, AlbumDetailFragment.class.getName());
        fragmentTransaction.commit();
    }

    public static void removeAlbumDetailFragment(FragmentManager fragmentManager)
    {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment!=null)
        {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        else
        {

        }
    }

    public static void navigateToNowPlayingList(FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NowPlayingSongList_Fragment nowPlayingSongList_fragment = NowPlayingSongList_Fragment.getInstance();
        fragmentTransaction.replace(R.id.fragment_container, nowPlayingSongList_fragment, PlayerActivityFragment.class.getName());
        fragmentTransaction.commit();
    }
    public static void navigateToPlayerFragment(FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlayerActivityFragment playerActivityFragment = PlayerActivityFragment.getNewInstance();
        fragmentTransaction.replace(R.id.fragment_container, playerActivityFragment, PlayerActivityFragment.class.getName());
        fragmentTransaction.commit();
    }

    public static void navigateToArtistActivityFragment(FragmentManager fragmentManager, String artist,String artistId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ArtistActivityFragment artistActivityFragment = ArtistActivityFragment.getNewInstance();
        Bundle b = new Bundle();
        b.putCharSequence("artist",artist);
        b.putCharSequence("artistId",artistId);
        artistActivityFragment.setArguments(b);
        fragmentTransaction.replace(R.id.fragment_container, artistActivityFragment, ArtistActivityFragment.class.getName());
        fragmentTransaction.commit();
    }

    public static void navigateToGenreActivityFragment(FragmentManager fragmentManager, String genre,String genreId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GenreActivityFragment genreActivityFragment = GenreActivityFragment.getNewInstance();
        Bundle b = new Bundle();
        b.putCharSequence("genre",genre);
        b.putCharSequence("genreId",genreId);
        genreActivityFragment.setArguments(b);
        fragmentTransaction.replace(R.id.fragment_container, genreActivityFragment, GenreActivityFragment.class.getName());
        fragmentTransaction.commit();
    }

    public static void navigateToEqualiser(FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EqualiserAc equaliserAc = EqualiserAc.getInstance();
        fragmentTransaction.replace(R.id.fragment_container, equaliserAc, EqualiserAc.class.getName());
        fragmentTransaction.commit();
    }

    public static void navigateToPlaylistFragment(FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlaylistFragmentActivity playlistFragmentActivity = PlaylistFragmentActivity.getInstance();
        fragmentTransaction.replace(R.id.fragment_container, playlistFragmentActivity, PlaylistFragmentActivity.class.getName());
        fragmentTransaction.commit();
    }


}
