

package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;


import com.heroku.java.DAO.ScheduleDAO;
import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.DAO.AssignDAO;
import com.heroku.java.model.Schedule;
import com.heroku.java.model.Staff;
import com.heroku.java.model.Assign;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;  
import java.util.Map;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;


@Controller

public class Assigncontroller {

private final AssignDAO assignDAO;
private final DataSource dataSource;

@Autowired
    public Assigncontroller(AssignDAO assignDAO, DataSource dataSource) {
        this.assignDAO = assignDAO;
        this.dataSource = dataSource;
    }
    //Add schedule//
    @GetMapping("/Addassign")
    public String Addassign(HttpSession session, Model model, @RequestParam("scheduleid") int scheduleid) {
        StaffDAO staffDAO = new StaffDAO(dataSource);
        try {
            List<Staff> stafflist = staffDAO.Liststaff();
            // List<Assign> assignList = assignDAO.listassign();
            // List<Staff> results = new ArrayList<>();

            // for (Staff staff : stafflist) {
            //     for (Assign assign : assignList) {
            //         if (assign.getStaff()== null || assign.getStaff().getId() == staff.getId() ) {
            //             Assign newAssign = new Assign();
            //             newAssign.setId(assign.getId());
            //             newAssign.setStaff(staff);
            //             results.add(newAssign);
            //         }
            //     }
            // }
            model.addAttribute("assigns", stafflist);
            // model.addAttribute("assigns", results);
            model.addAttribute("scheduleid", scheduleid);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or display an error message as per your requirement
            return "error";
        }
        return "admin/Addassign";
    }

    @PostMapping("/Addassign")
    public String addAssign(@ModelAttribute("assigns") Assign assigns, @RequestParam("scheduleid") int scheduleid) {
        AssignDAO assignDAO = new AssignDAO(dataSource);
        Assign assign = new Assign();
        assign.setId(assigns.getId());
        assign.setScheduleid(scheduleid);
        assign.setDt1(assigns.getDt1());
        assign.setDt2(assigns.getDt2());
        assign.setDt3(assigns.getDt3());
        assign.setDt4(assigns.getDt4());
        assign.setDt5(assigns.getDt5());
        assign.setDt6(assigns.getDt6());
        assign.setDt7(assigns.getDt7());
        try {
            assignDAO.AddAssign(assign);
            //Redirect to a success page or another appropriate page
            return "redirect:/listassigns";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
            return "admin/Addassign";
        }
        // return "admin/Addassign";
    }

    @GetMapping("/listassigns")
    public String listassigns(HttpSession session, Assign assign,Model model) {
      AssignDAO assignDAO = new AssignDAO(dataSource);
      try {
          List<Assign> assignlist = assignDAO.listassigns();
          model.addAttribute("assigns", assignlist);
      } catch (SQLException e) {
          e.printStackTrace();
          return "error";
      }

    return "admin/listassigns";
    }

    //Update schedule//
    // @GetMapping("/Updateassign")
    // public String Updateassign(@RequestParam("scheduleid") int scheduleId,Model model) {
    //   try {
    //     ScheduleDAO scheduleDAO = new ScheduleDAO(dataSource);
    //     Schedule schedule = scheduleDAO.getscheduleByScheduleid(scheduleId);
    //     model.addAttribute("schedule", schedule);
    //     return "admin/Updateschedule";
    //   } catch (SQLException e) {
    //     e.printStackTrace();
    //     System.out.println("damn error bro");
    //     return "error";
    // }
    // }

    // @PostMapping("/Updateschedule")
    // public String Updateschedule(@ModelAttribute("Updateschedule") Schedule schedule) {
    //     try {
    //         ScheduleDAO scheduleDAO = new ScheduleDAO(dataSource);
    //         scheduleDAO.updateschedule(schedule);
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Damn error BRO!");
    //     }
    //     return "redirect:/listschedules"; // Replace with the appropriate redirect URL after updating the schedule details
    // }

    //  //Delete the assign//
     @PostMapping("/Deleteassign")
    public String Deleteschedule(@RequestParam("assignid") int assignId) {
        try {
            assignDAO.deleteassign(assignId);
            return "redirect:/listassigns";
        } catch (SQLException e) {
            System.out.println("Error deleting assign: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
}

