package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.Interface.FIle;
import com.developmentforfun.mdnafiskhan.mp3player.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mdnafiskhan on 14/01/2018.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {


    ArrayList<File> folders = new ArrayList<>();
    ArrayList<Integer> count = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    FIle fIle;
    public FolderAdapter(Context context, ArrayList<File> folders, FIle fIle, ArrayList<Integer> count) {
        super();
        this.context =context;
        layoutInflater =LayoutInflater.from(context);
        this.folders = folders;
        this.fIle = fIle;
        this.count=count;
    }

    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.folder_recycler_view_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FolderAdapter.ViewHolder holder, int position) {
              holder.textView.setText(folders.get(position).getName().trim()+"");
              holder.textView2.setText(count.get(position)+" song");
              holder.textView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      fIle.onClick(holder.getAdapterPosition());
                  }
              });
    }
    @Override
    public int getItemCount() {
        Log.d("size of file",folders.size()+"");
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        TextView textView2;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text44);
            textView2 = (TextView) itemView.findViewById(R.id.count);
        }
    }


}
