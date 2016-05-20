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
 *
 */
public class EventDBHelper extends SQLiteOpenHelper{

    // Declare all the keys here with their associated values
    public static final String KEY_IMAGE    = "event_image";
    public static final String KEY_TITLE    = "event_title";
    public static final String KEY_DATE_TIME= "event_date_time";
    public static final String KEY_LOCATION = "event_location";
    public static final String KEY_COORDS   = "event_coordinates";
    public static final String KEY_COST     = "event_cost";
    public static final String KEY_ID       = "event_id";
    public static final String KEY_HOST     = "event_host";
    public static final String KEY_ROW_ID   = "event_row_id";
    public static final String KEY_DESCRIPTION    = "event_description";
    public static final String TABLE_NAME_ENTRIES = "entries";



    private static final String[] fieldsList = {KEY_COORDS, KEY_COST,
                    KEY_DATE_TIME, KEY_HOST, KEY_ID, KEY_IMAGE, KEY_LOCATION,
                    KEY_ROW_ID, KEY_TITLE, KEY_DESCRIPTION};

    // Database Fields
    private static final String DATABASE_NAME = "EventDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String CREAT_TABLE_ENTRIES = "CREATE TABLE IF NOT EXIST "
            + TABLE_NAME_ENTRIES + " ("
            + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_IMAGE + " INTEGER NOT NULL, "
            + KEY_TITLE + " INTEGER NOT NULL, "
            + KEY_DATE_TIME + " DATETIME NOT NULL, "
            + KEY_LOCATION + " INTEGER NOT NULL, "
            + KEY_COORDS + " DOUBLE NOT NULL, "
            + KEY_COST + " DOUBLE NOT NULL, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_ID + " LONG NOT NULL, "
            + KEY_HOST + " BLOB, " + ");";

    // Constructor
    public EventDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table following the format defined above
        db.execSQL(CREAT_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Insert an item given each column value
    public long insertEvent(Event event) {

        // Make a value variable to insert field one by one
        ContentValues value = new ContentValues();
        value.put(KEY_IMAGE, event.getImage());
        value.put(KEY_TITLE, event.getTitle());
        value.put(KEY_DATE_TIME, event.geteDateTimeInMillis());
        // value.put(KEY_TIME, event.geteTimeInMillis());
        value.put(KEY_LOCATION, event.getLocation());
        value.put(KEY_COORDS, event.getLocation());
        value.put(KEY_COST, event.getCost());
        value.put(KEY_DESCRIPTION, event.getDescription());
        value.put(KEY_ID, event.getID());
        // value.put(KEY_HOST, event.getHost());

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
    public Event fetchEventByIndex(long rowID) throws SQLException {

        SQLiteDatabase liteDatabase = getReadableDatabase();
        Cursor tempCursor = liteDatabase.query(true, TABLE_NAME_ENTRIES, fieldsList, KEY_ROW_ID + rowID,
                null, null, null, null, null);
        boolean first = tempCursor.moveToFirst();
        Event tempEvent = null;

        if (first)
            tempEvent = cursorToEntry(tempCursor, true);

        tempCursor.close();
        liteDatabase.close();
        return tempEvent;
    }

    // Query the entire table, return all rows
    public ArrayList<Event> fetchEntries() {

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
    private Event cursorToEntry(Cursor cursor, boolean bool) {

        Event tempEvent = new Event();

        // Set all the value according to our field

        tempEvent.setImage(cursor.getInt(cursor.getColumnIndex(KEY_IMAGE)));
        tempEvent.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        tempEvent.setDateTimeInMillis(cursor.getLong(cursor.getColumnIndex(KEY_DATE_TIME)));
        tempEvent.setDescription(cursor.getString((cursor.getColumnIndex(KEY_DESCRIPTION))));
        tempEvent.setCost(cursor.getDouble(cursor.getColumnIndex(KEY_COST)));
        // tempEvent.setLocation();
        // tempEvent.setCoordinates();
        // tempEvent.setHost();

        return tempEvent;

    }
}
