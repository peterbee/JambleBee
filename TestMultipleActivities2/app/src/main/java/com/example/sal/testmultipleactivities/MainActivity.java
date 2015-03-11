package com.example.sal.testmultipleactivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class MainActivity extends Activity {

    //Class Variables
    Button upload;
    Button editor;
    Button browser;
    String filePrefix = "sdcard/DCIM/Camera/";
    String vidLoc = null;
    MediaController mediaController;
    VideoView video;
    ListView listView = null;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaController = new MediaController(this);

        upload = (Button) findViewById(R.id.upload);
        editor = (Button) findViewById(R.id.editor);
        browser = (Button) findViewById(R.id.browser);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onCaptureClick(View view) {
        switch (view.getId()){
            case R.id.upload:
               loadVideo();
                break;
            case R.id.browser:
                startActivity(new Intent(MainActivity.this, VideoProjectBrowser.class));
                //Switch to Project Browser Activity
                break;
            case R.id.editor:
                startActivity(new Intent(MainActivity.this, VideoProjectEditor.class));
                //Switch to Project Editor Activity
                break;
            default:
                break;

        }
    }
    //Load a video from an Android local storage into Playback Window
    public void loadVideo() {
        File mediaDir = new File(filePrefix);
        final String[] videosArray = mediaDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".mp4");
            }
        });
        System.out.println(videosArray.length);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = (View) getLayoutInflater().inflate(R.layout.list_view, null);
        listView =(ListView) view.findViewById(R.id.list_view_1);
        listView.setAdapter((new VideoArrayAdapter(this, videosArray)));
        builder.setTitle("Pick a video").setView(view).setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vidLoc = filePrefix + videosArray[(int)id];
                video = (VideoView) findViewById(R.id.video);
                mediaController.setAnchorView(video);

                Uri uri = Uri.parse(vidLoc);
                video.setVideoURI(uri);
                video.setMediaController(mediaController);

                alertDialog.dismiss();
            }
        });

    }
}
