package com.example.contrivance.jamstagram;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.FilenameFilter;

public class MainActivity extends Activity {

  //   String vidLoc = "http://www.jsharkey.org/downloads/dailytest.3gp";
    String vidLoc = null;
    String filePrefix = "sdcard/DCIM/Camera/";
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaController = new MediaController(this);
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
        File mediaDir = new File(filePrefix);
        final CharSequence[] videosArray = mediaDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
               return filename.endsWith(".mp4");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a video")
               .setItems(videosArray, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       vidLoc = filePrefix + videosArray[id];
                       VideoView video = (VideoView) findViewById(R.id.video);
                       mediaController.setAnchorView(video);

                       Uri uri = Uri.parse(vidLoc);
                       video.setVideoURI(uri);
                       video.setMediaController(mediaController);

                       video.start();
                       video.requestFocus();
                       vidLoc = null;
                   }
               });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
