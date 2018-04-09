package com.developmentforfun.mdnafiskhan.mp3player.CustomView;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mdnafiskhan on 18/01/2018.
 */

public class ScrollingTextView extends AppCompatTextView {

    public ScrollingTextView(Context context) {
        super(context);
    }

    public ScrollingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if(focused)
            super.onWindowFocusChanged(focused);
    }


    @Override
    public boolean isFocused() {
        return true;
    }
}
