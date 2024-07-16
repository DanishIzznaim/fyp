package com.heroku.java.controller;

import com.heroku.java.DAO.AttendanceDAO;
import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.model.Attendance;
import com.heroku.java.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class Attendancecontroller {

    private final AttendanceDAO attendanceDAO;
    private final StaffDAO staffDAO;

    @Autowired
    public Attendancecontroller(AttendanceDAO attendanceDAO, StaffDAO staffDAO) {
        this.attendanceDAO = attendanceDAO;
        this.staffDAO = staffDAO;
    }

    @GetMapping
    public String attendance(HttpSession session, Model model) {
        int id = (int) session.getAttribute("id");
        try {
            List<Attendance> attendances = attendanceDAO.findByStaffId(id);
            model.addAttribute("attendances", attendances);
            model.addAttribute("staffId", id);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving attendance records");
        }
        return "security/attendance";
    }

    // @PostMapping("/signin")
    // public String signIn(HttpSession session, Model model) {
    //     int id = (int) session.getAttribute("id");
    //     try {
    //         Staff staff = staffDAO.getstaffById(id);
    //         if (staff == null) {
    //             model.addAttribute("message", "Staff not found");
    //             return "security/attendance";
    //         }
    //         List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
    //         if (attendances.isEmpty() || attendances.stream().allMatch(a -> a.getSignInTime() == null)) {
    //             Attendance attendance = new Attendance();
    //             attendance.setId(staff.getId());
    //             attendance.setAttendanceDate(LocalDate.now());
    //             attendance.setSignInTime(LocalTime.now());
    //             attendance.setStatus("Pending");
    //             attendanceDAO.save(attendance);
    //             model.addAttribute("message", "Signed in successfully at " + attendance.getSignInTime());
    //         } else {
    //             model.addAttribute("message", "Already signed in for today");
    //         }
    //     } catch (SQLException e) {
    //         model.addAttribute("message", e.getMessage());
    //     }
    //     return "security/attendance";
    // }

    // @PostMapping("/signout")
    // public String signOut(HttpSession session, Model model) {
    //     int id = (int) session.getAttribute("id");
    //     try {
    //         Staff staff = staffDAO.getstaffById(id);
    //         if (staff == null) {
    //             model.addAttribute("message", "Staff not found");
    //             return "security/attendance";
    //         }
    //         List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
    //         if (attendances.isEmpty() || attendances.stream().noneMatch(a -> a.getSignInTime() != null && a.getSignOutTime() == null)) {
    //             model.addAttribute("message", "No sign-in record found for today or already signed out");
    //         } else {
    //             Attendance attendance = attendances.stream().filter(a -> a.getSignInTime() != null && a.getSignOutTime() == null).findFirst().orElse(null);
    //             if (attendance != null) {
    //                 attendance.setSignOutTime(LocalTime.now());
    //                 attendanceDAO.save(attendance);
    //                 model.addAttribute("message", "Signed out successfully at " + attendance.getSignOutTime());
    //             }
    //         }
    //     } catch (SQLException e) {
    //         model.addAttribute("message", e.getMessage());
    //     }
    //     return "security/attendance";
    // }
    @PostMapping("/signin")
        public String signIn(HttpSession session, Model model) {
            int id = (int) session.getAttribute("id");
            try {
                Staff staff = staffDAO.getstaffById(id);
                if (staff == null) {
                    model.addAttribute("message", "Staff not found");
                    updateModelWithAttendanceData(id, model);
                    return "security/attendance";
                }
                List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
                if (attendances.isEmpty() || attendances.stream().allMatch(a -> a.getSignInTime() == null)) {
                    Attendance attendance = new Attendance();
                    attendance.setId(staff.getId());
                    attendance.setAttendanceDate(LocalDate.now());
                    attendance.setSignInTime(LocalTime.now());
                    attendance.setStatus("Pending");
                    attendanceDAO.save(attendance);
                    model.addAttribute("message", "Signed in successfully at " + attendance.getSignInTime());
                } else {
                    model.addAttribute("message", "Already signed in for today");
                }
            } catch (SQLException e) {
                model.addAttribute("message", e.getMessage());
            }
            updateModelWithAttendanceData(id, model);
            return "security/attendance";
    }

    @PostMapping("/signout")
    public String signOut(HttpSession session, Model model) {
        int id = (int) session.getAttribute("id");
        try {
            Staff staff = staffDAO.getstaffById(id);
            if (staff == null) {
                model.addAttribute("message", "Staff not found");
                updateModelWithAttendanceData(id, model);
                return "security/attendance";
            }
            List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
            if (attendances.isEmpty() || attendances.stream().noneMatch(a -> a.getSignInTime() != null && a.getSignOutTime() == null)) {
                model.addAttribute("message", "No sign-in record found for today or already signed out");
            } else {
                Attendance attendance = attendances.stream().filter(a -> a.getSignInTime() != null && a.getSignOutTime() == null).findFirst().orElse(null);
                if (attendance != null) {
                    attendance.setSignOutTime(LocalTime.now());
                    attendanceDAO.save(attendance);
                    model.addAttribute("message", "Signed out successfully at " + attendance.getSignOutTime());
                }
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        updateModelWithAttendanceData(id, model);
        return "security/attendance";
    }

    private void updateModelWithAttendanceData(int staffId, Model model) {
        try {
            List<Attendance> attendances = attendanceDAO.findByStaffId(staffId);
            model.addAttribute("attendances", attendances);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving updated attendance records");
        }
    }

    @GetMapping("/all")
    public String getAllAttendances(Model model) {
        try {
            List<Attendance> attendances = attendanceDAO.findAll();
            model.addAttribute("attendances", attendances);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving attendance records");
        }
        return "attendanceList";
    }

    @PostMapping("/approve")
    public String approveAttendance(@RequestParam int attendanceId, Model model) {
        try {
            Attendance attendance = attendanceDAO.findById(attendanceId);
            if (attendance == null) {
                model.addAttribute("message", "Attendance record not found");
            } else {
                attendance.setStatus("Approved");
                attendanceDAO.save(attendance);
                model.addAttribute("message", "Attendance approved successfully");
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/attendance/all";
    }
}
