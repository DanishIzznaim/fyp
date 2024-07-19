package com.heroku.java.model;

public class Payroll {
    private int payrollId;
    private int attendanceId;
    private String month;
    private int hoursWorked;
    private double hourlyRate;
    private double totalPay;

    private String sname;
    private int sid;

    public Payroll() {
    }

    public Payroll(int payrollId, int attendanceId, String month, int hoursWorked, double hourlyRate, double totalPay, String sname, int sid) {
        this.payrollId = payrollId;
        this.attendanceId = attendanceId;
        this.month = month;
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
        this.totalPay = totalPay;
        this.sname = sname;
        this.sid = sid;
    }

    // Getters and Setters
    
    public int getPayrollId() {
        return payrollId;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }
}
