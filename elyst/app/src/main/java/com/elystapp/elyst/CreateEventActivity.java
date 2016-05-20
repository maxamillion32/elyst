package com.elystapp.elyst;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    Context context;
    TextView infoText;
    int fetchType = Constants.USE_ADDRESS_LOCATION;
    AddressResultReceiver mResultReceiver;




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
        mResultReceiver = new AddressResultReceiver(null);
        context=this;
        fetchType = Constants.USE_ADDRESS_NAME;

        EditText title = (EditText) findViewById(R.id.entered_title);
        final EditText location = (EditText) findViewById(R.id.entered_location);
        EditText time = (EditText) findViewById(R.id.entered_time);
        EditText attending = (EditText) findViewById(R.id.entered_attending);
        EditText description = (EditText) findViewById(R.id.entered_description);

        infoText = (TextView) findViewById(R.id.infoText);

        Button submit = (Button) findViewById(R.id.save_event);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventActivity.this, GeocodeAddressIntentService.class);
                intent.putExtra(Constants.RECEIVER, mResultReceiver);
                intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
                if(fetchType == Constants.USE_ADDRESS_NAME) {
                    if(location.getText().length() == 0) {
                        Toast.makeText(context, "Please enter an address name", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, location.getText().toString());
                }
                //Toast.makeText(context, "Your request has been submitted",Toast.LENGTH_SHORT).show();
                //infoText.setVisibility(View.INVISIBLE);
                Log.e("create_event", "Starting Service");
                startService(intent);
                //finish();

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

    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

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
                        infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            }
        }
    }

}
