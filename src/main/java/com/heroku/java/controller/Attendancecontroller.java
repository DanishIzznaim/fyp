// 




package com.heroku.java.controller;

import com.heroku.java.DAO.AttendanceDAO;
import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.model.Attendance;
import com.heroku.java.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpSession;

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
        model.addAttribute("staffId", id);
        return "security/attendance";
    }

    @PostMapping("/signin")
    public String signIn(HttpSession session, Model model) {
        int id = (int) session.getAttribute("id");
        try {
            Staff staff = staffDAO.getstaffById(id);
            if (staff == null) {
                model.addAttribute("message", "Staff not found");
                return "security/attendance";
            }
            List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
            if (attendances.isEmpty()) {
                Attendance attendance = new Attendance();
                attendance.setId(staff.getId());
                attendance.setAttendanceDate(LocalDateTime.now());
                attendance.setSignInTime(LocalDateTime.now());
                attendance.setStatus("Pending");
                attendanceDAO.save(attendance);
                model.addAttribute("message", "Signed in successfully at " + attendance.getSignInTime());
            } else {
                model.addAttribute("message", "Already signed in for today");
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "security/attendance";
    }

    @PostMapping("/signout")
    public String signOut(HttpSession session, Model model) {
        int id = (int) session.getAttribute("id");
        try {
            Staff staff = staffDAO.getstaffById(id);
            if (staff == null) {
                model.addAttribute("message", "Staff not found");
                return "security/attendance";
            }
            List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
            if (attendances.isEmpty()) {
                model.addAttribute("message", "No sign-in record found for today");
            } else {
                Attendance attendance = attendances.get(0);
                attendance.setSignOutTime(LocalDateTime.now());
                attendanceDAO.save(attendance);
                model.addAttribute("message", "Signed out successfully at " + attendance.getSignOutTime());
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "security/attendance";
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


// Admin controller//

// package com.heroku.java.controller;

// import com.heroku.java.DAO.AttendanceDAO;
// import com.heroku.java.model.Attendance;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.sql.SQLException;
// import java.util.List;

// @Controller
// @RequestMapping("/admin")
// public class AdminController {

//     private final AttendanceDAO attendanceDAO;

//     @Autowired
//     public AdminController(AttendanceDAO attendanceDAO) {
//         this.attendanceDAO = attendanceDAO;
//     }

//     @GetMapping("/home")
//     public String adminHome(Model model) {
//         try {
//             List<Attendance> attendances = attendanceDAO.findAll();
//             model.addAttribute("attendances", attendances);
//         } catch (SQLException e) {
//             model.addAttribute("message", "Error retrieving attendance records");
//         }
//         return "admin/home";
//     }

//     @PostMapping("/approve")
//     public String approveAttendance(@RequestParam int attendanceId, Model model) {
//         try {
//             Attendance attendance = attendanceDAO.findById(attendanceId);
//             if (attendance == null) {
//                 model.addAttribute("message", "Attendance record not found");
//             } else {
//                 attendance.setStatus("Approved");
//                 attendanceDAO.save(attendance);
//                 model.addAttribute("message", "Attendance approved successfully");
//             }
//         } catch (SQLException e) {
//             model.addAttribute("message", e.getMessage());
//         }
//         return "redirect:/admin/home";
//     }
// }
