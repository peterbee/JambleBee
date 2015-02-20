package com.example.sal.loadvideos;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;


public class MainActivity extends Activity {

    String filePrefix = "sdcard/DCIM/Camera/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //record

        //upload code
        switch(item.getItemId()) {
            case R.id.actionLoadVideo:
                loadVideo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadVideo() {
        File mediaDir = new File(filePrefix);
        final String[] videosArray = mediaDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".mp4");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = (View) getLayoutInflater().inflate(R.layout.list_view, null);
        ListView listView =(ListView) view.findViewById(R.id.list_view_1);
        listView.setAdapter((new VideoArrayAdapter(this, videosArray)));
        builder.setTitle("Pick a video").setView(view).setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
