// package com.heroku.java.controller;

// import com.heroku.java.DAO.AssignDAO;
// import com.heroku.java.DAO.AttendanceDAO;
// import com.heroku.java.DAO.StaffDAO;
// import com.heroku.java.model.Assign;
// import com.heroku.java.model.Attendance;
// import com.heroku.java.model.Staff;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import jakarta.servlet.http.HttpSession;
// import java.sql.SQLException;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.List;

// import javax.sql.DataSource;

// @Controller

// public class Attendanceadmincontroller {

// private final AssignDAO assignDAO;
// private final StaffDAO staffDAO;
// private final DataSource dataSource;

// @Autowired
//     public Attendanceadmincontroller(AttendanceDAO attendanceDAO, StaffDAO staffDAO, DataSource dataSource) {
//         this.attendanceDAO = attendanceDAO;
//         this.staffDAO = staffDAO;
//         this.dataSource = dataSource;
//     }

//     @GetMapping("/listattendance")
//     public String listattendance(HttpSession session, Assign assign,Model model) {
//       AttendanceDAO attendanceDAO = new AttendanceDAO(dataSource);
//       try {
//           List<Attendance> attendancelist = attendanceDAO.listattendance();
//           model.addAttribute("attendances", attendancelist);
//       } catch (SQLException e) {
//           e.printStackTrace();
//           return "error";
//       }
//     return "admin/listattendance";
//     }
// }
package com.heroku.java.controller;

import com.heroku.java.DAO.AttendanceDAO;
import com.heroku.java.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller

public class AttendanceAdmincontroller {

    private final AttendanceDAO attendanceDAO;

    @Autowired
    public AttendanceAdmincontroller(AttendanceDAO attendanceDAO) {
        this.attendanceDAO = attendanceDAO;
    }

    // Display all attendance records for the admin
    @GetMapping("/listattendance")
    public String listattendance(Model model) {
        try {
            List<Attendance> attendances = attendanceDAO.findAllWithStaffName();
            model.addAttribute("attendances", attendances);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving attendance records");
            e.printStackTrace();
        }
        return "admin/listattendance";
    }

    //// Handle attendance approval by the admin
    @PostMapping("/approve")
    public String approveAttendance(@RequestParam int attendanceId, Model model) {
        try {
            Attendance attendance = attendanceDAO.findById(attendanceId);
            if (attendance == null) {
                model.addAttribute("message", "Attendance record not found");
            } else {
                attendance.setStatus("Present");  // Change status to "Present"
                attendanceDAO.save(attendance);
                model.addAttribute("message", "Attendance approved successfully");
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/listattendance";
    }

    // Handle attendance deletion by the admin
    @PostMapping("/delete/{attendanceId}")
    public String deleteAttendance(@PathVariable int attendanceId, Model model) {
        try {
            attendanceDAO.delete(attendanceId);
            model.addAttribute("message", "Attendance deleted successfully");
        } catch (SQLException e) {
            model.addAttribute("message", "Error deleting attendance record");
        }
        return "redirect:/listattendance";
    }
}

