package com.developmentforfun.mdnafiskhan.mp3player.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mdnafiskhan on 23/01/2018.
 */

public class EqualizerSeekbar extends AccentedSeekBar {
    private OnSeekBarChangeListener a;

    public EqualizerSeekbar(Context context) {
        super(context);
    }

    public EqualizerSeekbar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public EqualizerSeekbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i2, i, i4, i3);
    }

    protected synchronized void onMeasure(int i, int i2) {
        super.onMeasure(i2, i);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas canvas) {
        canvas.rotate(-90.0f);
        canvas.translate((float) (-getHeight()), 0.0f);
        super.onDraw(canvas);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.a = onSeekBarChangeListener;
        super.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 0:
                setProgress(getMax() - ((int) ((((float) getMax()) * motionEvent.getY()) / ((float) getHeight()))));
                if (this.a != null) {
                    this.a.onStartTrackingTouch(this);
                    break;
                }
                break;
            case 1:
                setProgress(getMax() - ((int) ((((float) getMax()) * motionEvent.getY()) / ((float) getHeight()))));
                if (this.a != null) {
                    this.a.onStopTrackingTouch(this);
                    break;
                }
                break;
            case 2:
                setProgress(getMax() - ((int) ((((float) getMax()) * motionEvent.getY()) / ((float) getHeight()))));
                invalidate();
                break;
            case 3:
                if (this.a != null) {
                    this.a.onStopTrackingTouch(this);
                    break;
                }
                break;
        }
        return true;
    }

    public void setProgress(int i) {
        super.setProgress(i);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }
}
