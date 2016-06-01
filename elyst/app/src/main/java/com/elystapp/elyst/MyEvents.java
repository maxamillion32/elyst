package com.elystapp.elyst;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.elystapp.elyst.views.SlidingTabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MyEvents extends AppCompatActivity {

    /** Called when the activity is created **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        // Fields for the tabs
        SlidingTabLayout slidingTabLayout;
        ViewPager viewPager;

        // Fields for fragments
        ArrayList<Fragment> myEventsFragments;
        MyEventsViewPagerAdapter myEventsAdapter;

        // Toolbar Setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        myToolbar.setLogo(getResources().getDrawable(R.drawable.rsz_splash_icon_elyst));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Could do something here
            }
        });

        // Match the XML files to the proper tabs
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Determine the color of the sliding tab underscore
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });

        // Create a fragment list in order - fill it up
        myEventsFragments = new ArrayList<Fragment>();
        myEventsFragments.add(new HostingFragment());
        myEventsFragments.add(new AttendingFragment());
        myEventsFragments.add(new WatchlistFragment());


        // Use Fragment adapter to bind the slidingTablLayout and viewpager
        myEventsAdapter = new MyEventsViewPagerAdapter(getFragmentManager(),
                myEventsFragments);
        viewPager.setAdapter(myEventsAdapter);

        // Tab properties and show tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        slidingTabLayout.setViewPager(viewPager);
    }

    // Establish menu content
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_events_fragment, menu);
        return true;
    }

    // Take care of the actions used on the Menu Item
    // This method is not getting called
    public boolean onOptionsItemsSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menuitem_search:
                Intent find = new Intent(MyEvents.this, FindEventsActivity.class);
                startActivity(find);
                return true;
            case R.id.menuitem_settings:
                Intent settings=new Intent(MyEvents.this,SettingsActivity.class);
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
                    Intent signin=new Intent(MyEvents.this,SignUpActivity.class);
                    startActivity(signin);
                }
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
