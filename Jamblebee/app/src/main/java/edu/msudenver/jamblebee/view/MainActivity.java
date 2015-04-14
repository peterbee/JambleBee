package edu.msudenver.jamblebee.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

import edu.msudevner.jamblebee.R;




public class MainActivity extends FragmentActivity implements DJFragment.OnFragmentInteractionListener, RecordFragment.OnFragmentInteractionListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;
    RecordFragment recordFragment;

    DJFragment djFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        recordFragment = new RecordFragment();
        djFragment = new DJFragment();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    public void loadVideo(View view) {
        recordFragment.loadVideo(view);

    }

    public void onPausePlayClick(View v) {
        recordFragment.onPausePlayClick(v);
    }

    public void onPlayClick(View v) {
        recordFragment.onPlayClick(v);
    }

    public void onPauseRecordClick(View v) {
        recordFragment.onPauseRecordClick(v);
    }

    public void onCaptureClick(View v) {
        recordFragment.onCaptureClick(v);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return recordFragment;
                case 1:
                    return djFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Record View";
                case 1:
                    return getString(R.string.title_activity_video_project_editor).toUpperCase(l);
             }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.menu_action_editor:
                startActivity(new Intent(Record.this, VideoProjectEditor.class));
                // switch activity
                break;
            case R.id.menu_load_project:
                loadProject();
                break;
            case R.id.menu_new_project:
                newProject();
                break;
            case R.id.menu_save_project:
                saveProject();
                break;
            case R.id.menu_delete_project:
                deleteProject();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
