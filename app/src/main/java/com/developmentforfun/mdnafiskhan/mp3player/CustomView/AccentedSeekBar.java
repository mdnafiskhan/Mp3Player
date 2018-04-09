package com.developmentforfun.mdnafiskhan.mp3player.CustomView;

/**
 * Created by mdnafiskhan on 23/01/2018.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.developmentforfun.mdnafiskhan.mp3player.R;

public class AccentedSeekBar extends AppCompatSeekBar {
    public AccentedSeekBar(Context context) {
        super(context);
        a();
    }

    public AccentedSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a();
    }

    public AccentedSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        a();
    }

    private void a() {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes;
            if (VERSION.SDK_INT >= 21) {
                obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{R.color.colorPrimary});
            } else {
                obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{R.color.colorPrimary});
            }
            int color = obtainStyledAttributes.getColor(0, 0);
            try {
                Drawable progressDrawable = getProgressDrawable();
                if (progressDrawable != null) {
                    progressDrawable.setColorFilter(color, Mode.SRC_IN);
                }
            } catch (Throwable th) {

            }
        }
    }
}
