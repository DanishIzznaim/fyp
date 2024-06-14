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


import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.model.Staff;

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

public class Staffcontroller {

private final StaffDAO staffDAO;
private final DataSource dataSource;

@Autowired
    public Staffcontroller(StaffDAO staffDAO, DataSource dataSource) {
        this.staffDAO = staffDAO;
        this.dataSource = dataSource;
    }
    //Add staff//
    @GetMapping("/Addstaff")
    public String Addstaff(Model model) {
        model.addAttribute("staff", new Staff());
        return "admin/Addstaff";
    }

    @PostMapping("/Addstaff")
    public String Addstaff(@ModelAttribute("staff") Staff staff) {
        try {
            staffDAO.AddStaff(staff);
            // Redirect to a success page or another appropriate page
            return "redirect:/Liststaff";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
            return "admin/Addstaff";
        }
    }

    //list staff//
    @GetMapping("/Liststaff")
    public String Liststaff(HttpSession session, Staff staff,Model model) {
      StaffDAO staffDAO = new StaffDAO(dataSource);
      try {
          List<Staff> stafflist = staffDAO.Liststaff();
          model.addAttribute("staffs", stafflist);
      } catch (SQLException e) {
          e.printStackTrace();
          // Handle the exception or display an error message as per your requirement
          return "error";
      }

    return "admin/Liststaff";
    }

    //Update staff//
    @GetMapping("/Updatestaff")
    public String Updatestaff(@RequestParam("id") int staffId,Model model) {
      try {
        StaffDAO staffDAO = new StaffDAO(dataSource);
        Staff staff = staffDAO.getstaffById(staffId);
        model.addAttribute("staff", staff);
        return "admin/Updatestaff";
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("damn error bro");
        return "error";
    }
    }

    @PostMapping("/Updatestaff")
    public String Updatestaff(@ModelAttribute("Updatestaff") Staff staff) {
        try {
            StaffDAO staffDAO = new StaffDAO(dataSource);
            staffDAO.updatestaff(staff);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Damn error BRO!");
        }
        return "redirect:/Liststaff"; // Replace with the appropriate redirect URL after updating the staff details
    }

    //Delete the staff//
    @PostMapping("/Deletestaff")
    public String Deletestaff(@RequestParam("id") int staffId) {
        try {
            StaffDAO staffDAO = new StaffDAO(dataSource);
            staffDAO.deletestaff(staffId);
            return "redirect:/Liststaff";
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
            // Handle the exception or display an error message to the user
            // You can redirect to an error page or display a meaningful message
            return "error";
        }
    }

    //search staff by name//
    @GetMapping("/Searchstaff")
    public String searchstaff(@RequestParam(name = "searchValue", required = false) String searchValue, Model model) {
        try {
            // Perform the search based on the searchValue
            StaffDAO staffDAO = new StaffDAO(dataSource);
            List<Staff> searchResults = staffDAO.searchstaffByName(searchValue);
    
            // Add the search results and the searchValue to the model
            model.addAttribute("staffs", searchResults);
            model.addAttribute("searchValue", searchValue);
        } catch (SQLException e) {
            // Handle the SQLException, log it, or rethrow it as a RuntimeException if needed
            e.printStackTrace(); // You may want to log the exception instead
            // You can also redirect to an error page or handle it in a way that makes sense for your application
            model.addAttribute("error", "An error occurred during the search: " + e.getMessage());
        }
    // Return the view name to display the search results
    return "admin/Liststaff";
    }

//     @GetMapping("/Homepageadmin")
// public String homepageAdmin(Model model) {
//     try {
//         int totalStaffCount = staffDAO.getTotalStaffCount();
//         model.addAttribute("totalStaffCount", totalStaffCount);
//     } catch (SQLException e) {
//         e.printStackTrace();
//         // Handle the exception or display an error message as needed
//     }
//     return "admin/Homepageadmin";
// }
    
    
    
}


