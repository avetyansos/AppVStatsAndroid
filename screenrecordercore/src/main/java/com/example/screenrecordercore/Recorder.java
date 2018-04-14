package com.example.screenrecordercore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.screenrecordercore.Interface.RecorderListener;
import com.example.screenrecordercore.Model.RecorderParams;
import com.example.screenrecordercore.Model.Screenshot;
import com.example.screenrecordercore.Model.Status;
import com.halilibo.screenrecorddebug.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Vardan Mkrtchyan on 4/13/2018.
 */

public class Recorder {

    private int fps;
    private int bitrate;
    private int count;

    private Activity activity;
    private Status statusFlag;
    private long initiateTime;
    private Point size;
    private RecorderListener mRecorderListener;

    private VideoRecorderThread videoRecorderThread;
    private RecorderParams recorderParams;
    private Paint paint;

    private Context mContext;

    public Recorder(Context context){
        Utility.init(context);
        this.mContext = context;
    }

    public void setVideoResolution(Point size){
        this.size = size;
    }


    public void onResume(Activity activity){
        this.activity = activity;
        Display display = activity.getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        Log.d("injection", "Recorder will work with " + activity.getComponentName() + " " + size.x + " " + size.y);

        Intent intent = new Intent(Utility.getContext(), RecordService.class);
        intent.setAction("on_activity_change");
        Utility.getContext().startService(intent);
    }

    /**
     * Makes the current activity null.
     */
    public void onPause(){
        activity = null;

        Intent intent = new Intent(Utility.getContext(), RecordService.class);
        intent.setAction("on_activity_change");
        Utility.getContext().startService(intent);
    }

    /**
     * Sets the maximum frame per seconds for the video.
     * @param fps Desired maximum fps. Actual FPS can be lower due to memory problems.
     */
    public void setFps(int fps) {
        this.fps = fps;
    }


    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * Sets a recorder listener for callbacks.
     * @param _listener A {@link RecorderListener} instance to use for callbacks.
     */
    public void setRecorderListener(RecorderListener _listener){
        this.mRecorderListener = _listener;
    }

    /**
     * Initializes variables before the recording starts.
     */
    private void initializeVariables() {
        String recordName = Utility.getNextRecordName();
        Handler handler = new Handler();
       // File videoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), recordName);
        File videoFile = new File(mContext.getFilesDir().getAbsolutePath()+"/"+recordName);
        this.count = 0;

//        Display display = activity.getWindowManager().getDefaultDisplay();
//        size = new Point();
//        display.getSize(size);

        paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.recording_primary));
        paint.setStrokeWidth(10);

        recorderParams = new RecorderParams();
        recorderParams.videoPath = videoFile.getPath();
        recorderParams.fps = fps;
        recorderParams.screenWidth = size.x;
        recorderParams.screenHeight = size.y;
        recorderParams.bitrate = bitrate;
        recorderParams.queue = new ConcurrentLinkedQueue<>();

        videoRecorderThread = new VideoRecorderThread(recorderParams, mRecorderListener, handler);

    }

    /**
     * Starts the recording instantly.
     * This method should be called from outside.
     */
    public void startRecording(){
        initializeVariables();
        // Hold the starting time. If recording time goes over a constant limit, it'll be cancelled.
        initiateTime = System.currentTimeMillis();
        videoRecorderThread.start();
        statusFlag = Status.RECORDING;
        saveFrame();
    }

    /**
     * Starts the stopping process.
     * Recording does not finish immediately. This will only stop taking new screenshots.
     * Queue should be cleaned first.
     */
    public void stopRecording(){
        this.statusFlag = Status.STOPPED;
        videoRecorderThread.stopRecording(true);
    }

    /**
     * Stops the recording instantly.
     * Calling this method cancels all recording tasks and removes the video from files.
     */
    public void cancelRecording(){
        statusFlag = Status.CANCELLED;
        videoRecorderThread.stopRecording(false);
    }

    /**
     * Initiates the screenshot taking process.
     * This method calls itself repeatedly. Delays are calculated according to maximum FPS
     */
    private void saveFrame() {
        // Stop taking new screenshots
        if(statusFlag != Status.RECORDING){
            Log.d("recording", "takescreenshot status is not recording");
            return;
        }
        // This object will be sent to video recording thread.
        Screenshot ss = new Screenshot();
        ss.time = System.currentTimeMillis();
        ss.frameNumber = count;

        // Hold the starting time to calculate how long does it take to draw a screenshot.
        long startTime = System.currentTimeMillis();

        Bitmap drawing = null;
        try {
            // Draw the view over a canvas whose bitmap reference is known to us.
            View rootView = activity.findViewById(android.R.id.content).getRootView();
            drawing = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(drawing);
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            Log.d("ScreenRecorder","Touch Action Down " + " X " + event.getX() + " Y " + event.getY());
                            canvas.drawCircle(event.getX(),event.getY(),40,paint);
                            break;
                    }
                    return true;
                }
            });
            rootView.draw(canvas);

        }
        catch(Error | Exception ignored){
            //TODO : Make a log!
        }
        ss.bitmap = drawing;
        recorderParams.queue.add(ss);
        count++;

        long finishTime = System.currentTimeMillis();
        // Over time should stop recording.
        if(finishTime - initiateTime >= 100000){
            stopRecording();
        }
        else{
            int passedTime = (int) (finishTime - startTime);
            int delay = 1000/fps - passedTime;
            // If we are not able to achieve the maximum fps, at least continue with what we have!
            if(delay <= 0){
                delay = 0;
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                synchronized public void run() {
                    saveFrame();
                }

            }, delay);

        }
    }

    public void setActivity(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        this.activity = activity;
    }

    public Activity getActivity(){
        return activity;
    }

    public boolean isRecording() {
        if(statusFlag == Status.RECORDING)
            return true;
        else
            return false;
    }
}
