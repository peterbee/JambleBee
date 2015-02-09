package com.example.contrivance.jamstagram;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends Activity {

  //   String vidLoc = "http://www.jsharkey.org/downloads/dailytest.3gp";
    String vidLoc = null;
    String filePrefix = "sdcard/DCIM/Camera/";
    // you will need to change this to a video file on your device

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (vidLoc != null) {
//            VideoView video = (VideoView) findViewById(R.id.video);
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(video);
//
//            Uri uri = Uri.parse(vidLoc);
//            video.setVideoURI(uri);
//            video.setMediaController(mediaController);
//
//            video.start();
//            video.requestFocus();
//            vidLoc = null;
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
        case R.id.actionLoadVideo:
            loadVideo();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadVideo() {
        final CharSequence[] videosArray = (CharSequence[]) new File(filePrefix).list();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder1 = builder
                .setTitle("Pick a video")
                .setItems(videosArray, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        vidLoc = filePrefix + (String) videosArray[id];
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        if (vidLoc != null) {
            VideoView video = (VideoView) findViewById(R.id.video);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);

            Uri uri = Uri.parse(vidLoc);
            video.setVideoURI(uri);
            video.setMediaController(mediaController);

            video.start();
            video.requestFocus();
            vidLoc = null;
        }

    }

}
