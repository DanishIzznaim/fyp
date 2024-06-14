package com.heroku.java.model;

public class Assign {
    
    private int assignid;
    private int id;
    private int scheduleid;
    private String dt1;
    private String dt2;
    private String dt3;
    private String dt4;
    private String dt5;
    private String dt6;
    private String dt7;
    
    public Assign() {
    }

    public Assign(int assignid, int id, int scheduleid, String dt1, String dt2, String dt3, String dt4, String dt5,
            String dt6, String dt7) {
        this.assignid = assignid;
        this.id = id;
        this.scheduleid = scheduleid;
        this.dt1 = dt1;
        this.dt2 = dt2;
        this.dt3 = dt3;
        this.dt4 = dt4;
        this.dt5 = dt5;
        this.dt6 = dt6;
        this.dt7 = dt7;
    }

    public int getAssignid() {
        return assignid;
    }

    public void setAssignid(int assignid) {
        this.assignid = assignid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(int scheduleid) {
        this.scheduleid = scheduleid;
    }

    public String getDt1() {
        return dt1;
    }

    public void setDt1(String dt1) {
        this.dt1 = dt1;
    }

    public String getDt2() {
        return dt2;
    }

    public void setDt2(String dt2) {
        this.dt2 = dt2;
    }

    public String getDt3() {
        return dt3;
    }

    public void setDt3(String dt3) {
        this.dt3 = dt3;
    }

    public String getDt4() {
        return dt4;
    }

    public void setDt4(String dt4) {
        this.dt4 = dt4;
    }

    public String getDt5() {
        return dt5;
    }

    public void setDt5(String dt5) {
        this.dt5 = dt5;
    }

    public String getDt6() {
        return dt6;
    }

    public void setDt6(String dt6) {
        this.dt6 = dt6;
    }

    public String getDt7() {
        return dt7;
    }

    public void setDt7(String dt7) {
        this.dt7 = dt7;
    }
    

}