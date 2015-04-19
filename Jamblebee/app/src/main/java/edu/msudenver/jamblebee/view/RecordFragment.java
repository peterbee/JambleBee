package edu.msudenver.jamblebee.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.msudenver.jamblebee.controller.CameraHelper;
import edu.msudenver.jamblebee.model.AudioData;
import edu.msudenver.jamblebee.model.JSONdata;
import edu.msudenver.jamblebee.model.ProjectContents;
import edu.msudenver.jamblebee.model.VideoData;
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
    String VIDEOS_LOCATION = "sdcard/DCIM/Camera/";
    String vidLoc = null;
    MediaController mediaController;
    VideoView playBackVideo;

    //Recording
    private Camera mCamera;
    private TextureView mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private ImageButton captureButton;
    private ImageButton stop,save;
    GridView gridView;                  // For displaying each track in project and record user interaction
    ArrayList<VideoThumbnail> files;
    ArrayList<String> filesLocation = new ArrayList<String>();
    JSONdata metaData;
    JSONObject currentProject;
    File currentFile;
    static int count = 0;
    AudioData ad;
    ProjectContents pc;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RecordFragment() {

    }

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
        inflatedView = inflater.inflate(R.layout.fragment_record, container, false);
        mediaController = new MediaController(getActivity());
        metaData = new JSONdata();
        currentProject = new JSONObject();
        ad = new AudioData(getActivity());
        pc = new ProjectContents();


        try {
            currentProject.put("locations",new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mPreview = (TextureView) inflatedView.findViewById(R.id.surface_view);
        captureButton = (ImageButton) inflatedView.findViewById(R.id.button_capture);
        stop = (ImageButton) inflatedView.findViewById(R.id.stop_recording);
        gridView = (GridView) inflatedView.findViewById(R.id.gridView);
        playBackVideo = (VideoView) inflatedView.findViewById(R.id.video);
        save = (ImageButton) inflatedView.findViewById(R.id.save_video);

        //Hide + disable stop and pause button when start
        stop.setVisibility(View.INVISIBLE);
        stop.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
        save.setEnabled(false);

        //Mute the VideoView
        ad.muteVideoView(playBackVideo);

        return inflatedView;
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
        save.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
        stop.setEnabled(true);
        stop.setVisibility(View.VISIBLE);
        new MediaPrepareTask().execute(null, null, null);
        if (files != null)
            if (!files.isEmpty())
                ad.playSounds(files);
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
        save.setEnabled(true);
        save.setVisibility(View.VISIBLE);
        stop.setEnabled(false);
        stop.setVisibility(View.INVISIBLE);
    }


    public void onSaveVideoClick(View v){
   //         mMediaRecorder.stop();  // stop the recording
        //mCamera.lock();         // take camera access back from MediaRecorder
        //releaseCamera();

        final EditText userInput = new EditText(getActivity());
        userInput.setMaxLines(1);
        filesLocation.add(currentFile.getAbsolutePath());
        System.out.println("files locations now  : " + filesLocation.get(0));
        // mMediaRecorder.stop();
        releaseMediaRecorder(); // release the MediaRecorder object
        releaseCamera();
        // mCamera.lock();
        loadVideoList(filesLocation);



    }
    public void loadVideo(View v){
        loadVideoList(filesLocation);
    }

    public void loadVideoList(ArrayList<String> locations){
        // Gets the UUID (Unique Device ID) for storing video files.
        files = pc.getProjectContentsFromLoc(locations);

        ///gridview work
        gridView = (GridView) inflatedView.findViewById(R.id.gridView);
        ArrayAdapter<VideoThumbnail> adapter = new MyListAdapter();
        gridView.setAdapter(adapter);

        //set default to the first video
        if(files.size()!=0) {
            vidLoc = files.get(0).getFile().getAbsolutePath();
            playBackVideo = (VideoView) inflatedView.findViewById(R.id.video);
            mediaController.setAnchorView(playBackVideo);
            Uri uri = Uri.parse(vidLoc);
            playBackVideo.setVideoURI(uri);
            playBackVideo.setMediaController(mediaController);

//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    vidLoc = files.get(position).getFile().getAbsolutePath();
//                    //System.out.println("VIDLOC : "+vidLoc);
//
//                    Uri uri = Uri.parse(vidLoc);
//                    playBackVideo.setVideoURI(uri);
//                    playBackVideo.setMediaController(mediaController);
//                    playBackVideo.start();
//                    //alertDialog.dismiss();
//                }
//            });
            // This is what happens every time an item is selected in the gridview
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    if (playBackVideo.isPlaying() ) {
                        final int time = playBackVideo.getCurrentPosition();
                        vidLoc = files.get(position).getFile().getAbsolutePath();
                        Uri uri = Uri.parse(vidLoc);
                        playBackVideo.setVideoURI(uri);
                        playBackVideo.setMediaController(mediaController);
                        playBackVideo.seekTo(time);
                    //    playBackVideo.requestFocus();
                        playBackVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mp) {
                                playBackVideo.seekTo(time);
                            }
                        });
                        setUpVideo(position);
                        playBackVideo.start();
                    } else {
                        vidLoc = files.get(position).getFile().getAbsolutePath();
                        Uri uri = Uri.parse(vidLoc);
                        playBackVideo.setVideoURI(uri);
                        playBackVideo.setMediaController(mediaController);
                        playBackVideo.start();
                    }
                }
            });
        }
    }



    private void setUpVideo(int i) {
        playBackVideo.stopPlayback();
        String vidLoc = files.get(i).getFile().getAbsolutePath();
        playBackVideo.setVideoPath(vidLoc);
    }



    void loadProject() {
        count = 0;
        ArrayList<String> projectNames = metaData.getProjectsNames();
        String[] list = new String[projectNames.size()];
        for(int i=0;i<projectNames.size();i++)
            list[i] = projectNames.get(i);
        final String[] projects = list;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick A Project to Load")
                .setItems(projects, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //JSONObject projectObject = (JSONObject) projectBrowser.loadProject(projectName);
                        try {
                            String projectName = projects[which];
                            currentProject = metaData.getProject(projectName);
                            System.out.println("current project : "  + currentProject.toString());
                            if (currentProject.get("locations")!=null) {
                                ArrayList<String> list = (ArrayList<String>) currentProject.get("locations");
                                if (list.size() > 0) {
                                    filesLocation = new ArrayList<String>();
                                    for (int i = 0; i < list.size(); i++) {
                                        filesLocation.add((String) list.get(i));
                                    }

                                    loadVideoList(filesLocation);
                                }else{
                                    dialogDisplay("no videos in this project");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

     void saveProject() {
         if(filesLocation.size()==0){
             dialogDisplay("No videos in the project");
         }
         else {
             final EditText userInput = new EditText(getActivity());
             userInput.setMaxLines(1);
             AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
             builder.setTitle("Enter the Name of the Project")
                     .setView(userInput)
                     .setCancelable(false)
                     .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             String projectName = userInput.getText().toString();
                             //projectName = projectName.replaceAll("\\s+","");
                             JSONObject object = new JSONObject();
                             try {
                                 object.put("name", projectName);
                                 object.put("locations", filesLocation);
                                 metaData.putProject(object);
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }


                         }
                     })
                     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                         }
                     });
             AlertDialog alertDialog = builder.create();
             alertDialog.show();
         }
    }

     void newProject() {
        currentProject = new JSONObject();
        try {
            JSONObject locations = currentProject.put("locations", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        filesLocation = new ArrayList<String>();
        loadVideoList(filesLocation);
    }


     void deleteProject() {

        ArrayList<String> projectNames = metaData.getProjectsNames();
        String[] list = new String[projectNames.size()];
        for(int i=0;i<projectNames.size();i++)
            list[i] = projectNames.get(i);
        final String[] projects = list;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick A Project to Delete")
                .setItems(projects, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        metaData.deleteProject(metaData.getProject(projects[which]));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void dialogDisplay(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


     void releaseMediaRecorder(){
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

     void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
     boolean prepareVideoRecorder(){

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
        currentFile = CameraHelper.getOutputMediaFile(VIDEOS_LOCATION);
        if (currentFile == null) {
            return false;
        }
        mMediaRecorder.setOutputFile(currentFile.getAbsolutePath());

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
               // video.requestFocus(); causes bugs
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

     class MyListAdapter extends ArrayAdapter<VideoThumbnail> {
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


