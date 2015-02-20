package com.example.sal.loadvideos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoArrayAdapter extends ArrayAdapter<String>{
    Context context;
    String videoName [];

    public VideoArrayAdapter(Context context, String videoNames[]) {
        super(context, R.layout.list_row, videoNames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_row, null);
        }
        String name = videoName[position];
        TextView textView = (TextView) rowView.findViewById(R.id.row_text);
        ImageView thumbnail = (ImageView) rowView.findViewById(R.id.row_image);
        textView.setText(name);
        return rowView;
    }
}
