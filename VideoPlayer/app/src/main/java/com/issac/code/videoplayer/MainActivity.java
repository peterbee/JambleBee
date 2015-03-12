package com.issac.code.videoplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;


public class MainActivity extends ActionBarActivity {

    private ArrayList<VideoTile> tiles = new ArrayList<VideoTile>();
    private Button playAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView1),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView2),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView3),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView4),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView5),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView6),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView7),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView8),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView9),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView10),this));
        tiles.add(new VideoTile((VideoView)this.findViewById(R.id.videoView11),this));

        playAll = (Button) this.findViewById(R.id.playAllButton);
        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(VideoTile t : tiles)
                    t.play();
            }
        });

        long max = Runtime.getRuntime().maxMemory();
        long available = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        System.out.println("Max: " + max + " Available: " + available + " free: " +free);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    public void dispatchTakeVideoIntent(VideoTile tile) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,getOutputMediaFileUri(REQUEST_VIDEO_CAPTURE));
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
        requestVid = tile;
    }
    private VideoTile requestVid;
    private Uri viduri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Video saved to:" +
                    data.getData(), Toast.LENGTH_LONG).show();
            viduri = data.getData();
            if(requestVid!=null)
                requestVid.setVidUri(viduri);
        }

    }

    private Uri getOutputMediaFileUri(int type){

        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){
                Toast.makeText(this, "Failed to make dir", Toast.LENGTH_LONG).show();
                return null;
            }
        }
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());
        File mediaFile;
        if(type == REQUEST_VIDEO_CAPTURE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");

        } else {
            return null;
        }
        return mediaFile;
    }
}
