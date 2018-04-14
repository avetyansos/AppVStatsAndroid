package com.example.screenrecordercore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.example.screenrecordercore.Interface.RecorderListener;
import com.example.screenrecordercore.Model.Place;
import com.example.screenrecordercore.Model.Size;
import com.taobao.android.dexposed.DexposedBridge;

/**
 * Created by ozercanh on 26/08/2015.
 */
public class ScreenRecord {

    private static final int WITH = 320;
    private static final int HEIGHT = 640;
    private static final int BITRATE = 40000;
    private static final int FPS = 5;


    private final Context context;
    private int fps;
    private Place where;
    private Size size;
    private boolean xposed;
    private Recorder recorder;

    public ScreenRecord(Application app){
        Utility.setApp(app);
        this.context = app.getApplicationContext();
        init(app);

    }

    public static ScreenRecord with(Application app){
        ScreenRecord screenRecord = new ScreenRecord(app);
        Recorder recorder = new  Recorder(screenRecord.context);
        recorder.setFps(5);
        Utility.setRecorder(recorder);
        return screenRecord;
    }

    private RecorderListener listener = new RecorderListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onFail(String reason) {

        }

        @Override
        public void onFinish(String path) {

        }
    };

    private void init(Application app){
        recorder = new Recorder(app.getApplicationContext());
        recorder.setFps(FPS);

        Point point = new Point();
        point.x = WITH;
        point.y = HEIGHT;
        size(Size.SMALL);
        recorder.setRecorderListener(listener);
        recorder.setVideoResolution(point);
        recorder.setBitrate(BITRATE);
        Utility.setRecorder(recorder);
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public ScreenRecord maxFPS(int fps){
        this.fps = fps;
        Utility.getRecorder().setFps(fps);
        return this;
    }

    public ScreenRecord place(Place where){
        this.where = where;
        return this;
    }

    public ScreenRecord size(Size size ){
        this.size = size;
        return this;
    }

    public ScreenRecord xposedInjection(boolean xposed){
        boolean isSupport = DexposedBridge.canDexposed(context);
        boolean isLDevice = Build.VERSION.SDK_INT >= 21;
        if(isSupport && !isLDevice) {
            this.xposed = xposed;
        }
        else{
            this.xposed = false;
            Log.e("ScreenRecord", "This device does not support code injection into activities.");
        }
        return this;
    }

    public Recorder start(){
        if(xposed) {
            Utility.hookActivityMethods(context);
        }
        Intent intent = new Intent(context, RecordService.class);
        intent.setAction("start");
        context.startService(intent);
        Recorder recorder = Utility.getRecorder();
        return recorder;
    }

    public void stop(){
        Recorder recorder = Utility.getRecorder();
        if(recorder != null){
            recorder.stopRecording();
        }
    }
}
