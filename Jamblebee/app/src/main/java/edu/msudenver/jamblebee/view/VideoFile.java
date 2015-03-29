package edu.msudenver.jamblebee.view;

/**
 * Created by sal on 3/28/15.
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