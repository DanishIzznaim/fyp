// package com.heroku.java.controller;

// import com.heroku.java.DAO.PayrollDAO;
// import com.heroku.java.DAO.StaffDAO;
// import com.heroku.java.model.Payroll;
// import com.heroku.java.model.Staff;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import java.sql.SQLException;
// import java.util.List;

// @Controller
// public class PayrollStaffcontroller {

//     private final PayrollDAO payrollDAO;
//     private final StaffDAO staffDAO;

//     @Autowired
//     public PayrollStaffcontroller(PayrollDAO payrollDAO, StaffDAO staffDAO) {
//         this.payrollDAO = payrollDAO;
//         this.staffDAO = staffDAO;
//     }

//     @GetMapping("/payroll")
//     public String listPayrollForStaff(@RequestParam(required = false) Integer staffId, Model model) {
//         if (staffId == null) {
//             model.addAttribute("message", "Missing staffId parameter");
//             return "error";
//         }
//         try {
//             List<Payroll> payrolls = payrollDAO.findByStaffId(staffId);
//             model.addAttribute("payrolls", payrolls);
//         } catch (SQLException e) {
//             model.addAttribute("message", "Error retrieving payroll records for staff");
//             e.printStackTrace();
//         }
//         return "security/payroll";
//     }

//     @GetMapping("/viewpayslip")
//     public String viewPayslip(@RequestParam(required = false) Integer staffId, @RequestParam(required = false) String month, Model model) {
//         if (staffId == null || month == null) {
//             model.addAttribute("message", "Missing staffId or month parameter");
//             return "error";
//         }
//         try {
//             Payroll payroll = payrollDAO.findByStaffAndMonth(staffId, month);
//             Staff staff = staffDAO.getstaffById(staffId);
//             model.addAttribute("payroll", payroll);
//             model.addAttribute("staff", staff);
//         } catch (SQLException e) {
//             model.addAttribute("message", "Error retrieving payslip");
//             e.printStackTrace();
//         }
//         return "security/viewpayslip";
//     }
// }
