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
import com.heroku.java.DAO.SecurityDAO;
import com.heroku.java.model.Security;

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

public class Securitycontroller {
    private final SecurityDAO securityDAO;

    private final DataSource dataSource;
@Autowired
    public Securitycontroller(SecurityDAO securityDAO, DataSource dataSource) {
        this.securityDAO = securityDAO;
        this.dataSource = dataSource;
    }
    //Add security//
    @GetMapping("/Addsecurity")
    public String Addsecurity(Model model) {
        model.addAttribute("security", new Security());
        return "admin/Addsecurity";
    }

    @PostMapping("/Addsecurity")
    public String AddSecurity(@ModelAttribute("security") Security security) {
        try {
            System.out.println("Sampai sini la sial");
            securityDAO.AddSecurity(security);
            // Redirect to a success page or another appropriate page
            return "redirect:/staffpage";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
            return "admin/Addsecurity";
        }
    }

    //list security//
    @GetMapping("/Listsecurity")
    public String Listsecurity(HttpSession session, Security security,Model model) {
      SecurityDAO securityDAO = new SecurityDAO(dataSource);
      try {
          List<Security> securitylist = securityDAO.ListSecurity();
          model.addAttribute("securitys", securitylist);
      } catch (SQLException e) {
          e.printStackTrace();
          // Handle the exception or display an error message as per your requirement
          return "error";
      }

    return "admin/Listsecurity";
    }

    @GetMapping("/Updatesecurity")
    public String Updatesecurity(@RequestParam("id") int securityId,Model model) {
      try {
        SecurityDAO securityDAO = new SecurityDAO(dataSource);
        Security security = securityDAO.getSecurityById(securityId);
        model.addAttribute("security", security);
        return "admin/Updatesecurity";
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("damn error bro");
        return "error";
    }
    }

    @PostMapping("/Updatesecurity")
    public String UpdateSecurity(@ModelAttribute("Updatesecurity") Security security) {
        try {
            SecurityDAO securityDAO = new SecurityDAO(dataSource);
            securityDAO.updateSecurity(security);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Damn error BRO!");
        }
        return "redirect:/Listsecurity"; // Replace with the appropriate redirect URL after updating the staff details
    }

    //Delete the security//
    @PostMapping("/Deletesecurity")
    public String DeleteSecurity(@RequestParam("id") int securityId) {
        try {
            SecurityDAO securityDAO = new SecurityDAO(dataSource);
            securityDAO.deleteSecurity(securityId);
            return "redirect:/Listsecurity";
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
            // Handle the exception or display an error message to the user
            // You can redirect to an error page or display a meaningful message
            return "error";
        }
    }

}
