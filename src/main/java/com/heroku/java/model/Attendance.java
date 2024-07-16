// package com.heroku.java.model;

// import java.time.LocalDateTime;

// public class Attendance {
    
//     private int attendanceId;
//     private int id;
//     private LocalDateTime attendanceDate;
//     private LocalDateTime signInTime;
//     private LocalDateTime signOutTime;
//     private String status;

//     public Attendance() {}

//     public Attendance(int attendanceId, int id, LocalDateTime attendanceDate, LocalDateTime signInTime, LocalDateTime signOutTime, String status) {
//         this.attendanceId = attendanceId;
//         this.id = id;
//         this.attendanceDate = attendanceDate;
//         this.signInTime = signInTime;
//         this.signOutTime = signOutTime;
//         this.status = status;
//     }

//     public int getAttendanceId() {
//         return attendanceId;
//     }

//     public void setAttendanceId(int attendanceId) {
//         this.attendanceId = attendanceId;
//     }

//     public int getId() {
//         return id;
//     }

//     public void setId(int id) {
//         this.id = id;
//     }

//     public LocalDateTime getAttendanceDate() {
//         return attendanceDate;
//     }

//     public void setAttendanceDate(LocalDateTime attendanceDate) {
//         this.attendanceDate = attendanceDate;
//     }

//     public LocalDateTime getSignInTime() {
//         return signInTime;
//     }

//     public void setSignInTime(LocalDateTime signInTime) {
//         this.signInTime = signInTime;
//     }

//     public LocalDateTime getSignOutTime() {
//         return signOutTime;
//     }

//     public void setSignOutTime(LocalDateTime signOutTime) {
//         this.signOutTime = signOutTime;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public void setStatus(String status) {
//         this.status = status;
//     }
// }
package com.heroku.java.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Attendance {
    
    private int attendanceId;
    private int id;
    private LocalDate attendanceDate;
    private LocalTime signInTime;
    private LocalTime signOutTime;
    private String status;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Attendance() {}

    public Attendance(int attendanceId, int id, LocalDate attendanceDate, LocalTime signInTime, LocalTime signOutTime, String status) {
        this.attendanceId = attendanceId;
        this.id = id;
        this.attendanceDate = attendanceDate;
        this.signInTime = signInTime;
        this.signOutTime = signOutTime;
        this.status = status;
    }

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
}

       
