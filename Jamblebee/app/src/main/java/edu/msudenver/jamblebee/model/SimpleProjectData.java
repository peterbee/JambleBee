package edu.msudenver.jamblebee.model;

import java.util.Iterator;

/**
 * Created by guy on 3/31/15.
 */
public class SimpleProjectData implements ProjectData {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public void setOwner(String owner) {

    }

    @Override
    public String getCreatedAt() {
        return null;
    }

    @Override
    public void setCreatedAt(String createdAt) {

    }

    @Override
    public String getOrigId() {
        return null;
    }

    @Override
    public void setOrigId(String origId) {

    }

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public void setParentId(String parentId) {

    }

    @Override
    public long getVideoStartTime(String videoId) {
        return 0;
    }

    @Override
    public long getVideoEndTime(String videoId) {
        return 0;
    }

    @Override
    public void setVideoStartTime(String videoId, long startTime) {

    }

    @Override
    public void setVideoEndTime(String videoId, long endTime) {

    }

    @Override
    public void addVideo(String videoId, long startTime, long endTime) {

    }

    @Override
    public void addVideo(String videoId) {

    }

    @Override
    public String asJsonString() {
        return null;
    }

    @Override
    public Iterator<?> keys() {
        return null;
    }

}

