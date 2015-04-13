package edu.msudenver.jamblebee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.provider.Settings.Secure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.msudenver.jamblebee.model.VideoData;
import edu.msudenver.jamblebee.model.VideoThumbnail;
import edu.msudevner.jamblebee.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DJFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DJFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DJFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String VIDEOS_LOCATION = "sdcard/DCIM/Camera";// or "/sdcard/storage/";

    ArrayList<VideoThumbnail> files;    // For storing all the files in a project
    ArrayList<VideoData> videos;        // For storing user interactions
    Button playButton;                  // Button that plays back recorded interactions
    GridView gridView;                  // For displaying each track in project and record user interaction
    Handler handler;                    // For the mid-video call backs - Separate thread that runs timer
    MediaPlayer mediaPlayer;            // The media player
    MediaController mc;                 // The media Controller
    String UUID;                        // Devices Unique Identifier
    VideoView videoView;                // THE VIDEO VIEW! =)
    boolean recording;                  // True if recording, False if not recording
    int playingVideoNumber;             // Variable for moving on to the next video after a switch in play
    private View inflatedView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DJFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DJFragment newInstance(String param1, String param2) {
        DJFragment fragment = new DJFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DJFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_dj, container, false);

        gridView = (GridView) inflatedView.findViewById(R.id.gridView);
        playButton = (Button) inflatedView.findViewById(R.id.playButton);
        videoView = (VideoView) inflatedView.findViewById(R.id.videoView);
        recording = false;

        // Gets the UUID (Unique Device ID) for storing video files.
        UUID = Secure.getString(getActivity().getContentResolver(), Secure.ANDROID_ID);

        // A handler to assist us with our postDelayed Method for when we playback interactions
        handler = new Handler();

        // Sets up the MediaController to the VideoView
        mc = new MediaController(getActivity());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showInstructionToast();
                return false;
            }
        });

        // times is an ArrayList containing the VideoFile objects (user interaction time and path)
        videos = new ArrayList<VideoData>();

        // We need this when we want to mute sound of each video played from gridview
        //     videoView.setOnPreparedListener(PreparedListener);

        // Adds the completion listener to the videoView
        videoView.setOnCompletionListener(CompletionListener);

        // Gets all the .mp4s and adds them to files array list (see method below)
        files = getProjectContents(VIDEOS_LOCATION);

        // Sets up the ArrayAdapter to translate our files ArrayList into a scrollable gridview
        ArrayAdapter<VideoThumbnail> adapter = new MyListAdapter();//(this, android.R.layout.select_dialog_item, files);
        gridView.setAdapter(adapter);


        // This is what happens every time an item is selected in the gridview
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (videoView.isPlaying() && recording) {
                    final int time = videoView.getCurrentPosition();
                    videoView.seekTo(time);
                    VideoData lastVid = videos.get(videos.size()-1);
                    lastVid.setEndTime(time);
                    videos.add(new VideoData(time, files.get(position).getFile().getAbsolutePath()));
                    // Apparently we need to request focus to setUp the listener allowing seekTo()
                    videoView.requestFocus();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            videoView.seekTo(time);
                        }
                    });
                    setUpVideo(position);
                    videoView.start();
                } else {
                    record(position);
                }
            }
        });


/////////////////////////////////////////Help///////////////////////////////////////////////////////

        // This is the Thread that is called every 100 ms to check if the video has switch
        // playback mode only
        // I need help here
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                int currentTime = videoView.getCurrentPosition();
                final VideoData currentVid = videos.get(playingVideoNumber);
                int switchTime = currentVid.getEndTime();
                if (currentTime >= switchTime) {
                    if (playingVideoNumber < videos.size() - 1) {
                        videoView.requestFocus();
                        VideoData nextVid = videos.get(playingVideoNumber + 1);
                        setUpVideo(nextVid.getPath());
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mp){
                                videoView.seekTo(currentVid.getEndTime());
                            }
                        });
                        videoView.start();
                        playingVideoNumber++;
                    }

                }
                if (playingVideoNumber < videos.size() - 1) {
                    handler.postDelayed(this, 100);
                }
            }
        };

        // This is what happens when the play button is clicked - This is where i need the most help
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording && videos.size() > 0) {
                    playingVideoNumber = 0;
                    VideoData firstVid = videos.get(0);
                    setUpVideo(firstVid.getPath());
                    videoView.requestFocus();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp){
                            videoView.seekTo(0);
                        }
                    });
                    handler.postDelayed(r, 100);
                    videoView.start();
                    playSounds();
                } else {
                    showInstructionToast();
                }
            }
        });


        return inflatedView;
    }
    // Method that is used throughout application to load a new video to the videoview
    // viewView.start() must be called after this running this method to actually start it
    private void setUpVideo(int i) {
        videoView.stopPlayback();
        String vidLoc = files.get(i).getFile().getAbsolutePath();
        videoView.setVideoPath(vidLoc);
    }
    private void setUpVideo(String path) {
        videoView.stopPlayback();
        videoView.setVideoPath(path);
    }


    // Method which adds all the .mp4 files to our files ArrayList from the project location
    private ArrayList<VideoThumbnail> getProjectContents(String location) {
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


    // Method which starts the recording process
    public void record(int position) {
        recording = true;
        setUpVideo(position);
        videos.add(new VideoData(0, files.get(position).getFile().getAbsolutePath()));
        videoView.start();
        playSounds();
    }

    // The current way we are playing sounds from video files - This is our lag issue.
    private void playSounds() {
        for (int i = 0; i<files.size(); i++) {
            String vidLoc = files.get(i).getFile().getAbsolutePath();
            Uri uri = Uri.parse(vidLoc);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(CompletionListener);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(getActivity().getApplicationContext(), uri);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void showInstructionToast() {
        Context context = getActivity().getApplicationContext();
        CharSequence text = "Tap a video path to start recording.";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private class MyListAdapter extends ArrayAdapter<VideoThumbnail> {
        public MyListAdapter() {
            super(getActivity(), R.layout.grid_item, files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.grid_item, parent, false);
            }

            VideoThumbnail video = files.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageBitmap(video.getThumb());

            return itemView;
        }
    }

}
