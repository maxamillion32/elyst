package com.elystapp.elyst.data;

import android.location.Address;

import java.sql.Time;
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
    private Calendar eDate;
    private Time eTime;
    private String eLocation;
    private String eDescription;
    private Double eCost;
    private Integer ID;
    // private Host eHost;

    // Helping Fields
    private Address locationGPS;

    public Event(){
        this.eImage = -1;
        this.eTitle = "";
        this.eDate  = Calendar.getInstance();
        this.eTime  = (Time)Calendar.getInstance().getTime();
        this.eLocation  =   "";
        this.eDescription   =   "";
        this.eCost  =   0.0d;
        this.ID =   -1;
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

    public Calendar getDate() {
        return eDate;
    }

    public void setDate(Calendar eDate) {
        this.eDate = eDate;
    }

    public long geteDateInMillis() {
        return eDate.getTimeInMillis();
    }

    public Time geteTime() {
        return eTime;
    }

    public void seteTime(Time eTime) {
        this.eTime = eTime;
    }

    // Set the Date
    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        eDate.set(year, monthOfYear, dayOfMonth);
    }

    // Set the Time
    public void setTime(int hourOfDay, int minute) {
        eDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eDate.set(Calendar.MINUTE, minute);
        eDate.set(Calendar.SECOND, 0);
    }

    public String getLocation() {
        return eLocation;
    }

    public void setLocation(String eLocation) {
        this.eLocation = eLocation;
    }

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


}
