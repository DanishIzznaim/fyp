

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
    public String Addassign(HttpSession session, Staff staff,Model model) {
        StaffDAO staffDAO = new StaffDAO(dataSource);
        try {
            List<Staff> stafflist = staffDAO.Liststaff();
            model.addAttribute("staffs", stafflist);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or display an error message as per your requirement
            return "error";
        }
        return "admin/Addassign";
    }

    @PostMapping("/Addassign")
    public String Addassign(@ModelAttribute("assign") Assign assign) {
        try {
            assignDAO.AddAssign(assign);
            // Redirect to a success page or another appropriate page
            return "redirect:/Listassign";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
            return "admin/Addassign";
        }
    }
}

