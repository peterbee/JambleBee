package com.example.contrivance.editview;

import java.io.File;

/**
 * Created by ster on 3/12/15.
 *
 * This class stores the time and path of the video selected
 * Makes it convenient to store this data in an ArrayList
 */
public class VideoFile {
    int startTime;
    int endTime;      // Time (in miliseconds) of every user action while in record mode
    String path;   // Path to the selected video

    public VideoFile (int start, String p) {
        startTime = start;
        path = p;
    }

    public void setEndTime(int time) {
        endTime = time;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getPath() {
        return path;
    }
}
