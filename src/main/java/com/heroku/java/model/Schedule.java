package com.heroku.java.model;

import java.sql.Date;

public class Schedule {

    private int scheduleid;
    private String month;
    private String week;
    private Date startdate;
    private Date enddate;
    private boolean isAssigned; // New field

    public Schedule() {
    }

    public Schedule(int scheduleid, String month, String week, Date startdate, Date enddate, boolean isAssigned) {
        this.scheduleid = scheduleid;
        this.month = month;
        this.week = week;
        this.startdate = startdate;
        this.enddate = enddate;
        this.isAssigned = isAssigned;
    }

    public int getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(int scheduleid) {
        this.scheduleid = scheduleid;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public boolean getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }
}
