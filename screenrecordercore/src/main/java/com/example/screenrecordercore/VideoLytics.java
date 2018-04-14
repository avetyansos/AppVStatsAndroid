package com.example.screenrecordercore;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by Vardan Mkrtchyan on 4/14/2018.
 */

public class VideoLytics {

    public static void start(Application application){
        //Start recording
        final ScreenRecord screenRecord = new ScreenRecord(application);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
            @Override
            public void onActivityStarted(Activity activity) {

            }
            @Override
            public void onActivityResumed(Activity activity) {
                Recorder recorder = screenRecord.getRecorder();
                recorder.setActivity(activity);
                if(!recorder.isRecording()){
                    screenRecord.start();
                }
            }
            @Override
            public void onActivityPaused(Activity activity) {
                screenRecord.stop();
            }
            @Override
            public void onActivityStopped(Activity activity) {

            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

}
