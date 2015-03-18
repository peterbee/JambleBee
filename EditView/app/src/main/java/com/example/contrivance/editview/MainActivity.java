package com.example.contrivance.editview;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.VideoView;
import android.provider.Settings.Secure;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<File> files;      // For storing all the files in a project
    ArrayList<VideoFile> times; // For storing user interactions
    Button recordButton;        // Button that starts recording of user interactions
    Button playButton;          // Button that plays back recorded interactions
    GridView gridView;          // For displaying each track in project and record user interaction
    Handler handler;            // For the mid-video call backs - Separate thread that runs timer
    MediaPlayer mediaPlayer;    // The media player
    MediaController mc;         // The media Controller
    String vidsLoc;             // Location of project on device
    String UUID;                // Devices Unique Identifier
    VideoView videoView;        // THE VIDEO VIEW! =)
    boolean recording;          // True if recording, False if not recording
    int playingVideoNumber;      // Variable for moving on to the next video after a switch in play

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        recordButton = (Button) findViewById(R.id.recordButton);
        playButton = (Button) findViewById(R.id.playButton);
        videoView = (VideoView) findViewById(R.id.videoView);
        recording = false;

        // Gets the UUID (Unique Device ID) for storing video files.
        UUID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

        // A handler to assist us with our postDelayed Method for when we playback interactions
        handler = new Handler();

        // Sets up the MediaController to the VideoView
        mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);

        // Setting current project location
        //vidsLoc = "/sdcard/storage/";

        // This location may be better for you...
        vidsLoc = "/sdcard/DCIM/camera/";

        // files is an ArrayList containing File objects
        files = new ArrayList<File>();

        // times is an ArrayList containing the VideoFile objects (user interaction time and path)
        times = new ArrayList<VideoFile>();

        // We need this when we want to mute sound of each video played from gridview
        //     videoView.setOnPreparedListener(PreparedListener);

        // Adds the completion listener to the videoView
        videoView.setOnCompletionListener(CompletionListener);

        // Gets all the .mp4s and adds them to files array list (see method below)
        getProjectContents();

        // Sets a video up to be played ( 0 is the first file in the files arraylist ) (see below)
        setUpVideo(0);

        // Sets up the ArrayAdapter to translate our files ArrayList into a scrollable gridview
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, files);
        gridView.setAdapter(adapter);


        // This is what happens every time an item is selected in the gridview
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (videoView.isPlaying() && recording) {
                    final int time = videoView.getCurrentPosition();
                    videoView.seekTo(time);
                    times.add(new VideoFile(time, files.get(position).getAbsolutePath()));
                    // Apparently we need to request focus to setUp the listener allowing seekTo()
                    videoView.requestFocus();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            videoView.seekTo(time);
                        }
                    });
                    setUpVideo(position);
                    videoView.start();
                }
            }
        });


        // The record method is called when the recordButton is pressed
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });


/////////////////////////////////////////Help///////////////////////////////////////////////////////

        // This is the Thread that is called every 100 ms to check if the video has switch
        // playback mode only
        // I need help here
        final Runnable r = new Runnable() {
            public void run() {
                int currentTime = videoView.getCurrentPosition();
                int switchTime = times.get(playingVideoNumber).getTime();
                Log.i("", "current time = "+currentTime);
                Log.i("", "switch time = "+switchTime);
                if (currentTime >= switchTime) {
                    if (playingVideoNumber < times.size()) {
                        videoView.requestFocus();
                        videoView.seekTo(times.get(playingVideoNumber).getTime());
                        setUpVideo(times.get(playingVideoNumber).getPath());
                        videoView.start();
                        playingVideoNumber++;
                    }

                }
                if (playingVideoNumber < times.size()) {
                    handler.postDelayed(this, 100);
                }
            }
        };

        // This is what happens when the play button is clicked - This is where i need the most help
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording && times.size() > 0) {
                    playingVideoNumber = 0;
                    setUpVideo(0);
                    videoView.requestFocus();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp){
                            videoView.seekTo(0);
                        }
                    });
                    handler.postDelayed(r, 100);
                    videoView.start();
      //              playSounds();
                }
            }
        });

    }

////////////////////////////////////////END HELP////////////////////////////////////////////////////

    // Method that is used throughout application to load a new video to the videoview
    // viewView.start() must be called after this running this method to actually start it
    private void setUpVideo(int i) {
        videoView.stopPlayback();
        String vidLoc = files.get(i).getAbsolutePath();
        videoView.setVideoPath(vidLoc);
    }
    private void setUpVideo(String path) {
        videoView.stopPlayback();
        videoView.setVideoPath(path);
    }


    // Method which adds all the .mp4 files to our files ArrayList from the project location
    private void getProjectContents() {
        File dir = new File(vidsLoc);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().endsWith(".mp4")) {
                    files.add(child);
                }
            }
        }
    }


    // Method which starts the recording process
    public void record() {
       recording = true;
       videoView.start();
    //   playSounds();
    }

    // The current way we are playing sounds from video files - This is our lag issue.
    private void playSounds() {
        for (int i = 0; i<files.size(); i++) {
            String vidLoc = files.get(i).getAbsolutePath();
            Uri uri = Uri.parse(vidLoc);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(CompletionListener);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    // This listener lets us know when we are finished recording (when the videoView Stops)
    MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            recording = false;
        }
    };




    // This Commented out listener is meant to mute the sound from the video view when a gridview
    // item is selected for recording user actions.
/*
    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(MediaPlayer m) {
            try {
                if (m.isPlaying()) {
                    m.stop();
                   m.release();
                    m = new MediaPlayer();
                }
                m.setVolume(0f, 0f);
                m.setLooping(false);
            //    m.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

*/


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
