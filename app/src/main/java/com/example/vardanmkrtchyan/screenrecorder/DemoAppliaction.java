package com.example.vardanmkrtchyan.screenrecorder;

import android.app.Application;

import com.example.screenrecordercore.VideoLytics;

/**
 * Created by Vardan Mkrtchyan on 4/14/2018.
 */

public class DemoAppliaction extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        VideoLytics.start(this);
    }
}
