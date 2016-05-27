package com.elystapp.elyst;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.elystapp.elyst.views.CustomAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "holla";

//    // Arrays of events
//    String[][] eventName = {
//            {"Yoga", "12:30 PM", "The River", "Wednesday 05/26","description"},
//            {"Booze Cruise", "8:00 AM", "35 West Street, Hanover NH", "05/28/2016","description"},
//            {"Wine Tasting", "3:30 PM", "Pine, Hanover", "Thursday","description"},
//            {"Club Dance", "midnight", "GDX", "Saturday Night","description"}
//    };

    ArrayList<ArrayList<String>> event_details = new ArrayList<ArrayList<String>>();
    ArrayList<Integer> images=new ArrayList<>();

//    Integer[] imageArray = {
//            R.drawable.activity_one,
//            R.drawable.activity_two,
//            R.drawable.activity_three,
//            R.drawable.activity_four,
//
//    };

    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar Setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        myToolbar.setLogo(getResources().getDrawable(R.drawable.rsz_splash_icon_elyst));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create_event = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(create_event);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Database retrieving events
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("events");

        final Query events_query=myRef.orderByChild("eDateTimeInMillis");
        events_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, HashMap> map = (HashMap<String, HashMap>) dataSnapshot.getValue();

                List<String> events_array=new ArrayList<String>();
                for (int i = 0; i < map.values().size(); i++) {

                    ArrayList<String> event = new ArrayList<String>();
                    HashMap temp_map = (HashMap) map.values().toArray()[i];
                    events_array.add((String) temp_map.get("title"));
                    event.add((String) temp_map.get("title"));
                    String time =  temp_map.get("eDateTimeInMillis")+"";
                    Long time_millis= (Long)temp_map.get("eDateTimeInMillis");
                    Date date_long=new Date(time_millis);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a");
                    sdf.setTimeZone(TimeZone.getTimeZone("US/Eastern"));

                    GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
                    calendar.setTimeInMillis(time_millis);
                    time=sdf.format(calendar.getTime());
                    String date_string= time.substring(0,10);
                    String time_string= time.substring(10,time.length());
                    event.add(time_string);
                    event.add((String) temp_map.get("location"));
                    String date = temp_map.get("eDateTimeInMillis")+"";
                    event.add(date_string);
                    event.add((String)temp_map.get("description"));
                    event_details.add(event);
                    images.add(R.drawable.activity_one);
                }
                String [] events_array_adapt = events_array.toArray(new String[0]);

                //============================== LIST ITEMS ====================================
                CustomAdapter adapter= new CustomAdapter(MainActivity.this,event_details,images,events_array_adapt);
                //CustomAdapter adapter = new CustomAdapter(this, eventName, imageArray);
                ListView list = (ListView) findViewById(R.id.list_events);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String Slecteditem = event_details.get(+position).get(1);
                        Log.d(TAG, "Within item click");
                        Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("idontknow", "The read failed: " + databaseError.getMessage());

            }
        });



    }

    @Override
    public void onResume(){
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menuitem_search:
                Toast.makeText(this, "Search",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_settings:
                Toast.makeText(this, "Settings",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_log_out:
                Toast.makeText(this, "Log Out",
                        Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // Intent and throw new action
        Intent intent = new Intent();

        if (id == R.id.drawer_find_events) {
            Intent find = new Intent(MainActivity.this, FindEventsActivity.class);
            startActivity(find);

        } else if (id == R.id.drawer_sign_up) {
            Intent sign_up =new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(sign_up);

        } else if (id == R.id.drawer_settings) {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);

        } else if (id == R.id.drawer_my_events) {
            intent.setClass(this, MyEvents.class);
            startActivity(intent);
        } else if (id == R.id.drawer_create_event) {
            Intent create = new Intent(MainActivity.this, CreateEventActivity.class);
            startActivity(create);

        } else if (id == R.id.drawer_account_preferences) {
            Intent preference = new Intent(MainActivity.this, AccountPreferencesActivity.class);
            startActivity(preference);


        } else if (id == R.id.drawer_about) {
            Intent about_us = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(about_us);

        } else if (id == R.id.drawer_rate_us) {
            Uri uri = Uri.parse("https://play.google.com/store?hl=en" );
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store?hl=en")));
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
