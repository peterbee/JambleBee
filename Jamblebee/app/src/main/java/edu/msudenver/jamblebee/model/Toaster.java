package edu.msudenver.jamblebee.model;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ster on 4/16/15.
 */
public class Toaster {

    Context context;

    public Toaster (Context c){
        context = c;
    }

    public void showInstructionToast() {
        Toast toast = Toast.makeText(context, "Tap a video path to start recording.", Toast.LENGTH_LONG);
        toast.show();
    }
}
