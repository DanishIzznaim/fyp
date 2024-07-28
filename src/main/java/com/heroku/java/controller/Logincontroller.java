package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.DAO.LoginDAO;
import com.heroku.java.model.Staff;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

// @SpringBootApplication
@Controller
public class Logincontroller {
    private LoginDAO loginDAO;

    @Autowired
    public Logincontroller(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("Staff", new Staff()); // Ensure Staff object is present
        return "login";
    }

    @PostMapping("/login")
    public String dashboard(HttpSession session, @ModelAttribute("Staffs") Staff users, RedirectAttributes redirectAttributes) {
        try {
            Staff authenticatedUser = loginDAO.authenticateUser(users.getUsername(), users.getPassword());

            if (authenticatedUser != null) {
                session.setAttribute("username", authenticatedUser.getUsername());
                session.setAttribute("role", authenticatedUser.getRole());
                session.setAttribute("id", authenticatedUser.getId());
                System.out.println("Staff ID who login: " + authenticatedUser.getId());

                if ("admin".equals(authenticatedUser.getRole())) {
                    return "redirect:/Homepageadmin";
                } else if ("security".equals(authenticatedUser.getRole())) {
                    return "redirect:/Homepagesecurity";
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred. Please try again.");
        }
        return "redirect:/login"; // Redirect to /login to show the error message
    }

    
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        // System.out.println(">>>>(" + session.getAttribute("id") + ") = " + session.getAttribute("username") + " logged out");
        
        // Invalidate the session
        session.invalidate();
        
        // Set headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        return "redirect:/login";
    }
}
