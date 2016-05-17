package com.elystapp.elyst.views;

import android.app.Activity;
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

    private Activity context;
    private String[] events;
    private Integer[] images;

    public CustomAdapter(Activity active,
                         String[] names, Integer[] draws) {
        super(active, R.layout.custom_list, names);
        events = names;
        images = draws;
        context = active;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list, null, true);

        ImageView imageView;
        TextView title;
        TextView date;
        TextView time;
        TextView address;
        TextView description;

        imageView = (ImageView) rowView.findViewById(R.id.image_event);
        title = (TextView) rowView.findViewById(R.id.title_event);
        // holder.date = (TextView) rowView.findViewById(R.id.date_event);
        // holder.time = (TextView) rowView.findViewById(R.id.time);
        address = (TextView) rowView.findViewById(R.id.address_event);

        imageView.setImageResource(images[position]);
        title.setText(events[position]);
        // time.setText(events[position]);
        // date.setText(events[position]);
        address.setText(events[position]);

        return rowView;

    }
}
