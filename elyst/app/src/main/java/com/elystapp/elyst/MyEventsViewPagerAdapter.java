package com.elystapp.elyst;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ERNEL on 5/16/2016.
 */
public class MyEventsViewPagerAdapter extends FragmentPagerAdapter{

    // Collection of the fragments tabs
    private ArrayList<Fragment> myEventsFragments;

    // Fragments ID and Strings
    public static final int HOSTING     = 0;
    public static final int ATTENDING   = 1;
    public static final int WATCHLIST   = 2;
    public static final String UI_TAB_HOSTING   = "Hosting";
    public static final String UI_TAB_ATTENDING = "Attending";
    public static final String UI_TAB_WATCHLIST = "Watchlist";

    // Constructor - initiate the array of fragments
    public MyEventsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        myEventsFragments = fragments;
    }

    @Override
    // Return the fragment at that position
    public Fragment getItem(int position) {
        return myEventsFragments.get(position);
    }

    @Override
    // Get the number of fragment
    public int getCount() {
        return myEventsFragments.size();
    }

    // Return the name of the tab by returning a char sequence
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case HOSTING:
                return UI_TAB_HOSTING;
            case ATTENDING:
                return UI_TAB_ATTENDING;
            case WATCHLIST:
                return UI_TAB_WATCHLIST;
            default:
                break;
        }
        return null;
    }
}
