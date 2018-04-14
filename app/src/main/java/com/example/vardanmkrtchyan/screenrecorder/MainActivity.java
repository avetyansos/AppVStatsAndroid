package com.example.vardanmkrtchyan.screenrecorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.screenrecordercore.Interface.RecorderListener;
import com.example.screenrecordercore.Model.Place;
import com.example.screenrecordercore.Model.Size;
import com.example.screenrecordercore.Recorder;
import com.example.screenrecordercore.ScreenRecord;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Recorder myRecorder;
    private RecorderBuilder myRecorderBuilder;
    private ScreenRecord screenRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textview);


        screenRecord = ScreenRecord.with(getApplication());
        screenRecord.maxFPS(10);
        screenRecord.place(Place.TOP_LEFT);
        screenRecord.size(Size.SMALL);
        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  myRecorder.startRecording();
                myRecorder = screenRecord.start();
                myRecorder.setRecorderListener(listener);
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.hyperspace_jump);
                findViewById(R.id.cancel_button).startAnimation(hyperspaceJumpAnimation);
            }
        });

        findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myRecorder != null)
                    myRecorder.stopRecording();
            }
        });

        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myRecorder != null)
                myRecorder.cancelRecording();
            }
        });

        findViewById(R.id.new_window_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private RecorderListener listener = new RecorderListener() {
        @Override
        public void onStart() {
            textView.setText("started");
        }

        @Override
        public void onFail(String reason) {
            textView.setText("failed " + reason);
        }

        @Override
        public void onFinish(final String path) {
            if(path == null){
                textView.setText("failed");
            }
            else {
                textView.setText("finished " + path);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                        intent.setDataAndType(Uri.parse(path), "video/mp4");
                        startActivity(intent);
                    }
                });
            }
        }
    };
}
