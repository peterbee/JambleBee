package multipleactivities.jamblebee.com.multipleactivies;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

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

    //Recording
    private Camera mCamera;
    private TextureView mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private static final String TAG = "Recorder";
    private Button captureButton;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaController = new MediaController(this);

        upload = (Button) findViewById(R.id.upload);
        //editor = (Button) findViewById(R.id.editor);
       // browser = (Button) findViewById(R.id.browser);
        //Record
        mPreview = (TextureView) findViewById(R.id.surface_view);
        captureButton = (Button) findViewById(R.id.button_capture);
        stop = (Button) findViewById(R.id.stop_recording);
        stop.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.menu_action_editor:
                startActivity(new Intent(MainActivity.this, VideoProjectEditor.class));
                // switch activity
                break;
             case R.id.menu_action_browser:
                startActivity(new Intent(MainActivity.this, VideoProjectBrowser.class));
                // switch activity
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCaptureClick(View view) {
        if (isRecording) {
            // BEGIN_INCLUDE(stop_release_media_recorder)
            switch(view.getId()) {
                case R.id.stop_recording:
                    // stop recording and release camera
                    mMediaRecorder.stop();  // stop the recording
                    releaseMediaRecorder(); // release the MediaRecorder object
                    mCamera.lock();         // take camera access back from MediaRecorder

                    // inform the user that recording has stopped
                    //setCaptureButtonText((String) getResources().getText(R.string.btnDef));
                    isRecording = false;
                    releaseCamera();
                    captureButton.setEnabled(true);
                    stop.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
        else {
            // BEGIN_INCLUDE(prepare_start_media_recorder)
            if (vidLoc != null){
                //setCaptureButtonText((String) getResources().getText(R.string.btnCapture));
                switch(view.getId()) {
                    case R.id.button_capture:
                        captureButton.setEnabled(false);
                        stop.setEnabled(true);
                        break;
                    default:
                        break;
                }
                new MediaPrepareTask().execute(null, null, null);
            }
            else {
                // show alert that video selected is null .
                switch (view.getId()) {
                    case R.id.upload:
                        loadVideo();
                        break;
                    case R.id.button_capture:
                        break;
                    default:
                        break;
                }
            }
            // END_INCLUDE(prepare_start_media_recorder)
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

    @Override
    protected void onPause() {
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

    private void setCaptureButtonText(String title) {
        captureButton.setText(title);
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
        File temp = CameraHelper.getOutputMediaFile(filePrefix);
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
                video.start();
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
}
