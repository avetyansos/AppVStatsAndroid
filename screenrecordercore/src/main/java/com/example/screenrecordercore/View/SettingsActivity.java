package com.example.screenrecordercore.View;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.example.screenrecordercore.RecordService;
import com.halilibo.screenrecorddebug.R;

/**
 * Created by Vardan Mkrtchyan on 4/13/2018.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
    @Override
    public void onStop(){
        super.onStop();
        Intent intent = new Intent(this, RecordService.class);
        intent.setAction("update_settings");
        startService(intent);
    }
}
