package com.elystapp.elyst;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        EditText title = (EditText) findViewById(R.id.entered_title);
        EditText location = (EditText) findViewById(R.id.entered_location);
        EditText time = (EditText) findViewById(R.id.entered_time);
        EditText attending = (EditText) findViewById(R.id.entered_attending);
        EditText description = (EditText) findViewById(R.id.entered_description);

        Button submit = (Button) findViewById(R.id.save_event);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Your request has been submitted",Toast.LENGTH_SHORT).show();
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

    }

}
