package com.developmentforfun.mdnafiskhan.mp3player.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mdnafiskhan on 14-03-2017.
 */

public class Equaliser extends View {

    private int x ;
    private int y;
    int flag=0;
    private Paint paint = new Paint();
    private Path path = new Path();


    public Equaliser(Context context) {
        super(context);
        init(null,0);
    }

    public Equaliser(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public Equaliser(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    public void init( AttributeSet attrs, int defStyleAttr)
    {
     paint.setStrokeWidth(20);
     paint.setAntiAlias(true);
     paint.setColor(Color.BLACK);
     paint.setStyle(Paint.Style.STROKE);
        x=getWidth();
        y=getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(0, getHeight(), getWidth() / 2, paint);
        canvas.drawCircle(getWidth(), getHeight(), getWidth() / 2, paint);
        Log.d("Call","OnDraw gets called");
            canvas.drawPath(path,paint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        flag=1;
             path.moveTo(0,getHeight());
             path.lineTo(x,y);
        Log.d("Call","Caling invalidate");
        invalidate();
        return true ;
    }
}
