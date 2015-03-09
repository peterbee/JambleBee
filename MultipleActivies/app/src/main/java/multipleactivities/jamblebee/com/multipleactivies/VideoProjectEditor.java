package multipleactivities.jamblebee.com.multipleactivies;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.graphics.ComposePathEffect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;
import android.provider.Settings.Secure;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VideoProjectEditor extends Activity {

    //Variabels
    VideoView videoView;
    Button button;
    Button button2;
    GridView gridView;
    String vidsLoc = null;
    ArrayList<File> files;
    ArrayList<Integer> times;
    String UUID;
    int time;
    boolean recording;
    MediaPlayer mediaPlayer;
    MediaController mc;
    Button goToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_project_editor);

//        goToMain = (Button) findViewById(R.id.go_to_main);

        gridView = (GridView) findViewById(R.id.gridView);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        videoView = (VideoView) findViewById(R.id.videoView);
        time = 0;
        recording = false;

        mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);

        //vidsLoc = "/sdcard/storage/";
        vidsLoc = "/sdcard/DCIM/camera/";
        files = new ArrayList<>();
        times = new ArrayList<Integer>();

        UUID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        videoView.setOnPreparedListener(PreparedListener);

        //   videoView.setOnCompletionListener(CompletionListener);
//method this >>
        File dir = new File(vidsLoc);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().substring(child.getName().length()-4, child.getName().length()).equals(".mp4")) {
                    files.add(child);
                }
            }
        }
//to here
        String vidLoc = files.get(0).getAbsolutePath();
        Uri uri = Uri.parse(vidLoc);
        videoView.setVideoURI(uri);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, files);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String vidLoc = files.get(position).getAbsolutePath();
                Uri uri = Uri.parse(vidLoc);
                videoView.setVideoURI(uri);
                //      videoView.start();
                if (recording) {
                    time = mediaPlayer.getCurrentPosition();
                    //    if (videoView.canSeekForward()) {
                    videoView.seekTo(time);
                    //    }
                    times.add(time);
                }
                videoView.start();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = true;
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

                videoView.start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            recording = false;
            mediaPlayer.release();
            String s = Integer.toString(times.size());
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    };
    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener(){
        @Override
        public void onPrepared(MediaPlayer m) {
            m.setVolume(0f, 0f);
            m.setLooping(false);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_project_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.menu_action_main:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCaptureClick(View view) {

    }
}
