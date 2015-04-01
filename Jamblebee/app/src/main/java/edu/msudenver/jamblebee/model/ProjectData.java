package edu.msudenver.jamblebee.model;

/**
 * Created by guy on 3/31/15.
 */
public interface ProjectData {
    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getOwner();

    void setOwner(String owner);

    String getCreatedAt();

    void setCreatedAt(String createdAt);

    String getOrigId();

    void setOrigId(String origId);

    String getParentId();

    void setParentId(String parentId);

    long getVideoStartTime(String videoId);

    long getVideoEndTime(String videoId);

    void setVideoStartTime(String videoId, long startTime);

    void setVideoEndTime(String videoId, long endTime);

    void addVideo(String videoId, long startTime, long endTime);

    void addVideo(String videoId);

    String asJsonString();
}
