package edu.msudenver.jamblebee.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.msudenver.jamblebee.controller.CameraHelper;
import edu.msudenver.jamblebee.model.VideoThumbnail;
import edu.msudevner.jamblebee.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CONTEXT_KEY = "context";
    private static final String ARG_PARAM2 = "param2";



    //Class Variables
    View inflatedView;
    Button upload;
    Button editor;
    Button browser;
    String VIDEOS_LOCATION = "sdcard/DCIM/Camera/";
    String vidLoc = null;
    MediaController mediaController;
    VideoView video; //record video
    VideoView playBackVideo;
    ListView listView = null;
    AlertDialog alertDialog;

    //Recording
    private Camera mCamera;
    private TextureView mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private static final String TAG = "Recorder";
    private ImageButton captureButton,play;
    private ImageButton stop,pausePlay;
    GridView gridView;                  // For displaying each track in project and record user interaction
    ArrayList<VideoThumbnail> files;
    String UUID;

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
     * @return A new instance of fragment RecordFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putString(CONTEXT_KEY, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(CONTEXT_KEY);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_record_fragement, container, false);
        mediaController = new MediaController(getActivity());

        upload = (Button) inflatedView.findViewById(R.id.upload);
        mPreview = (TextureView) inflatedView.findViewById(R.id.surface_view);
        captureButton = (ImageButton) inflatedView.findViewById(R.id.button_capture);
        stop = (ImageButton) inflatedView.findViewById(R.id.stop_recording);
        gridView = (GridView) inflatedView.findViewById(R.id.gridView);
        play = (ImageButton) inflatedView.findViewById(R.id.play_button);
        playBackVideo = (VideoView) inflatedView.findViewById(R.id.video);


        //Hide + disable stop and pause button when start
        stop.setVisibility(View.INVISIBLE);
        stop.setEnabled(false);
        pausePlay = (ImageButton) inflatedView.findViewById(R.id.pause_button);
        pausePlay.setVisibility(View.INVISIBLE);
        pausePlay.setEnabled(false);
        return inflatedView;
    }

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

    public void onCaptureClick(View v){
        captureButton.setEnabled(false);
        captureButton.setVisibility(View.INVISIBLE);
        stop.setEnabled(true);
        stop.setVisibility(View.VISIBLE);
        new MediaPrepareTask().execute(null, null, null);
    }

    public void onPauseRecordClick(View v){
        mMediaRecorder.stop();  // stop the recording
        releaseMediaRecorder(); // release the MediaRecorder object
        mCamera.lock();         // take camera access back from MediaRecorder

        // inform the user that recording has stopped
        //setCaptureButtonText((String) getResources().getText(R.string.btnDef));
        isRecording = false;
        releaseCamera();
        captureButton.setEnabled(true);
        captureButton.setVisibility(View.VISIBLE);
        stop.setEnabled(false);
        stop.setVisibility(View.INVISIBLE);
    }

    public void onPlayClick(View v){
        playBackVideo.start();
        pausePlay.setVisibility(View.VISIBLE);
        pausePlay.setEnabled(true);
        play.setVisibility(View.INVISIBLE);
        play.setEnabled(false);
    }

    public void onPausePlayClick(View v){
        playBackVideo.pause();
        play.setVisibility(View.VISIBLE);
        play.setEnabled(true);
        pausePlay.setVisibility(View.INVISIBLE);
        pausePlay.setEnabled(false);
    }

    public void loadVideo(View v){
        // Gets the UUID (Unique Device ID) for storing video files.
        UUID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        files = getProjectContents(VIDEOS_LOCATION);

        ///gridview work

        ArrayAdapter<VideoThumbnail> adapter = new MyListAdapter();//(this, android.R.layout.select_dialog_item, files);
        gridView.setAdapter(adapter);

        //set default to the first video
        vidLoc = files.get(0).getFile().getAbsolutePath();

        mediaController.setAnchorView(playBackVideo);
        Uri uri = Uri.parse(vidLoc);
        playBackVideo.setVideoURI(uri);
        playBackVideo.setMediaController(mediaController);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vidLoc = files.get(position).getFile().getAbsolutePath();
                //System.out.println("VIDLOC : "+vidLoc);

                Uri uri = Uri.parse(vidLoc);
                playBackVideo.setVideoURI(uri);
                playBackVideo.setMediaController(mediaController);
                //alertDialog.dismiss();
            }
        });
    }

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

    @Override
    public void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean prepareVideoRecorder(){

        // BEGIN_INCLUDE (configure_preview)
        mCamera = CameraHelper.getDefaultCameraInstance();

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes,
                mPreview.getWidth(),
                mPreview.getHeight());

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        try {
            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
            // with {@link SurfaceView}
            mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
        } catch (IOException e) {
            //Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
            return false;
        }
        // END_INCLUDE (configure_preview)


        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);

        // Step 4: Set output file
        File temp = CameraHelper.getOutputMediaFile(VIDEOS_LOCATION);
        if (temp == null) {
            return false;
        }
        mMediaRecorder.setOutputFile(temp.getAbsolutePath());

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            //Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            //Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();
                // video.start();
                video.requestFocus();
                vidLoc = null;
                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }
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
