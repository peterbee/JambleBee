package edu.msudenver.jamblebee.model;

import java.util.List;

/**
 * Created by guy on 3/30/15.
 */
public class ProjectDataFactory {

    public ProjectData createNew(List<VideoData> videos) {
        ProjectData project = new ProjectData();
        for (VideoData video : videos) {
            project.addVideo(video.getId(), video.getStartTime(), video.getEndTime());
        }
        return project;
    }
}
