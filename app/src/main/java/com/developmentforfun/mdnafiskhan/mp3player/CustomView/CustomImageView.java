package com.developmentforfun.mdnafiskhan.mp3player.CustomView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by mdnafiskhan on 27/06/2017.
 */

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {

    float x_down;
    float x_up;
    float x;
    float y_down;
    float y_up;
    float y;
    float thisx;
    float thisy;
    AttributeSet attributeSet;

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attributeSet = attrs;
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attributeSet = attrs;
    }

    @Override
    public void setX( float x) {
        super.setX(x);
    }
    public void init()
    {
        thisx = this.getX();
    }

    @Override
    public void setTranslationX( float translationX) {
        super.setTranslationX(translationX);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP  :
                Log.d("MotionEvent","action up");
                x_up = event.getX();
                y_up = event.getY();
                ObjectAnimator.ofFloat(this,"translationX", 0).setDuration(200).start();

                break;
            case MotionEvent.ACTION_HOVER_ENTER :
                Log.d("MotionEvent","Action hover enter");
                break;
            case MotionEvent.ACTION_HOVER_EXIT :
                Log.d("MotionEvent","Action hover exit");
                break;
            case MotionEvent.ACTION_MOVE :
                Log.d("MotionEvent","Action move");
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_DOWN :
                Log.d("MotionEvent","Action down");
                ObjectAnimator.ofFloat(this,"translationX", -100).setDuration(200).start();
                thisx = event.getX();
                y_down = event.getY();

                break;
            case MotionEvent.ACTION_SCROLL:
                Log.d("MotionEvent","Action scroll");
                Log.d("coordinates","x="+event);
                break;
        }

        return true;
    }

}
