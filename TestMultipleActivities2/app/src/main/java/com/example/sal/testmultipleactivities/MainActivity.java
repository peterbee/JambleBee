package com.example.sal.testmultipleactivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

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

        switch(item.getItemId()) {
            case R.id.menu_main_action_editor:
                startActivity(new Intent(MainActivity.this, VideoProjectEditor.class));
                // switch activity
                return true;
            case R.id.menu_main_action_browser:
                startActivity(new Intent(MainActivity.this, VideoProjectBrowser.class));
                // switch activity
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
