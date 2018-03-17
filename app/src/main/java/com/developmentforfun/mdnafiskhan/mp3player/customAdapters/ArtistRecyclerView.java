package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.NavigationControler;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.ArtistActivity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Artist_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Artists;
import com.developmentforfun.mdnafiskhan.mp3player.R;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 27/06/2017.
 */

public class ArtistRecyclerView extends RecyclerView.Adapter<ArtistRecyclerView.ViewHolder> {

    Context context;
    ArrayList<Artists> artist = new ArrayList<>();
    LayoutInflater layoutInflater;
    public ArtistRecyclerView(Context context, ArrayList<Artists> artist) {
        super();
        this.context= context;
        this.artist = artist;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.artistdetaillayout,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
          holder.artistName.setText(artist.get(position).getArtistname());
         if(artist.get(position).getNofosongs()>1)
          holder.noOfSOngs.setText(artist.get(position).getNofosongs()+" songs");
          else
             holder.noOfSOngs.setText(artist.get(position).getNofosongs()+" song");
             holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationControler.navigateToArtistActivityFragment(((FragmentActivity)context).getSupportFragmentManager(),artist.get(position).getArtistname(),artist.get(position).getArtistId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return artist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
          TextView artistName;
          TextView noOfSOngs;
          ConstraintLayout constraintLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cl) ;
            artistName = (TextView) itemView.findViewById(R.id.artistName);
            noOfSOngs = (TextView) itemView.findViewById(R.id.nofosongs);
        }

    }
}
