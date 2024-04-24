package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.DAO.LoginDAO;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login") 
    public String login(HttpSession session) { 
            return "login"; 
    }
    @PostMapping("/login")
    public String login(HttpSession session, @RequestParam("username") String username,
                    @RequestParam("password") String password, Model model) {
    try {
        LoginDAO loginDAO = new LoginDAO(dataSource);
        
        boolean isSecurity = loginDAO.checkSecurity(username, password);
        boolean isAdmin = loginDAO.checkAdmin(username, password);
        
        if (isSecurity) {
            session.setAttribute("username", username);
            return "redirect:/Homepagesecurity"; // Replace with the appropriate security home page URL
        } else if (isAdmin) {
            
            session.setAttribute("username", username);
            return "redirect:/Homepageadmin";
        } else {
            System.out.println("Invalid username or password");
            model.addAttribute("error", true); 
            return "login"; 
        }
    } catch (SQLException e) {
        e.printStackTrace();
        model.addAttribute("error", true); 
        return "login";
    }
}

    @GetMapping("/Homepageadmin")
    public String Homepageadmin() {
        return "admin/Homepageadmin";
    }

    @GetMapping("/Homepagesecurity")
    public String Homepagesecurity() {
        return "admin/Homepagesecurity";
    }

    @GetMapping("/Profileadmin")
    public String Profileadmin() {
        return "admin/Profileadmin";
    }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }
}
