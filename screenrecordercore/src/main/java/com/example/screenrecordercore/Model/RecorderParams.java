package com.example.screenrecordercore.Model;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ozercanh on 18/08/2015.
 */
public class RecorderParams {
    public String videoPath;
    public int fps;
    public int screenWidth;
    public int screenHeight;
    public int bitrate;
    public ConcurrentLinkedQueue<Screenshot> queue;
}
