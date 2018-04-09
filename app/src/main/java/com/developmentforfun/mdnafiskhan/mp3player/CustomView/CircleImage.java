package com.developmentforfun.mdnafiskhan.mp3player.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by mdnafiskhan on 28/06/2017.
 */

public class CircleImage extends View {
    Bitmap bitmap;
    int pw;
    int ph;
    int w;
    int h;
    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        w = getWidth();
        h = getHeight();
        Log.d("w",""+w);
        Log.d("h",""+h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        if(bitmap == null)
        {
            bitmap = BitmapFactory.decodeResource(getResources(), android.support.design.R.drawable.abc_ab_share_pack_mtrl_alpha);
        }
        bitmap = getCroppedBitmap(bitmap);
        canvas.drawBitmap(bitmap,0,0,paint);
        Log.d("onDraw","Called");

    }
    public void setBitmap(Bitmap bitmap)
    {
        w = getWidth();
        h = getHeight();
        Log.d("w",""+w);
        Log.d("h",""+h);
           this.bitmap = bitmap;
           invalidate();
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
        bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        int small;
         if(w<h)
              small = w;
         else
              small = h;
        final Rect rect = new Rect(0, 0,small,small);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(small/2,  small/ 2,
                small/ 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return output;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        pw = MeasureSpec.getSize(widthMeasureSpec);
        ph = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        invalidate();
    }


}
