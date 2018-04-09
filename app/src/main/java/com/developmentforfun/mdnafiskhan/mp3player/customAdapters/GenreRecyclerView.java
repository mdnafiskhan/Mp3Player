package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Activities.ActivityFragments.NavigationControler;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Artist_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Fragment_Container_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Activities.Genre_Activity;
import com.developmentforfun.mdnafiskhan.mp3player.Models.Genre;
import com.developmentforfun.mdnafiskhan.mp3player.R;

import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 16/01/2018.
 */

public class GenreRecyclerView extends RecyclerView.Adapter<GenreRecyclerView.ViewHolder> {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<Genre> genre = new ArrayList<>();
    public GenreRecyclerView(Context context, ArrayList<Genre> genre) {
        super();
        this.context = context;
        this.genre = genre;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.artistdetaillayout,null);
        return new GenreRecyclerView.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
         holder.Genre.setText(genre.get(position).getNameGenre()+"");
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle b= new Bundle();
                 b.putString("GenreId",genre.get(holder.getAdapterPosition()).getId());
                 b.putString("GenreName",genre.get(holder.getAdapterPosition()).getNameGenre());
                 b.putString("which","GenreDetail");
                 Intent i = new Intent(context, Fragment_Container_Activity.class);
                 i.putExtras(b);
                 context.startActivity(i);
                // NavigationControler.navigateToGenreActivityFragment(((FragmentActivity)context).getSupportFragmentManager(), genre.get(holder.getAdapterPosition()).getNameGenre(), genre.get(holder.getAdapterPosition()).getId());
             }
         });
    }

    @Override
    public int getItemCount() {
        return genre.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Genre;
        public ViewHolder(View itemView) {
            super(itemView);
            Genre = (TextView) itemView.findViewById(R.id.artistName);
        }
    }
}
