package com.issac.code.videoplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;

/**
 * Created by Issac on 2/20/2015.
 */
public class VideoTile  {

    private Uri videoFile;
    private VideoView view;
    private Activity main;
    private boolean playing = false;

    public VideoTile(VideoView v, Activity main)
    {
        this.main = main;
        view = v;
        setUpVideoListener(view);
    }

    public void setVidUri(Uri v)
    {
        videoFile = v;
    }

    public void play()
    {
        view.start();
        view.seekTo(1);
    }

    void togglePlayback(final VideoView v)
    {
        v.requestFocus();
        playing = ! playing;
        if(playing)
            v.start();
        else
            v.pause();
    }

    void setUpVideoListener(final VideoView v)
    {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(videoFile != null && !v.canPause())
                    v.setVideoURI(videoFile);
                else if(videoFile == null)
                    loadOrRecord();
                if(v.canPause())
                    togglePlayback(v);
                return false;
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                v.pause();
                return false;
            }
        });
        //MediaController mc = new MediaController(main);
       // v.setMediaController(mc);
        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    void loadOrRecord()
    {
        final AlertDialog.Builder build = new AlertDialog.Builder(main);
        build.setTitle("Load Video");
        build.setPositiveButton("Record Video",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity)main).dispatchTakeVideoIntent(VideoTile.this);
                dialog.cancel();
            }
        });
        build.setNeutralButton("Load Video",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                loadVideo();
            }
        });
        build.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        build.show();
    }

    void loadVideo()
    {
        final AlertDialog.Builder build = new AlertDialog.Builder(main);
        build.setTitle("Load Video");
        LayoutInflater inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myScrollView = inflater.inflate(R.layout.scroll, null, false);
        build.setView(myScrollView);
        build.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = build.create();

        FileFilter typeFilter = new FileFilter() {
            public boolean accept(File file) {
                if (file.isFile() && file.getName().endsWith("mp4")) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");
        final File[] file = f.listFiles(typeFilter);


        final LinearLayout vertItems = (LinearLayout)myScrollView.findViewById(R.id.scrollList);

        for (int index = 0; index < file.length; index++) {
            final LinearLayout hor = new LinearLayout(main);
            hor.setOrientation(LinearLayout.HORIZONTAL);

            Button bDelete = new Button(main);
            bDelete.setText("Delete Video");
            final int id = index;
            bDelete.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    file[id].delete();
                    vertItems.removeView(hor);
                }
            });

            ImageView image = new ImageView(main);

            hor.addView(image);
            hor.addView(bDelete);

            vertItems.addView(hor);
            image.getLayoutParams().width = 256;
            image.getLayoutParams().height = 256;
            image.setImageBitmap( ThumbnailUtils.createVideoThumbnail(file[index].getAbsolutePath(),
                    MediaStore.Images.Thumbnails.MINI_KIND));
            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    videoFile = Uri.fromFile(file[id]);
                    alertDialog.cancel();
                    return false;
                }
            });

            LinearLayout.LayoutParams lop = (LinearLayout.LayoutParams) hor.getLayoutParams();
            lop.gravity = Gravity.CENTER_VERTICAL;

        }



        alertDialog.show();
    }
}
