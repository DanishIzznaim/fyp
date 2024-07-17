package com.heroku.java.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Attendance {
    private int attendanceId;
    private int id;
    private LocalDate attendanceDate;
    private LocalTime signInTime;
    private LocalTime signOutTime;
    private String status;
    private String staffName;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Attendance() {}

    public Attendance(int attendanceId, int id, LocalDate attendanceDate, LocalTime signInTime, LocalTime signOutTime, String status, String staffName) {
        this.attendanceId = attendanceId;
        this.id = id;
        this.attendanceDate = attendanceDate;
        this.signInTime = signInTime;
        this.signOutTime = signOutTime;
        this.status = status;
        this.staffName = staffName;
    }

    // Getters and Setters

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalTime getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(LocalTime signInTime) {
        this.signInTime = signInTime;
    }

    public LocalTime getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(LocalTime signOutTime) {
        this.signOutTime = signOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getFormattedSignInTime() {
        return signInTime != null ? signInTime.format(TIME_FORMATTER) : "";
    }

    public String getFormattedSignOutTime() {
        return signOutTime != null ? signOutTime.format(TIME_FORMATTER) : "";
    }
}
