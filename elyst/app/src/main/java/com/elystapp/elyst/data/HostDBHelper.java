package com.elystapp.elyst.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ERNEL on 5/20/2016.
 */
public class HostDBHelper extends SQLiteOpenHelper{

    // Declare all the keys here with their associated values
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PASSWORD= "host_password";
    public static final String KEY_NAME    = "host_name";
    public static final String KEY_EMAIL   = "host_email";
    public static final String KEY_PHONE   = "host_phone";
    public static final String KEY_IMAGE   = "host_image";

    public static final String KEY_ROW_ID  = "_id";
    public static final String TABLE_NAME_ENTRIES = "entries";


    private static final String[] fieldsList = {KEY_NAME, KEY_EMAIL,
            KEY_PHONE, KEY_ROW_ID, KEY_USER_ID, KEY_PASSWORD, KEY_IMAGE};

    // Database Fields
    private static final String DATABASE_NAME = "HostDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXIST "
            + TABLE_NAME_ENTRIES + " ("
            + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_USER_ID + " STRING NOT NULL, "
            + KEY_PASSWORD + " STRING NOT NULL, "
            + KEY_NAME + " STRING NOT NULL, "
            + KEY_EMAIL + " STRING NOT NULL, "
            + KEY_PHONE + " STRING NOT NULL, "
            + KEY_IMAGE + " INTEGER NOT NULL, "+ ");";

    // Constructor
    public HostDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table following the format defined above
        db.execSQL(CREATE_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Insert an item given each column value
    public long insertHost(Host host) {

        // Make a value variable to insert field one by one
        ContentValues value = new ContentValues();
        value.put(KEY_IMAGE, host.getImage());
        value.put(KEY_EMAIL, host.getEmail());
        value.put(KEY_NAME, host.getName());
        value.put(KEY_PASSWORD, host.getPassword());
        value.put(KEY_PHONE, host.getPhone());
        value.put(KEY_USER_ID, host.getUser_ID());

        SQLiteDatabase liteDatabase = getWritableDatabase();
        long entry = liteDatabase.insert(TABLE_NAME_ENTRIES, null, value);
        liteDatabase.close();
        return entry;
    }

    // Remove an entry by giving its index
    public void removeEvent(long rowIndex) {
        SQLiteDatabase liteDBase = getWritableDatabase();
        liteDBase.delete(TABLE_NAME_ENTRIES, KEY_ROW_ID + rowIndex, null);
        liteDBase.close();
    }

    // Query a specific entry by its index
    public Host fetchHostByIndex(long rowID) throws SQLException {

        SQLiteDatabase liteDatabase = getReadableDatabase();
        Cursor tempCursor = liteDatabase.query(true, TABLE_NAME_ENTRIES, fieldsList, KEY_ROW_ID + rowID,
                null, null, null, null, null);
        boolean first = tempCursor.moveToFirst();
        Host tempHost = null;

        if (first)
            tempHost = cursorToEntry(tempCursor, true);

        tempCursor.close();
        liteDatabase.close();
        return tempHost;
    }

    // Query the entire table, return all rows
    public ArrayList<Host> fetchEntries() {

        SQLiteDatabase liteDatabase = getReadableDatabase();
        ArrayList tempList = new ArrayList();
        Cursor tempCursor = liteDatabase.query(TABLE_NAME_ENTRIES, fieldsList,
                null, null, null, null, null);

        while (tempCursor.moveToNext())
            tempList.add(cursorToEntry(tempCursor, false));

        tempCursor.close();
        liteDatabase.close();
        return tempList;
    }

    // Cursor to Entry helper
    private Host cursorToEntry(Cursor cursor, boolean bool) {

        Host tempHost = new Host();

        // Set all the value according to our field

        tempHost.setImage(cursor.getInt(cursor.getColumnIndex(KEY_IMAGE)));
        tempHost.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
        tempHost.setUser_ID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
        tempHost.setPassword(cursor.getString((cursor.getColumnIndex(KEY_PASSWORD))));
        tempHost.setPhone(cursor.getLong(cursor.getColumnIndex(KEY_PHONE)));

        return tempHost;

    }
}

