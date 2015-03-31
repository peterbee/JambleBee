package edu.msudenver.jamblebee.model;

public class VideoData {

    String videoId;
    int startTime;
    int endTime;      // Time (in miliseconds) of every user action while in record mode
    String path;   // Path to the selected video

    public VideoData (String id, int start, String p) {
        videoId = id;
        startTime = start;
        path = p;
    }
    public VideoData (int start, String p) {
        startTime = start;
        path = p;
    }

    public VideoData(String json) {
        //TODO need to parse JSON
    }

	public String getId() {
		return videoId;
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

	public String asJsonString() {
        //TODO  this is wrong. Need another class to translate to JSON
		return toString();
	}

}
