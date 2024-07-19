// package com.heroku.java.controller;

// import com.heroku.java.DAO.PayrollDAO;
// import com.heroku.java.model.Payroll;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.sql.SQLException;
// import java.util.List;

// @Controller
// public class PayrollAdmincontroller {

//     private final PayrollDAO payrollDAO;

//     @Autowired
//     public PayrollAdmincontroller(PayrollDAO payrollDAO) {
//         this.payrollDAO = payrollDAO;
//     }

//     @GetMapping("/listpayroll")
//     public String listPayrolls(Model model) throws SQLException {
//         List<Payroll> payrolls = payrollDAO.findAll();
//         model.addAttribute("payrolls", payrolls);
//         return "admin/listpayroll";
//     }

//     @GetMapping("/createpayroll")
//     public String showCreatePayrollForm(Model model) {
//         model.addAttribute("payroll", new Payroll());
//         return "admin/createpayroll";
//     }

//     @PostMapping("/createpayroll")
//     public String createPayroll(@ModelAttribute("payroll") Payroll payroll) throws SQLException {
//         payrollDAO.save(payroll);
//         return "redirect:/listpayroll";
//     }

//     @GetMapping("/admin/calculatepayroll")
//     public String calculatePayroll(Model model) throws SQLException {
//         List<Payroll> payrolls = payrollDAO.calculatePayrollForSecurityGuards();
//         for (Payroll payroll : payrolls) {
//             payrollDAO.save(payroll);
//         }
//         return "redirect:/listpayroll";
//     }
// }
