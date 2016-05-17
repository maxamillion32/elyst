package com.elystapp.elyst;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = (TextView)findViewById(R.id.title);
        TextView location = (TextView)findViewById(R.id.location);
        TextView time = (TextView)findViewById(R.id.time);
        TextView attending = (TextView)findViewById(R.id.attending);
        TextView description = (TextView)findViewById(R.id.description);
        Button watchlist = (Button)findViewById(R.id.watchlist);
        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Watchlist","tapped");

            }
        });
        Button attend= (Button)findViewById(R.id.attend);
        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Attend","tapped");

            }
        });



    }

}
