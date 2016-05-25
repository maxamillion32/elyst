package com.elystapp.elyst;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.elystapp.elyst.views.CustomAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "holla";

    // Arrays of events
    String[][] eventName = {
            {"Yoga", "12:30 PM", "The River", "Wednesday 05/26"},
            {"Booze Cruise", "8:00 AM", "35 West Street, Hanover NH", "05/28/2016"},
            {"Wine Tasting", "3:30 PM", "Pine, Hanover", "Thursday"},
            {"Club Dance", "midnight", "GDX", "Saturday Night"}
    };

    Integer[] imageArray = {
            R.drawable.activity_one,
            R.drawable.activity_two,
            R.drawable.activity_three,
            R.drawable.activity_four,

    };


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

        // Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //============================== LIST ITEMS ====================================

        CustomAdapter adapter = new CustomAdapter(this, eventName, imageArray);
        ListView list = (ListView) findViewById(R.id.list_events);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = eventName[+position][1];
                Log.d(TAG, "Within item click");
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

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
