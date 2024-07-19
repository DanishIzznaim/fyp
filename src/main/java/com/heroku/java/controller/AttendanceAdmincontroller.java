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

    @GetMapping("/listattendance")
    public String listAttendance(Model model) {
        try {
            List<Attendance> attendances = attendanceDAO.findAllWithStaffName();
            model.addAttribute("attendances", attendances);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving attendance records");
            e.printStackTrace();
        }
        return "admin/listattendance";
    }

    @PostMapping("/approve")
    public String approveAttendance(@RequestParam int attendanceId, Model model) {
        try {
            Attendance attendance = attendanceDAO.findById(attendanceId);
            if (attendance == null) {
                model.addAttribute("message", "Attendance record not found");
            } else {
                attendance.setStatus("Present");
                attendanceDAO.save(attendance);
                model.addAttribute("message", "Attendance approved successfully");
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/listattendance";
    }

    @PostMapping("/delete")
    public String deleteAttendance(@RequestParam int attendanceId, Model model) {
        try {
            attendanceDAO.deleteattendance(attendanceId);
            model.addAttribute("message", "Attendance deleted successfully");
        } catch (SQLException e) {
            model.addAttribute("message", "Error deleting attendance record");
        }
        return "redirect:/listattendance";
    }
}
