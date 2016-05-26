package com.elystapp.elyst;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    static Context context;
    static int fetchType = Constants.USE_ADDRESS_LOCATION;
    static AddressResultReceiver mResultReceiver;
    // Write a message to the database
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    //the event being created
    static Event current_event ;



    public static String title_text="Title";
    public static String location_text="Location";
    public static String date_text = "Date";
    public static String time_text="Time";
    public static String cost_text="Cost";
    public static String guest_text= "Guests";
    public static String description_text="Description";


    public static TextView title_view;
    public static TextView location_view;
    public static TextView date_view;
    public static TextView time_view;
    public static TextView cost_view;
    public static TextView guest_view;
    public static TextView description_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setLogo(getResources().getDrawable(R.drawable.rsz_splash_icon_elyst));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        current_event = new Event();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("events");
        mResultReceiver = new AddressResultReceiver(null);
        context=this;
        fetchType = Constants.USE_ADDRESS_NAME;


        Button submit = (Button) findViewById(R.id.save_event);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference newRef =myRef.push();
                newRef.setValue(current_event);
                String postId=newRef.getKey();
                current_event.setID(postId);
                myRef.child(postId).child("id").setValue(postId);
                Log.d("uniqueID", postId);
                finish();

            }
        });
        Button cancel = (Button) findViewById(R.id.cancel_event);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Request canceled",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        title_view=(TextView)findViewById(R.id.title_event);
        title_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TitleFragment title_dialog= new TitleFragment();
                title_dialog.show(getSupportFragmentManager(), "title");
            }
        });
        location_view=(TextView)findViewById(R.id.location_event);
        location_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationFragment loc = new LocationFragment();
                loc.show(getSupportFragmentManager(),"location");
                updateAddress();
            }
        });
        date_view=(TextView)findViewById(R.id.date_event);
        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment d = new DatePickerFragment();
                d.show(getSupportFragmentManager(),"datePicker");
            }
        });
        time_view=(TextView)findViewById(R.id.time_event);
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment t = new TimePickerFragment();
                t.show(getSupportFragmentManager(),"timePicker");

            }
        });
        guest_view=(TextView)findViewById(R.id.attending_event);
        guest_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestFragment guest_dialog = new GuestFragment();
                guest_dialog.show(getSupportFragmentManager(),"guests");
            }
        });
        cost_view=(TextView)findViewById(R.id.cost_event);
        cost_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CostFragment cost_dialog = new CostFragment();
                cost_dialog.show(getSupportFragmentManager(),"cost");
            }
        });
        description_view=(TextView)findViewById(R.id.description_event);
        description_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DescriptionFragment desc = new DescriptionFragment();
                desc.show(getSupportFragmentManager(),"description");
            }
        });




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
                        location_text="Location: "+resultData.getString(Constants.RESULT_DATA_KEY);
                        location_view.setText(location_text);
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

        }
    }
    /**
     * class that handles the time picker dialog
     */
    public static class TimePickerFragment extends DialogFragment
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

    /**
     * class that handles the dialog for entering the comments
     */
    public static class DescriptionFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.decription_dialog_layout, null);
            final EditText et1 = (EditText) view.findViewById(R.id.inputData2);

            builder.setMessage("Description")
                    .setView(view)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d("Executing this","statement");
                            //save the comments to the exercise entry

                            final String comment_input = et1.getText().toString();
                            current_event.setDescription(comment_input);
                            Log.d("description set",current_event.getDescription());
                            description_text="Description: "+ comment_input;
                            description_view.setText(description_text);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public static class TitleFragment extends DialogFragment{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.text_dialog_layout, null);
            final EditText et1 = (EditText) view.findViewById(R.id.inputDataplace);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setMessage("Title")
                    .setView(view)
                            // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // save the distance entered to the database
                            String title_input = et1.getText().toString();
                            current_event.setTitle(title_input);
                            title_text="Title: "+title_input;
                            title_view.setText(title_text);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TitleFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();

        }
    }

    /**
     * class that handles the dialog for entering the calories
     */
    public static class CostFragment extends DialogFragment{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.custom_dialog_layout, null);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setMessage("Cost")
                    .setView(view)
                            // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // save the calories entered to the database
                            final EditText et1 = (EditText) view.findViewById(R.id.custom_dialog_edit_text);
                            final String cost_input = et1.getText().toString();

                            Double cost;
                            cost = Double.parseDouble(cost_input);
                            current_event.setCost(cost);
                            cost_text="Cost: $"+ cost_input;
                            cost_view.setText(cost_text);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CostFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();

        }
    }

    /**
     * class that handles the dialog for entering the calories
     */
    public static class GuestFragment extends DialogFragment{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.custom_dialog_layout, null);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setMessage("Guests")
                    .setView(view)
                            // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // save the calories entered to the database
                            final EditText et1 = (EditText) view.findViewById(R.id.custom_dialog_edit_text);
                            final String guests_input = et1.getText().toString();

                            int guests;
                            guests = Integer.parseInt(guests_input);
                            current_event.setGuests(guests);
                            guest_text="Guests: "+guests_input;
                            guest_view.setText(guest_text);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GuestFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();

        }
    }

    public static class LocationFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.text_dialog_layout, null);
            final EditText et1 = (EditText) view.findViewById(R.id.inputDataplace);
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setMessage("Location")
                    .setView(view)
                            // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // save the entered duration to the database
                            if(et1.getText().length() == 0) {
                                Toast.makeText(context, "Please enter an address name", Toast.LENGTH_LONG).show();
                                return;
                            }
                            final String location_input = et1.getText().toString();

                            current_event.setLocation(location_input);




                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            LocationFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
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
