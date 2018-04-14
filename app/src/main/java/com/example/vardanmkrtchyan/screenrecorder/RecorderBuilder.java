package com.example.vardanmkrtchyan.screenrecorder;

import android.content.Context;

import com.example.screenrecordercore.Interface.RecorderListener;
import com.example.screenrecordercore.Recorder;

/**
 * Created by Vardan Mkrtchyan on 4/13/2018.
 */

public class RecorderBuilder {
    private Context mContext;
    private int fps;
    private RecorderListener listener;

    public RecorderBuilder(Context mContext) {
        this.mContext = mContext;
    }

    public RecorderBuilder setFps(int fps){
        this.fps = fps;
        return this;
    }

    public RecorderBuilder setListener(RecorderListener listener){
        this.listener = listener;
        return this;
    }

    public Recorder build(){
        Recorder recorder = new Recorder(mContext);
        recorder.setRecorderListener(listener);
        recorder.setFps(fps);
        return recorder;
    }

}
