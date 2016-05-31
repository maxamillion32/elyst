package com.elystapp.elyst;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class EventDetailsActivity extends AppCompatActivity {



    public static final String EXTRA_POST_KEY = "post_key";
    public static final String IMAGE_KEY="image_name";
    private String mPostKey;
    private DatabaseReference mPostReference;

    private TextView title;
    private TextView location;
    private TextView time;
    private TextView attending;
    private TextView description;
    private ImageView image;
    private ValueEventListener mPostListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setLogo(getResources().getDrawable(R.drawable.rsz_splash_icon_elyst));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        title = (TextView)findViewById(R.id.title);
        location = (TextView)findViewById(R.id.location);
        time = (TextView)findViewById(R.id.time);
        attending = (TextView)findViewById(R.id.attending);
        description = (TextView)findViewById(R.id.description);
        image=(ImageView)findViewById(R.id.details_image);
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

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        int image_id=getIntent().getIntExtra(IMAGE_KEY,0);
        image.setImageResource(image_id);

        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("events").child(mPostKey);

    }
    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                HashMap map = (HashMap) dataSnapshot.getValue();
                // [START_EXCLUDE]
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                title.setText("Title: "+(String)map.get("title"));
                location.setText("Location: "+(String)map.get("location"));
                String time2;
                //get nicely formatted time and date from the milliseconds
                String string_time = "" + map.get("eDateTimeInMillis");
                Long time_millis = Long.valueOf(string_time);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
                GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
                calendar.setTimeInMillis(time_millis);
                time2 = sdf.format(calendar.getTime());
                String date_string = time2.substring(0, 10);
                String time_string = time2.substring(10, time2.length());
                time.setText("Date & Time: "+time2);
                String guest=""+ map.get("guests");
                attending.setText("Guests: "+guest);
                description.setText("Description: "+(String)map.get("description"));

                //attending.setText((String)map.get("guests"));
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(EventDetailsActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
