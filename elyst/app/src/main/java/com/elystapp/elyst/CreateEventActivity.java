package com.elystapp.elyst;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.elystapp.elyst.data.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.Calendar;
import java.util.Date;


public class CreateEventActivity extends AppCompatActivity {

    static Context context;
    static int fetchType = Constants.USE_ADDRESS_LOCATION;
    static AddressResultReceiver mResultReceiver;
    // Write a message to the database
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    FirebaseStorage mStorage;

    //the event being created
    static Event current_event ;


    public static EditText title_view;
    public static EditText location_view;
    public static TextView date_view;
    public static TextView time_view;
    public static EditText cost_view;
    public static EditText guest_view;
    public static EditText description_view;

    public static String date_text="Date";
    public static String time_text="Time";
    public String location_input="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(toolbar!=null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setLogo(getResources().getDrawable(R.drawable.rsz_splash_icon_elyst));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        current_event = new Event();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("events");

        // Storage Initiation
        mStorage = FirebaseStorage.getInstance();
        mResultReceiver = new AddressResultReceiver(null);
        context=this;
        fetchType = Constants.USE_ADDRESS_NAME;

        Button submit = (Button) findViewById(R.id.save_event);
        if(submit!=null) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!title_view.getText().toString().equals("")) {
                        String title_input = title_view.getText().toString();
                        current_event.setTitle(title_input);
                    } else {
                        Toast.makeText(CreateEventActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                    }
                    location_input = location_view.getText().toString();
                    current_event.setLocation(location_input);
                    updateAddress();

                    String guests_input = guest_view.getText().toString();
                    int guests;
                    guests = Integer.parseInt(guests_input);
                    current_event.setGuests(guests);

                    String cost_input = cost_view.getText().toString();
                    Double cost;
                    cost = Double.parseDouble(cost_input);
                    current_event.setCost(cost);

                    String description_text = description_view.getText().toString();
                    current_event.setDescription(description_text);
                    Log.d("description set", current_event.getDescription());

                    DatabaseReference newRef = myRef.push();
                    newRef.setValue(current_event);
                    String postId = newRef.getKey();
                    current_event.setID(postId);
                    myRef.child(postId).child("id").setValue(postId);
                    Log.d("uniqueID", postId);
                    finish();

                }
            });
        }
        Button cancel = (Button) findViewById(R.id.cancel_event);
        if(cancel!=null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Request canceled", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });
        }

        title_view=(EditText)findViewById(R.id.entered_title);

        location_view=(EditText)findViewById(R.id.entered_location);

        date_view=(TextView)findViewById(R.id.date_event);
        Date value = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override public void onDateSet(DatePicker view,
                                                            int y, int m, int d) {
                                cal.set(Calendar.YEAR, y);
                                cal.set(Calendar.MONTH, m);
                                cal.set(Calendar.DAY_OF_MONTH, d);
                                // set the exercise entry to have the date chosen by the user
                                current_event.setDate(y, m, d);
                                Log.d("picked date", current_event.getDateTime().toString());
                                date_text="Date: "+ d+ " "+ getMonthfromInt(m)+" "+y;
                                date_view.setText(date_text);
                                // now show the time picker
                                new TimePickerDialog(CreateEventActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view, int h, int min) {
                                                cal.set(Calendar.HOUR_OF_DAY, h);
                                                cal.set(Calendar.MINUTE, min);
                                                current_event.setTime(h, min);
                                                Log.d("time picked", current_event.getDateTime().toString());
                                                if(min<10){
                                                    date_text+=" "+h+ ":0"+ min;
                                                }
                                                else {
                                                    date_text += " " + h + ":" + min;
                                                }
                                                date_view.setText(date_text);

                                            }
                                        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        guest_view=(EditText)findViewById(R.id.entered_attending);

        cost_view=(EditText)findViewById(R.id.entered_cost);

        description_view=(EditText)findViewById(R.id.entered_description);


    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        infoText.setText("Latitude: " + address.getLatitude() + "\n" +
//                                "Longitude: " + address.getLongitude() + "\n" +
//                                "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));
                        current_event.setLocationGPS(address);
                        Log.d("doneLocation","found");
                        current_event.setGPS(address.getLatitude(), address.getLongitude());
                        location_input=resultData.getString(Constants.RESULT_DATA_KEY);
                        location_view.setText(location_input);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            }
        }
    }

    /**
     * class that handles the date picker dialog
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // set the exercise entry to have the date chosen by the user
            current_event.setDate(year, month, day);
            Log.d("picked date", current_event.getDateTime().toString());
            date_text="Date: "+ day+ " "+ getMonthfromInt(month)+" "+year;
            date_view.setText(date_text);
            /**
             * class that handles the time picker dialog
             */
            class TimePickerFragment extends DialogFragment
                    implements TimePickerDialog.OnTimeSetListener {

                @NonNull
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    // Use the current time as the default values for the picker
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    // Create a new instance of TimePickerDialog and return it
                    return new TimePickerDialog(getActivity(), this, hour, minute,
                            DateFormat.is24HourFormat(getActivity()));
                }

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // set the time for the exercise entry as the time chosen by the user
                    current_event.setTime(hourOfDay, minute);
                    Log.d("time picked", current_event.getDateTime().toString());
                    time_text="Time: "+ hourOfDay+ ":"+ minute;
                    time_view.setText(time_text);
                }
            }

        }
    }


    public void updateAddress(){
        Intent intent = new Intent(CreateEventActivity.this, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
        if(fetchType == Constants.USE_ADDRESS_NAME) {

            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA,current_event.getLocation());
        }
        //Toast.makeText(context, "Your request has been submitted",Toast.LENGTH_SHORT).show();
        Log.e("create_event", "Starting Service");
        startService(intent);
    }
    public static String getMonthfromInt(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }






}
