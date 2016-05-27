package com.elystapp.elyst.data;

import java.util.Calendar;

/**
 * Created by roxanagheorghe on 5/26/16.
 */
public class EventFacts{
    double cost;
    Calendar dateTime;
    String description;
    Long eDateTimeInMillis;
    Host eHost;
    int guests;
    String id;
    int image;
    String location;
    String title;

    public EventFacts(){
        // empty default constructor, necessary for Firebase to be able to deserialize
    }
    public double getCost(){
        return cost;

    }
    public Calendar getDateTime(){
        return dateTime;
    }
    public String getDescription(){
        return description;
    }
    public Long geteDateTimeInMillis(){
        return eDateTimeInMillis;
    }

    public Host geteHost() {
        return eHost;
    }

    public int getGuests() {
        return guests;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }
}