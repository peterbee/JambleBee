package com.example.sal.testmultipleactivities;


import android.content.Context;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoArrayAdapter extends ArrayAdapter<String>{
    Context context;
    String videoNames [];

    public VideoArrayAdapter(Context context, String videoNames[]) {
        super(context, R.layout.list_row, videoNames);
        this.videoNames = videoNames;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_row, null);
        }
        System.out.print(this.videoNames.length + " " + position);
        String name = this.videoNames[position];
        TextView textView = (TextView) rowView.findViewById(R.id.row_text);
        ImageView thumbnail = (ImageView) rowView.findViewById(R.id.row_image);
        textView.setText(name);
        thumbnail.setImageBitmap(ThumbnailUtils.createVideoThumbnail("sdcard/DCIM/Camera/" + name, MediaStore.Video.Thumbnails.MINI_KIND));
        return rowView;
    }
}