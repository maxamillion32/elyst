package com.elystapp.elyst.data;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by roxanagheorghe on 5/18/16.
 */
public class Event {

    private Integer eImage;
    private String eTitle;
    private Date eDate;
    private Time eTime;
    private String eLocation;
    private String eDescription;
    private Double eCost;
    private Integer ID;
    //private Host eHost;

    public Event(){
        this.eImage=-1;
        this.eTitle="";
        this.eDate= Calendar.getInstance().getTime();
        this.eTime=(Time)Calendar.getInstance().getTime();
        this.eLocation="";
        this.eDescription="";
        this.eCost=0.0d;
        this.ID=-1;
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

    public Date getDate() {
        return eDate;
    }

    public void setDate(Date eDate) {
        this.eDate = eDate;
    }

    public Time geteTime() {
        return eTime;
    }

    public void seteTime(Time eTime) {
        this.eTime = eTime;
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


}
