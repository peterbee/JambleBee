package edu.msudenver.jamblebee.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.msudenver.jamblebee.view.MainActivity;


/**
 * Created by ster on 4/15/15.
 */
public class AudioData {

    MediaPlayer mediaPlayer;
    Context context;

    public AudioData(Context c) {
        context = c;
    }

    /**
     * This method plays all the sounds from videos in the files ArrayList simultaneously
     *
     * @param files - the ArrayList containing our .mp4 files.
     */
    public void playSounds(ArrayList<VideoThumbnail> files) {
        for (int i = 0; i<files.size(); i++) {
            String vidLoc = files.get(i).getFile().getAbsolutePath();
            Uri uri = Uri.parse(vidLoc);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This listener is meant to mute the sound from the video view when a gridview
     * item is selected for recording user actions and playing back those actions.
     *
     * @param v - the videoView to be muted.
     */
    public void muteVideoView(VideoView v) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        v.setOnPreparedListener(PreparedListener);
    }

}
