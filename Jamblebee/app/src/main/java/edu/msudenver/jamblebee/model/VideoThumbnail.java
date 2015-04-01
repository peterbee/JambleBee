package edu.msudenver.jamblebee.model;

import android.graphics.Bitmap;
import java.io.File;


/**
 * Created by ster on 3/31/15.
 */
public class VideoThumbnail {

    private File file;
    private Bitmap thumb;


    public VideoThumbnail (File f, Bitmap t) {
        this.file = f;
        this.thumb = t;
    }

    public void setFile(File f) {
        file = f;
    }

    public void setThumb(Bitmap t) {
        thumb = t;
    }

    public File getFile() {
        return file;
    }

    public Bitmap getThumb() {
        return thumb;
    }

}
