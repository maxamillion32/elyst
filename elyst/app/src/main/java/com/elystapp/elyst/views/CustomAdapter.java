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

import java.util.ArrayList;


/**
 * Created by ERNEL on 5/16/2016.
 *
 */
public class CustomAdapter extends ArrayAdapter<String> {


    private Activity context;
    private ArrayList<ArrayList<String>> events;
    private ArrayList<Integer> images;

    public CustomAdapter(Activity active,
                         ArrayList<ArrayList<String>> names, ArrayList<Integer> draws, String[] some) {

        super(active, R.layout.custom_list, some);
        Log.d("whatsinNames", names.toString());
        Log.d("sizeNames", names.get(0).toString());
        events = names;
        images = draws;
        context = active;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.custom_list, null, true);



        ImageView imageView;
        TextView title;
        TextView date;
        TextView time;
        TextView address;

        imageView = (ImageView) rowView.findViewById(R.id.image_event);
        title = (TextView) rowView.findViewById(R.id.title_event);
        date = (TextView) rowView.findViewById(R.id.date_event);
        time = (TextView) rowView.findViewById(R.id.time_event);
        address = (TextView) rowView.findViewById(R.id.address_event);

        imageView.setImageResource(images.get(position));
        title.setText(events.get(position).get(0));
        time.setText(events.get(position).get(1));
        address.setText(events.get(position).get(2));
        date.setText(events.get(position).get(3));


        return rowView;

    }
}
