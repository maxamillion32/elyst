package com.elystapp.elyst.views;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elystapp.elyst.R;


/**
 * Created by ERNEL on 5/16/2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private static final String TAG = "holla";

    private Activity context;
    private String[][] events;
    private Integer[] images;

    public CustomAdapter(Activity active,
                         String[][] names, Integer[] draws) {
        super(active, R.layout.custom_list, names[1]);
        events = names;
        images = draws;
        context = active;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        Log.d(TAG, "After inflater");

        View rowView = inflater.inflate(R.layout.custom_list, null, true);

        Log.d(TAG, "After inflation");


        ImageView imageView;
        TextView title;
        TextView date;
        TextView time;
        TextView address;
        TextView description;

        imageView = (ImageView) rowView.findViewById(R.id.image_event);
        title = (TextView) rowView.findViewById(R.id.title_event);
        date = (TextView) rowView.findViewById(R.id.date_event);
        time = (TextView) rowView.findViewById(R.id.time_event);
        address = (TextView) rowView.findViewById(R.id.address_event);

        imageView.setImageResource(images[position]);
        title.setText(events[position][0]);
        time.setText(events[position][1]);
        address.setText(events[position][2]);
        date.setText(events[position][3]);


        return rowView;

    }
}
