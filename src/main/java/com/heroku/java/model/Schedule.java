// package com.heroku.java.model;
// import java.util.Date;

// public class Schedule {

//     private int scheduleid ;
//     private String schedulename;
//     private String description;
//     private java.sql.Date startdate;
//     private java.sql.Date enddate;

//     public Schedule() {
//     }

//     public Schedule(int scheduleid, String schedulename, String description, java.sql.Date startdate, java.sql.Date enddate) {
//         this.scheduleid = scheduleid;
//         this.schedulename = schedulename;
//         this.description = description;
//         this.startdate = startdate;
//         this.enddate = enddate;
//     }

//     public int getScheduleid() {
//         return scheduleid;
//     }

//     public void setScheduleid(int scheduleid) {
//         this.scheduleid = scheduleid;
//     }

//     public String getSchedulename() {
//         return schedulename;
//     }

//     public void setSchedulename(String schedulename) {
//         this.schedulename = schedulename;
//     }

//     public String getDescription() {
//         return description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public java.sql.Date getStartdate() {
//         return startdate;
//     }

//     public void setStartdate(java.sql.Date startdate) {
//         this.startdate = startdate;
//     }

//     public java.sql.Date getEnddate() {
//         return enddate;
//     }

//     public void setEnddate(java.sql.Date enddate) {
//         this.enddate = enddate;
//     }

    
// }

package com.heroku.java.model;

import java.sql.Date;

public class Schedule {

    private int scheduleid;
    private String month;
    private String week;
    private Date startdate;
    private Date enddate;

    public Schedule() {
    }

    public Schedule(int scheduleid, String month, String week, Date startdate, Date enddate) {
        this.scheduleid = scheduleid;
        this.month = month;
        this.week = week;
        this.startdate = startdate;
        this.enddate = enddate;
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
}
