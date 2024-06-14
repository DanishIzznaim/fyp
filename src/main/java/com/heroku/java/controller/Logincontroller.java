package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.DAO.LoginDAO;
import com.heroku.java.model.Staff;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;


@SpringBootApplication
@Controller
public class Logincontroller{
    private LoginDAO loginDAO;

    @Autowired
    public Logincontroller(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }


    @GetMapping("/login") 
    public String login(HttpSession session) { 
            return "login"; 
    }

    @PostMapping("/login")
    public String dashboard(HttpSession session, @ModelAttribute("Staffs") Staff users) {
        try {
            Staff authenticatedUser = loginDAO.authenticateUser(users.getUsername(), users.getPassword());

            if (authenticatedUser != null) {
                session.setAttribute("username", authenticatedUser.getUsername());
                session.setAttribute("role", authenticatedUser.getRole());
                session.setAttribute("id", authenticatedUser.getId());
                System.out.println("user id is " + authenticatedUser.getId());

                if ("admin".equals(authenticatedUser.getRole())) {
                    return "redirect:/Homepageadmin";
                } else if ("security".equals(authenticatedUser.getRole())) {
                    return "redirect:/homepagesecurity";
                }
            }
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
        }
        return "/login";
    }
}
