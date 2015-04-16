package edu.msudenver.jamblebee.model;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ster on 4/16/15.
 */
public class ProjectContents {


    // Method which adds all the .mp4 files to our files ArrayList from the project location
    public ArrayList<VideoThumbnail> getProjectContents(String location) {
        File dir = new File(location);
        File[] directoryListing = dir.listFiles();
        ArrayList<VideoThumbnail> files = new ArrayList<VideoThumbnail>();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().endsWith(".mp4")) {
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(child.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
                    VideoThumbnail v = new VideoThumbnail(child, thumb);
                    files.add(v);
                }
            }
        }
        return files;
    }
}
