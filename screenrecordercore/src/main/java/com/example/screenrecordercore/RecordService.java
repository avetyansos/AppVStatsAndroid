package com.example.screenrecordercore;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.example.screenrecordercore.Model.Place;
import com.example.screenrecordercore.Model.Size;


public class RecordService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }
        startRecording();
        return START_STICKY;
    }

    private void startRecording(){
        if (!Utility.getRecorder().isRecording()) {
            Recorder recorder = Utility.getRecorder();
            recorder.startRecording();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
