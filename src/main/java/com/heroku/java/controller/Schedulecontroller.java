package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.heroku.java.DAO.ScheduleDAO;
import com.heroku.java.model.Schedule;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Controller
public class Schedulecontroller {

    private final ScheduleDAO scheduleDAO;
    private final DataSource dataSource;

    @Autowired
    public Schedulecontroller(ScheduleDAO scheduleDAO, DataSource dataSource) {
        this.scheduleDAO = scheduleDAO;
        this.dataSource = dataSource;
    }

    // Add schedule
    @GetMapping("/Addschedule")
    public String addSchedule(Model model) {
        model.addAttribute("schedule", new Schedule());
        return "admin/Addschedule";
    }

    @PostMapping("/Addschedule")
    public String addSchedule(@ModelAttribute("schedule") Schedule schedule) {
        try {
            scheduleDAO.AddSchedule(schedule);
            return "redirect:/listschedules";
        } catch (SQLException e) {
            e.printStackTrace();
            return "admin/Addschedule";
        }
    }

    // List schedules
    @GetMapping("/listschedules")
    public String listSchedules(Model model) {
        try {
            List<Schedule> scheduleList = scheduleDAO.listschedules();
            model.addAttribute("schedules", scheduleList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "admin/listschedules";
    }

    // Update schedule
    @GetMapping("/Updateschedule")
    public String updateSchedule(@RequestParam("scheduleid") int scheduleId, Model model) {
        try {
            Schedule schedule = scheduleDAO.getscheduleByScheduleid(scheduleId);
            model.addAttribute("schedule", schedule);
            return "admin/Updateschedule";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/Updateschedule")
    public String updateSchedule(@ModelAttribute("schedule") Schedule schedule) {
        try {
            scheduleDAO.updateschedule(schedule);
            return "redirect:/listschedules";
        } catch (SQLException e) {
            e.printStackTrace();
            return "admin/Updateschedule";
        }
    }

    @PostMapping("/Deleteschedule")
    public String deleteSchedule(@RequestParam("scheduleid") int scheduleId) {
        try {
            scheduleDAO.deleteSchedule(scheduleId);
            return "redirect:/listschedules";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }



    //FOR STAFF//

    @GetMapping("/schedule")
    public String listSchedulesStaff(Model model) {
        try {
            List<Schedule> scheduleList = scheduleDAO.listschedules();
            model.addAttribute("schedules", scheduleList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "security/schedule";
    }
}