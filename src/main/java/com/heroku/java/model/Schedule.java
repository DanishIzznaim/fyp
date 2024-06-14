package com.heroku.java.model;
import java.util.Date;

public class Schedule {

    private int scheduleid ;
    private String schedulename;
    private String description;
    private java.sql.Date startdate;
    private java.sql.Date enddate;

    public Schedule() {
    }

    public Schedule(int scheduleid, String schedulename, String description, java.sql.Date startdate, java.sql.Date enddate) {
        this.scheduleid = scheduleid;
        this.schedulename = schedulename;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public int getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(int scheduleid) {
        this.scheduleid = scheduleid;
    }

    public String getSchedulename() {
        return schedulename;
    }

    public void setSchedulename(String schedulename) {
        this.schedulename = schedulename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.sql.Date getStartdate() {
        return startdate;
    }

    public void setStartdate(java.sql.Date startdate) {
        this.startdate = startdate;
    }

    public java.sql.Date getEnddate() {
        return enddate;
    }

    public void setEnddate(java.sql.Date enddate) {
        this.enddate = enddate;
    }

    
}

