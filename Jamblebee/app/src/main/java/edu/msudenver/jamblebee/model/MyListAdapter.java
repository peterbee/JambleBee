package edu.msudenver.jamblebee.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import edu.msudevner.jamblebee.R;

/**
 * Created by ster on 4/16/15.
 */
public class MyListAdapter extends ArrayAdapter<VideoThumbnail> {

    Context context;
    ArrayList<VideoThumbnail> files;

    public MyListAdapter(Context c, ArrayList<VideoThumbnail> f) {
        super(c, R.layout.grid_item, f);
        context = c;
        files = f;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            itemView = mInflater.inflate(R.layout.grid_item, parent, false);
        }

        VideoThumbnail video = files.get(position);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(video.getThumb());

        return itemView;
    }
}
