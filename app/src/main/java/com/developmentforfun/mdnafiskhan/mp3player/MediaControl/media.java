package com.developmentforfun.mdnafiskhan.mp3player.MediaControl;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

/**
 * Created by mdnafiskhan on 09-01-2017.
 */

public class media extends MediaSessionCompat {

    public media(Context context, String tag) {
        super(context, tag);
    }



    public media(Context context, String tag, ComponentName mbrComponent, PendingIntent mbrIntent) {
        super(context, tag, mbrComponent, mbrIntent);
        Log.d("media button","pressed");

    }
}
