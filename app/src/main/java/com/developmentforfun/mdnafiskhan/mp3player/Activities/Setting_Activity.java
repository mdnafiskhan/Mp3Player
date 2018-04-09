package com.developmentforfun.mdnafiskhan.mp3player.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.developmentforfun.mdnafiskhan.mp3player.R;



import java.util.List;

/**
 * Created by mdnafiskhan on 20/03/2018.
 */
public class Setting_Activity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new Setting_Fragment()).commit();
    }

    /**
     * Populate the activity with the top-level headers.
     */
    /**
     * This fragment shows the preferences for the first header.
     */

    public static class Setting_Fragment extends PreferenceFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void addPreferencesFromIntent(Intent intent) {
            super.addPreferencesFromIntent(intent);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            //   PreferenceManager.setDefaultValues(getActivity(),
            //         R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragmented_preferences);
        }

        @Override
        public void addPreferencesFromResource(int preferencesResId) {
            super.addPreferencesFromResource(preferencesResId);
        }
    }
}
    /**
     * This fragment shows the preferences for the first header.
     */
