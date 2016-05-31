package com.elystapp.elyst;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.elystapp.elyst.views.CustomAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@linkosting Fragment. OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HostingFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {

    // Boolean Values and Position Selector
    int mCurrentPosition = 0;

    ArrayList<ArrayList<String>> event_details = new ArrayList<ArrayList<String>>();
    ArrayList<Integer> images=new ArrayList<>();

    Integer[] imageArray = {
            R.drawable.activity_four_min,
            R.drawable.activity_two_min,
            R.drawable.activity_one_min,
            R.drawable.activity_eleven_min,
            R.drawable.activity_ten_min

    };
    Integer[] category_image={
            R.drawable.activity_four_min,
            R.drawable.activity_two_min,
            R.drawable.activity_one_min,
            R.drawable.activity_eleven_min,
            R.drawable.activity_ten_min
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // This layout contains the list
        View view = inflater.inflate(R.layout.fragment_my_events_fragments,
                container, false);

        // Now we should initialize the listview
        final ListView listview = (ListView) view.findViewById(R.id.list_events);


        ////////////////////////////////// Database
        //Database retrieving events

        //array needed for list adapter
        final List<String> events_array=new ArrayList<>();
        //current time
        Long time_now=System.currentTimeMillis();
        //firebase database reference
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mDatabase.getReference("events");
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
//                if(event_details.size()<=imageArray.length) {
//                    images.add(imageArray[event_details.size() - 1]);
//                }
//                else{
//                    images.add(imageArray[imageArray.length-1]);
//                }
                String[] events_array_adapt = events_array.toArray(new String[0]);

                //============================== LIST ITEMS ====================================
                CustomAdapter adapter = new CustomAdapter(getActivity(), event_details, images, events_array_adapt);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String Slecteditem = event_details.get(+position).get(1);
                        //Log.d(TAG, "Within item click");
                        //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                        Intent details=new Intent(getActivity(),EventDetailsActivity.class);
                        //Log.d("whichID", event_details.get(+position).get(5) + " test");
                        String event_key=event_details.get(+position).get(5);
                        details.putExtra(EventDetailsActivity.EXTRA_POST_KEY,event_key);
                        details.putExtra(EventDetailsActivity.IMAGE_KEY,imageArray[+position]);
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
                CustomAdapter adapter = new CustomAdapter(getActivity(), event_details, images, events_array_adapt);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent details=new Intent(getActivity(),EventDetailsActivity.class);
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

        return view;
    }

    /**
    // onActivityCreated() is called when the activity's onCreate() method
    // has returned.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list with our static array of titles in list in the
        // test class
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                TestDataFragmentList.EVENTS));



        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurrentPosition = savedInstanceState.getInt("curChoice", 0);
        }

        // We also highlight in uni-pane just for fun
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setItemChecked(mCurrentPosition, true);

    }
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurrentPosition);
    }

}
