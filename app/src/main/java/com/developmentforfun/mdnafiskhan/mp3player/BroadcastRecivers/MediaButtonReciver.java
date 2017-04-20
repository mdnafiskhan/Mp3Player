package com.developmentforfun.mdnafiskhan.mp3player.BroadcastRecivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by mdnafiskhan on 01-02-2017.
 */

public class MediaButtonReciver extends BroadcastReceiver {
    public MediaButtonReciver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Finaly i recived it","");
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        Log.d("In keycode","");
                        break;
                }
            }
        }
    }
}
