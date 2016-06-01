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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    Integer[] imageArray = {
            R.drawable.activity_one,
            R.drawable.activity_two,
            R.drawable.activity_three,
            R.drawable.activity_four,
            R.drawable.activity_five,
            R.drawable.activity_six,
            R.drawable.activity_seven,
            R.drawable.activity_eight,
            R.drawable.activity_nine

    };

    Integer[] category_image={
            R.drawable.activity_four_min,
            R.drawable.activity_two_min,
            R.drawable.activity_one_min,
            R.drawable.activity_eleven_min,
            R.drawable.activity_ten_min
    };

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

        //initialize facebook sdk
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


        //navigation drawer initialization
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Database retrieving events

        //array needed for list adapter
        final List<String> events_array=new ArrayList<>();
        //current time
        Long time_now=System.currentTimeMillis();
        //firebase database reference
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("events");
        //getting the events ordered by datetime
        //the query only gets the first 100 events and only the events that are occurring later than the current time
        final Query latest_events_query=myRef.orderByChild("eDateTimeInMillis").limitToFirst(100).startAt(time_now);
        ChildEventListener childEventListener = latest_events_query.addChildEventListener(new ChildEventListener() {

            /**
             * method to handle what happens when a new event is added
             * @param dataSnapshot the snapshot of the current data
             * @param s previousChildName
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap map = (HashMap) dataSnapshot.getValue();
                //add one element to the list adapter array
                events_array.add((String) map.get("title"));
                //make a new event arraylist
                ArrayList<String> event = new ArrayList<>();
                //add the event title to the list
                event.add((String) map.get("title"));
                String time;
                //get nicely formatted time and date from the milliseconds
                String string_time = "" + map.get("eDateTimeInMillis");
                Long time_millis = Long.valueOf(string_time);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
                GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
                calendar.setTimeInMillis(time_millis);
                time = sdf.format(calendar.getTime());
                String date_string = time.substring(0, 10);
                String time_string = time.substring(10, time.length());
                //add the time of the event to the list
                event.add(time_string);
                //add the location of the event to the list
                event.add((String) map.get("location"));
                //add the date of the event to the list
                event.add(date_string);
                //add the description of the event to the list
                event.add((String) map.get("description"));
                //add the id of the event to the list
                //this is not displayed in the listview, but we need it as a reference when removing elements
                event.add((String)map.get("id"));
                //if we're adding the first event, simply add it
                if(event_details.isEmpty()) {
                    event_details.add(event);
                }
                else{
                    //if it's not the first event, find the right position to add it to
                    //the parameter s is the id of the previous event, so find the position of that event
                    //and add it after it
                    boolean found=false;
                    for(int i = 0;i<event_details.size();i++) {
                        ArrayList<String> event_item = event_details.get(i);
                        if (event_item.contains(s)) {
                            found=true;
                            event_details.add(i+1,event);
                            break;
                        }

                    }
                    //if the previous event was not found, it means this will be added to the top of the list
                    if(!found){
                        event_details.add(0,event);
                    }

                }
                //list adapter stuff

                String cat_text=""+map.get("category");
                int cat=Integer.valueOf(cat_text);
                images.add(category_image[cat]);

                String[] events_array_adapt = events_array.toArray(new String[0]);

                //============================== LIST ITEMS ====================================
                CustomAdapter adapter = new CustomAdapter(MainActivity.this, event_details, images, events_array_adapt);
                //CustomAdapter adapter = new CustomAdapter(this, eventName, imageArray);
                ListView list = (ListView) findViewById(R.id.list_events);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String Slecteditem = event_details.get(+position).get(1);
                        //Log.d(TAG, "Within item click");
                        //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                        Intent details=new Intent(MainActivity.this,EventDetailsActivity.class);
                        //Log.d("whichID", event_details.get(+position).get(5) + " test");
                        String event_key=event_details.get(+position).get(5);
                        details.putExtra(EventDetailsActivity.EXTRA_POST_KEY,event_key);
                        details.putExtra(EventDetailsActivity.IMAGE_KEY,images.get(position));
                        startActivity(details);

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("OnChildChanged", "called");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("OnChildRemoved", "called");
                Log.d("whatData",dataSnapshot.getValue().toString());
                Log.d("whatKey", dataSnapshot.getKey()); //the id of the event
                //dataSnapshot is the event removed
                //we get the map with all the event details
                //so we need to search the list and remove the right one
                for(int i = 0;i<event_details.size();i++){
                    ArrayList<String> event_item=event_details.get(i);
                    if(event_item.contains(dataSnapshot.getKey())){
                        event_details.remove(event_item);
                        break;
                    }
                }

                images.remove(0);
                events_array.remove(0);
                String[] events_array_adapt = events_array.toArray(new String[0]);

                //============================== LIST ITEMS ====================================
                CustomAdapter adapter = new CustomAdapter(MainActivity.this, event_details, images, events_array_adapt);
                //CustomAdapter adapter = new CustomAdapter(this, eventName, imageArray);
                ListView list = (ListView) findViewById(R.id.list_events);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent details=new Intent(MainActivity.this,EventDetailsActivity.class);
                        String event_key=event_details.get(+position).get(5);
                        details.putExtra(EventDetailsActivity.EXTRA_POST_KEY, event_key);
                        startActivity(details);

                    }
                });
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("OnChildMoved", "called");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("onCancelled", "The read failed: " + databaseError.getMessage());

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
                Intent find = new Intent(MainActivity.this, FindEventsActivity.class);
                startActivity(find);
                return true;
            case R.id.menuitem_settings:
                Intent settings=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.menuitem_log_out:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null) {
                    Toast.makeText(this, "Sign In",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Sign Out",
                            Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent signin=new Intent(MainActivity.this,SignUpActivity.class);
                    startActivity(signin);
                }
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

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user==null) {
                Toast.makeText(this, "Sign In",
                        Toast.LENGTH_SHORT).show();
                Intent sign_in =new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(sign_in);
            }
            else {
                Intent intent2 = new Intent(MainActivity.this, MyEvents.class);
                startActivity(intent2);
            }

        } else if (id == R.id.drawer_create_event) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user==null) {
                Toast.makeText(this, "Sign In",
                        Toast.LENGTH_SHORT).show();
                Intent sign_in =new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(sign_in);
            }
            else {
                Intent create = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(create);
            }


        } else if (id == R.id.drawer_account_preferences) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user==null) {
                Toast.makeText(this, "Sign In",
                        Toast.LENGTH_SHORT).show();
                Intent sign_in =new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(sign_in);
            }
            else {
                Intent preference = new Intent(MainActivity.this, AccountPreferencesActivity.class);
                startActivity(preference);
            }



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
