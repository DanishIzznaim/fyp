package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.heroku.java.model.Staff;

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
    @GetMapping("/Homepageadmin")
    public String Homepageadmin() {
        return "admin/Homepageadmin";
    }

    @GetMapping("/Profileadmin")
    public String Profileadmin() {
        return "admin/Profileadmin";
    }

    @GetMapping("/listattendance")
    public String listattendance() {
        return "admin/listattendance";
    }

    @GetMapping("/listpayroll")
    public String listpayroll() {
        return "admin/listpayroll";
    }

    @GetMapping("/calculatepayroll")
    public String calculatepayroll() {
        return "admin/calculatepayroll";
    }

    @GetMapping("/test")
    public String test() {
        return "admin/test";
    }

    @GetMapping("/homepagesecurity")
    public String homepagesecurity() {
        return "security/homepagesecurity";
    }

    @GetMapping("/schedule")
    public String schedule() {
        return "security/schedule";
    }

    @GetMapping("/attendance")
    public String attendance() {
        return "security/attendance";
    }

    @GetMapping("/listattendancestaff")
    public String listattendancestaff() {
        return "security/listattendancestaff";
    }

    @GetMapping("/Updateattendance")
    public String Updateattendance() {
        return "admin/Updateattendance";
    }

    @GetMapping("/payslip")
    public String payslip() {
        return "security/payslip";
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
