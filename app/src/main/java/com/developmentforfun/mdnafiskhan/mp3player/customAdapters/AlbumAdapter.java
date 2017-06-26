package com.developmentforfun.mdnafiskhan.mp3player.customAdapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developmentforfun.mdnafiskhan.mp3player.R;
import com.developmentforfun.mdnafiskhan.mp3player.SongLoader.songDetailloader;

import java.util.ArrayList;


/**
 * Created by mdnafiskhan on 04-01-2017.
 */

public class AlbumAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    songDetailloader songloader = new songDetailloader();
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    Cursor cursor,cursor2;

    int width;
        TextView nofsongs;
        TextView albumname;
        ImageView imageView;
    public AlbumAdapter(final Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        this.context =context;
       //songloader.set(context);
        Point size = new Point();
        display.getSize(size);
        this.width = size.x;
    }
   public void setCursor()
   {
       this.cursor= songloader.getAlbumCursor(context);
       this.cursor2= songloader.getAlbumCursor(context);
   }
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
          if(convertView==null)
            convertView = layoutInflater.inflate(R.layout.album_gridview, parent, false);
            this.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            this.nofsongs =(TextView) convertView.findViewById(R.id.textView);
            this.albumname =(TextView) convertView.findViewById(R.id.albumname);
          //  this.imageView.setImageResource(R.mipmap.black);
        this.imageView.setImageResource(R.drawable.default_album_small);

        RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(width/2,width/2);
        this.imageView.setLayoutParams(layoutParams);
        //below line of code genrating error on activtiy restart........
      //  if(bitmaps.get(position)!= null)
     //   this.imageView.setImageBitmap(bitmaps.get(position));
      //  else
     //   {
     //       this.imageView.setImageResource(R.drawable.default_album_small);
     //   }
      //  setCursor();
        new get(this.nofsongs,this.albumname,position,this.imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return convertView;
    }

    public class get extends AsyncTask<Void,Void,Void>{

        TextView albumname;
        ImageView imageView;
        int position;
        Bitmap b;
        String a;

        public get(TextView n,TextView al,int position,ImageView imageView) {
            super();
            this.albumname = al ;
            this.imageView = imageView;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            cursor.moveToPosition(position);
            Log.d("curr->",""+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
            a=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize= 2;
            b=BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)),options);
            if(b==null)
            {
                b=BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            albumname.setText(a);
            if(b!=null)
            imageView.setImageBitmap(b);
            else
                imageView.setImageResource(R.drawable.default_album_small_light);

        }
    }
}
