package com.heroku.java.controller;

import com.heroku.java.DAO.PayrollDAO;
import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.model.Payroll;
import com.heroku.java.model.Staff;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Payrollcontroller {

    private final PayrollDAO payrollDAO;
    private final StaffDAO staffDAO;

    @Autowired
    public Payrollcontroller(PayrollDAO payrollDAO, StaffDAO staffDAO) {
        this.payrollDAO = payrollDAO;
        this.staffDAO = staffDAO;
    }

    @GetMapping("/Addpayroll")
    public String addPayrollForm(Model model) {
        try {
            List<Staff> staffList = staffDAO.Liststaff();
            model.addAttribute("staffList", staffList);
            model.addAttribute("payroll", new Payroll());
            System.out.println("Add Payroll Form loaded successfully");
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving staff list");
            e.printStackTrace();
        }
        return "admin/Addpayroll";
    }

    @PostMapping("/Addpayroll")
    public String addPayroll(@RequestParam int staffId, @RequestParam String month, Model model) {
        try {
            // Parse the selected month
            YearMonth yearMonth = YearMonth.parse(month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            // Fetch sign-in and sign-out times
            List<LocalTime[]> timesList = payrollDAO.findSignInSignOutTimesByStaffAndMonth(staffId, startDate, endDate);
            int totalHours = 0;

            // Calculate total hours worked
            for (LocalTime[] times : timesList) {
                LocalTime signInTime = times[0];
                LocalTime signOutTime = times[1];
                if (signInTime != null && signOutTime != null) {
                    totalHours += Duration.between(signInTime, signOutTime).toHours();
                }
            }
            System.out.println("Total hours: " + totalHours);
            double totalPay = totalHours * 8.0; // RM8 per hour
            System.out.println("Total pay: " + totalPay);
            Payroll payroll = new Payroll();
            payroll.setSid(staffId);
            payroll.setMonth(month);
            payroll.setHoursWorked(totalHours);
            payroll.setHourlyRate(8.0);
            payroll.setTotalPay(totalPay);

            payrollDAO.save(payroll);
            model.addAttribute("message", "Payroll added successfully");
        } catch (SQLException e) {
            model.addAttribute("message", "Error adding payroll record");
            e.printStackTrace();
        }
        return "redirect:/listpayroll";
    }

    @GetMapping("/listpayroll")
    public String listPayroll(Model model, HttpSession session) {
        try {
            List<Payroll> payrolls = payrollDAO.findAll();
            model.addAttribute("payrolls", payrolls);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving payroll records");
            e.printStackTrace();
        }

        // Retrieve the message from the session and add it to the model
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            // session.removeAttribute("message"); 
            System.out.println("Added message" + message);
        }

        return "admin/listpayroll";
    }

    @GetMapping("/updatePayroll")
    public String updatePayroll(Model model, @RequestParam int payrollid, HttpSession session) {
        try {
            boolean isUpdated = payrollDAO.updatePayroll(payrollid);
            System.out.println("value: " + isUpdated);
            if (isUpdated) {
                session.setAttribute("message", "Payroll updated successfully.");
            } else {
                session.setAttribute("message", "Failed to update payroll. Please check the payroll ID.");
            }
        } catch (SQLException e) {
            session.setAttribute("message", "An error occurred while updating payroll.");
            e.printStackTrace();
        }
        return "redirect:/listpayroll";
    }

    //staff//
    @GetMapping("/payroll")
    public String payroll(Model model, @RequestParam int id) {
        List<Payroll> payrolls = new ArrayList<Payroll>();
        payrolls = payrollDAO.getPayrollsById(id);
        model.addAttribute("payrolls", payrolls);  
        return "security/payroll";
    }

    
    @GetMapping("/viewpayslip")
    public String viewpayslip(@RequestParam int payrollId, @RequestParam String month, Model model) {
        Payroll payroll = payrollDAO.getPayrollByPayrollId(payrollId,month);
        model.addAttribute("payroll", payroll);
        return "security/viewpayslip";
    }

}