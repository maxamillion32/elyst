package com.elystapp.elyst.data;

import android.location.Address;

import java.util.Calendar;


/**
 * Created by roxanagheorghe on 5/18/16.
 *
 */
public class Event {

    /**
     * Field for the event
     */
    private Integer eImage;
    private String eTitle;
    private Calendar eDateTime;
    private String eLocation;
    private String eDescription;
    private Double eCost;
    private Integer ID;
    private Host eHost;

    // Helping Fields
    private Address locationGPS;

    public Event(){
        this.eImage = -1;
        this.eTitle = "";
        this.eDateTime  = Calendar.getInstance();
        this.eLocation  =   "";
        this.eDescription   =   "";
        this.eCost  =   0.0d;
        this.ID =   -1;
        this.eHost= new Host();
    }

    public Integer getImage() {
        return eImage;
    }

    public void setImage(Integer eImage) {
        this.eImage = eImage;
    }

    public String getTitle() {
        return eTitle;
    }

    public void setTitle(String eTitle) {
        this.eTitle = eTitle;
    }

    public Calendar getDateTime() {
        return eDateTime;
    }

    public void setDateTime(Calendar eDate) {
        this.eDateTime = eDate;
    }

    public void setDateTimeInMillis(long date){

        eDateTime.setTimeInMillis(date);
    }

    public long geteDateTimeInMillis() {
        return eDateTime.getTimeInMillis();
    }


    // Set the Date
    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        eDateTime.set(year, monthOfYear, dayOfMonth);
    }

    // Set the Time
    public void setTime(int hourOfDay, int minute) {
        eDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eDateTime.set(Calendar.MINUTE, minute);
        eDateTime.set(Calendar.SECOND, 0);
    }

    public String getLocation() {
        return eLocation;
    }

    public void setLocation(String eLocation) {
        this.eLocation = eLocation;
    }

    public void setGPS(double lat, double lon) {
        locationGPS.setLatitude(lat);
        locationGPS.setLongitude(lon);
    }

    public Address getGPS() { return locationGPS; }

    public String getDescription() {
        return eDescription;
    }

    public void setDescription(String eDescription) {
        this.eDescription = eDescription;
    }

    public Double getCost() {
        return eCost;
    }

    public void setCost(Double eCost) {
        this.eCost = eCost;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public double generateID(long date, long time, double lat, double lon) {
        double resultID = 0.0;

        return resultID;
    }


    public Host geteHost() {
        return eHost;
    }

    public void seteHost(Host eHost) {
        this.eHost = eHost;
    }
}
