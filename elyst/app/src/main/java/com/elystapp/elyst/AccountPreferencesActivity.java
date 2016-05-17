package com.elystapp.elyst;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccountPreferencesActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //set the callback method for the save method
        Button mSave = (Button) findViewById(R.id.save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code for saving
                saveUserInfo();
                Toast.makeText(getApplicationContext(), R.string.save_message, Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        //set the callback method for the cancel button
        Button mCancel = (Button) findViewById(R.id.cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        loadUserInfo();

    }


    /** Method to start Crop activity using the library
     *	Earlier the code used to start a new intent to crop the image,
     *	but here the library is handling the creation of an Intent, so you don't
     * have to.
     *  **/

    public void saveUserInfo() {
        //saving all of the user information in the Shared Preferences
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.prefs_file), 0).edit();
        editor.putString(getString(R.string.pref_name), ((EditText) findViewById(R.id.enteredName)).getText().toString());
        editor.putString(getString(R.string.pref_email), ((EditText) findViewById(R.id.enteredEmail)).getText().toString());
        editor.putString(getString(R.string.pref_phone), ((EditText) findViewById(R.id.enteredPhone)).getText().toString());
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.chooseGender);
        editor.putInt(getString(R.string.pref_gender), radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId())));
        editor.apply();

    }


    public void loadUserInfo() {
        //loading the saved user information from the Shared Preferences
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), 0);
        ((EditText) findViewById(R.id.enteredName)).setText(prefs.getString(getString(R.string.pref_name), BuildConfig.FLAVOR));
        ((EditText) findViewById(R.id.enteredEmail)).setText(prefs.getString(getString(R.string.pref_email), BuildConfig.FLAVOR));
        ((EditText) findViewById(R.id.enteredPhone)).setText(prefs.getString(getString(R.string.pref_phone), BuildConfig.FLAVOR));
        int int_val = prefs.getInt(getString(R.string.pref_gender), -1);
        if (int_val >= 0) {
            ((RadioButton) ((RadioGroup) findViewById(R.id.chooseGender)).getChildAt(int_val)).setChecked(true);
        }

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }






}
