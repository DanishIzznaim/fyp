package com.heroku.java.controller;

import com.heroku.java.DAO.EmailService;
import com.heroku.java.DAO.PayrollDAO;
import com.heroku.java.DAO.StaffDAO;

import com.heroku.java.model.Payroll;
import com.heroku.java.model.Staff;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class Payrollcontroller {

    private final PayrollDAO payrollDAO;
    private final StaffDAO staffDAO;
    private final EmailService emailService;
   

    @Autowired
    public Payrollcontroller(PayrollDAO payrollDAO, StaffDAO staffDAO, EmailService emailService) {
        this.payrollDAO = payrollDAO;
        this.staffDAO = staffDAO;
        this.emailService = emailService;
    }

    @GetMapping("/Addpayroll")
    public String addPayrollForm(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
        return "redirect:/login"; // Redirect to login page if session id is null
    }
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

    // @PostMapping("/Addpayroll")
    // public String addPayroll(@RequestParam int staffId, @RequestParam String month, Model model) {
    //     try {
    //         // Parse the selected month
    //         YearMonth yearMonth = YearMonth.parse(month);
    //         LocalDate startDate = yearMonth.atDay(1);
    //         LocalDate endDate = yearMonth.atEndOfMonth();

    //         // Fetch sign-in and sign-out times
    //         List<LocalTime[]> timesList = payrollDAO.findSignInSignOutTimesByStaffAndMonth(staffId, startDate, endDate);
    //         int totalHours = 0;

    //         // Calculate total hours worked
    //         for (LocalTime[] times : timesList) {
    //             LocalTime signInTime = times[0];
    //             LocalTime signOutTime = times[1];
    //             if (signInTime != null && signOutTime != null) {
    //                 totalHours += Duration.between(signInTime, signOutTime).toHours();
    //             }
    //         }
    //         System.out.println("Total hours: " + totalHours);
    //         double totalPay = totalHours * 8.0; // RM8 per hour
    //         System.out.println("Total pay: " + totalPay);
    //         Payroll payroll = new Payroll();
    //         payroll.setSid(staffId);
    //         payroll.setMonth(month);
    //         payroll.setHoursWorked(totalHours);
    //         payroll.setHourlyRate(8.0);
    //         payroll.setTotalPay(totalPay);

    //         payrollDAO.save(payroll);
    //         model.addAttribute("message", "Payroll added successfully");
    //     } catch (SQLException e) {
    //         model.addAttribute("message", "Error adding payroll record");
    //         e.printStackTrace();
    //     }
    //     return "redirect:/listPayroll";
    // }

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
                    // Assuming signInTime and signOutTime are in the same day, otherwise, adjust the date accordingly
                    LocalDateTime signInDateTime = LocalDateTime.of(startDate, signInTime);
                    LocalDateTime signOutDateTime = LocalDateTime.of(startDate, signOutTime);

                    // Handle overnight shifts
                    if (signOutDateTime.isBefore(signInDateTime)) {
                        signOutDateTime = signOutDateTime.plusDays(1);
                    }

                    long hoursWorked = Duration.between(signInDateTime, signOutDateTime).toHours();
                    totalHours += hoursWorked;
                }
            }

            double totalPay = totalHours * 8.0; // RM8 per hour
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
        return "redirect:/listPayroll";
    }


    @GetMapping("/listPayroll")
    public String listPayroll(Model model, HttpSession session) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
        return "redirect:/login"; // Redirect to login page if session id is null
    }
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

        return "admin/listPayroll";
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
        return "redirect:/listPayroll";
    }

    @PostMapping("/updatePayroll")
    public String updatePayroll(HttpSession session, @RequestParam("payrollid") int payrollId, String month, Payroll payroll, Model model) {
        try {
            // Set payroll ID and other necessary fields
            payroll.setPayrollId(payrollId);

            // Update payroll in the database
            payrollDAO.updatePayroll(payrollId);

            // Fetch updated payroll and staff details
            Payroll updatedPayroll = payrollDAO.getPayrollByPayrollId(payrollId, month);
            Staff staff = staffDAO.getstaffById(updatedPayroll.getSid());


            // Prepare email content
            String subject = "New Payroll Updated: " + updatedPayroll.getMonth();
            String htmlContent = String.format(
                    "<h1>Payroll Confirmation</h1>" +
                    "<p>Dear %s,</p>" +
                    "<p>You have successfully got a Monthly Statement. Login to your account to download it:</p>" +
                    "<ul>" +
                    "<li>Year Month: %s</li>" +
                    "<li>Total Hours Worked: %d</li>" +
                    "<li>Total Pay:RM %.2f</li>" +
                    "</ul>" +
                    "<p>Thank you for your service!</p>" +
                    "<p>Sincerely,</p>" +
                    "<p>TPS Security</p>",
                    staff.getName(), updatedPayroll.getMonth(), updatedPayroll.getHoursWorked(), updatedPayroll.getTotalPay()
            );
            System.out.println("email volunteer: " + staff.getEmail());

            // Send email
            emailService.sendHtmlEmail(staff.getEmail(), subject, htmlContent);
            
            return "redirect:/listPayroll";
        } catch (SQLException e) {
            model.addAttribute("errorMessage", "An error occurred while updating the payroll.");
            return "redirect:/listPayroll?payrollid=" + payrollId;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/listpayroll";
        }

    }


    //staff//
    @GetMapping("/payroll")
    public String payroll(Model model, HttpSession session) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
        return "redirect:/login"; // Redirect to login page if session id is null
    }
        System.out.println("sid: " + id);
        List<Payroll> payrolls = new ArrayList<Payroll>();
        payrolls = payrollDAO.getPayrollsById(id);
        model.addAttribute("payrolls", payrolls);
        model.addAttribute("id", id);  
        return "security/payroll";
    }
    

    
    // @GetMapping("/viewpayslip")
    // public String viewpayslip(HttpSession session, @RequestParam int payrollId, @RequestParam String month, Model model) {
    //     Integer id = (Integer) session.getAttribute("id");
    //     if (id == null) {
    //     return "redirect:/login"; // Redirect to login page if session id is null
    // }
    //     Payroll payroll = payrollDAO.getPayrollByPayrollId(payrollId,month);
    //     model.addAttribute("payroll", payroll);
    //     return "security/viewpayslip";
    // }

    @GetMapping("/viewpayslip")
    public String viewpayslip(HttpSession session, @RequestParam int payrollId, @RequestParam String month, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            System.out.println("Session ID is null. Redirecting to login.");
            return "redirect:/login"; // Redirect to login page if session id is null
        }
        Payroll payroll = payrollDAO.getPayrollByPayrollId(payrollId, month);
        if (payroll == null) {
            System.out.println("No payroll found for payrollId: " + payrollId + " and month: " + month);
            return "redirect:/error"; // Redirect to error page if payroll is not found
        }
        model.addAttribute("payroll", payroll);
        return "security/viewpayslip";
    }


     @PostMapping("/export-to-pdf")
    public void generatePdf(HttpServletResponse response,
                            @RequestParam("payrollid") int payrollId,
                            @RequestParam("month") String month) throws DocumentException, IOException, SQLException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=payroll_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        List<Payroll> listofPayroll = payrollDAO.getPayrolls(payrollId, month);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        
        // Add header
        addHeader(document);
        
        // Add payslip details
        for (Payroll record : listofPayroll) {
            addPayslipDetails(document, record);
        }

        document.close();
    }

    private void addHeader(Document document) throws DocumentException {
        // Company header section
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Paragraph companyHeader = new Paragraph("TAMAN PELANGI SEMENYIH SECURITY", fontTitle);
        companyHeader.setAlignment(Element.ALIGN_CENTER);
        document.add(companyHeader);

        // Address
        Paragraph address = new Paragraph("Taman Pelangi Semenyih Fasa 6 & 7\nSemenyih, Selangor, 43500\nMalaysia", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);
        
        // Spacer
        document.add(Chunk.NEWLINE);
    }

    private void addPayslipDetails(Document document, Payroll record) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 5});
        
        // Adding employee details
        addTableHeader(table, "Employee Details");
        addEmployeeDetails(table, record);
        
        // Adding earnings and deductions
        addTableHeader(table, "Earnings & Deductions");
        addEarningsAndDeductions(table, record);

        document.add(table);

        // Adding net pay
        Paragraph netPay = new Paragraph("Net Pay: RM" + record.getTotalPay(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK));
        netPay.setAlignment(Element.ALIGN_RIGHT);
        document.add(netPay);
        
        // Spacer
        document.add(Chunk.NEWLINE);
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell cell = new PdfPCell(new Phrase(headerTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        cell.setColspan(2);
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addEmployeeDetails(PdfPTable table, Payroll record) {
        addCell(table, "Name", record.getSname());
        addCell(table, "IC", record.getSic());
        addCell(table, "Address", record.getSaddress());
        addCell(table, "Year/Month", record.getMonth());
        addCell(table, "Hours Worked", String.valueOf(record.getHoursWorked()));
        addCell(table, "Hourly Rate", String.valueOf(record.getHourlyRate()));
    }

    private void addEarningsAndDeductions(PdfPTable table, Payroll record) {
        addCell(table, "Earnings", String.valueOf(record.getTotalPay()));
        addCell(table, "Deductions", "None");
    }

    private void addCell(PdfPTable table, String header, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        cell.setPadding(5);
        table.addCell(cell);
    }
}
