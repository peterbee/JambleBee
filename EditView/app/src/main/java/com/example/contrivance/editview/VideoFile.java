package com.example.contrivance.editview;

import java.io.File;

/**
 * Created by ster on 3/12/15.
 *
 * This class stores the time and path of the video selected
 * Makes it convenient to store this data in an ArrayList
 */
public class VideoFile {

    int time;      // Time (in miliseconds) of every user action while in record mode
    String path;   // Path to the selected video

    public VideoFile (int t, String p) {
        time = t;
        path = p;
    }

    public int getTime() {
        return time;
    }

    public String getPath() {
        return path;
    }
}
