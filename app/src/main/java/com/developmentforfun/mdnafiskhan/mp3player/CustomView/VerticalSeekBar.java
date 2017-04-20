package com.developmentforfun.mdnafiskhan.mp3player.CustomView;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar implements SeekBar.OnSeekBarChangeListener {

    int progress;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    public synchronized void setProgress(int progress)  // it is necessary for calling setProgress on click of a button
    {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Action down","Progress ="+progress);
            case MotionEvent.ACTION_MOVE:
                Log.d("Action move","Progress ="+progress);
            case MotionEvent.ACTION_UP:
                if(event.getY()<=getHeight() && event.getY()>=0) {
                    progress = getMax() - (int) (getMax() * event.getY() / getHeight());
                    Log.d("Action up", "Progress =" + progress);
                    Log.d("GetMax Return", " " + getMax() + " getHeight " + getHeight() + " getY " + event.getY());
                    this.setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(l);
        Log.d("setonseekbarchnerCalled","  ...................");
        l.onProgressChanged(this,this.progress,true);
        l.onStartTrackingTouch(this);
        l.onStopTrackingTouch(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}