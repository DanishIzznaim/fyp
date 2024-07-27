
// package com.heroku.java.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import jakarta.servlet.http.HttpSession;

// import com.heroku.java.DAO.StaffDAO;
// import com.heroku.java.model.Staff;

// import javax.sql.DataSource;
// import java.sql.SQLException;
// import java.util.List;

// @Controller
// public class Staffcontroller {

//     private final StaffDAO staffDAO;
//     private final DataSource dataSource;

//     @Autowired
//     public Staffcontroller(StaffDAO staffDAO, DataSource dataSource) {
//         this.staffDAO = staffDAO;
//         this.dataSource = dataSource;
//     }

//     private boolean isSessionValid(HttpSession session) {
//         String username = (String) session.getAttribute("username");
//         return username != null;
//     }

//     // Add staff
//     @GetMapping("/Addstaff")
//     public String addStaff(Model model, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         model.addAttribute("staff", new Staff());
//         return "admin/Addstaff";
//     }

//     @PostMapping("/Addstaff")
//     public String addStaff(@ModelAttribute("staff") Staff staff, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         try {
//             staffDAO.AddStaff(staff);
//             return "redirect:/Liststaff";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "admin/Addstaff";
//         }
//     }

//     // List staff
//     @GetMapping("/Liststaff")
//     public String listStaff(Model model, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         try {
//             List<Staff> staffList = staffDAO.Liststaff();
//             model.addAttribute("staffs", staffList);
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }
//         return "admin/Liststaff";
//     }

//     // Update staff
//     @GetMapping("/Updatestaff")
//     public String updateStaff(@RequestParam("id") int staffId, Model model, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         try {
//             Staff staff = staffDAO.getstaffById(staffId);
//             model.addAttribute("staff", staff);
//             return "admin/Updatestaff";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }
//     }

//     @PostMapping("/Updatestaff")
//     public String updateStaff(@ModelAttribute("Updatestaff") Staff staff, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         try {
//             staffDAO.updatestaff(staff);
//             return "redirect:/Liststaff";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "admin/Updatestaff";
//         }
//     }

//     // Delete staff
//     @PostMapping("/Deletestaff")
//     public String deleteStaff(@RequestParam("id") int staffId, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         try {
//             staffDAO.deletestaff(staffId);
//             return "redirect:/Liststaff";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }
//     }




//     @GetMapping("/homepagesecurity")
//     public String homepagesecurity(HttpSession session, Model model) {
//         Integer id = (Integer) session.getAttribute("id");
//         if (id != null) {
//             try {
//                 Staff staff = staffDAO.getstaffById(id);
//                 model.addAttribute("staff", staff);
//             } catch (SQLException e) {
//                 e.printStackTrace();
//                 return "error";
//             }
//             return "security/homepagesecurity";
//         } else {
//             return "redirect:/login";
//         }
//     }


//     // Update profile staff
//     @GetMapping("/profilestaff")
//     public String updateProfileStaff(@RequestParam("id") int staffId, Model model, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         try {
//             Staff staff = staffDAO.getstaffById(staffId);
//             model.addAttribute("staff", staff);
//             return "security/profilestaff";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }
//     }

//     @PostMapping("/profilestaff")
//     public String updateProfileStaff(@ModelAttribute Staff staff, HttpSession session) {
//         if (!isSessionValid(session)) {
//             return "redirect:/login";
//         }
//         try {
//             staffDAO.updateProfilestaff(staff);
//             session.setAttribute("staff", staff); // Update session attribute if necessary
//             return "redirect:/homepagesecurity";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }
//     }


// }

package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import com.heroku.java.DAO.EmailService;
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
private final EmailService emailService;

@Autowired
    public Staffcontroller(StaffDAO staffDAO, DataSource dataSource, EmailService emailService) {
        this.staffDAO = staffDAO;
        this.dataSource = dataSource;
        this.emailService = emailService;
    }
    private boolean isSessionValid(HttpSession session) {
                String username = (String) session.getAttribute("username");
                return username != null;
            }
    //Add staff//
    @GetMapping("/Addstaff")
    public String Addstaff(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
        return "redirect:/login"; // Redirect to login page if session id is null
    }
        model.addAttribute("staff", new Staff());
        return "admin/Addstaff";
    }


    @PostMapping("/Addstaff")
    public String Addstaff(@ModelAttribute("staff") Staff staff, Model model, HttpSession session) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            return "redirect:/login"; // Redirect to login page if session id is null
        }
        try {
            // Add the new staff to the database
            staffDAO.AddStaff(staff);

            // Prepare email content
            String subject = "Welcome to TPS Security";
            String htmlContent = String.format(
                "<h1>Welcome to TPS Security</h1>" +
                "<p>Dear %s,</p>" +
                "<p>You have been registered to TPS Security. Please log in to your account using the following credentials:</p>" +
                "<ul>" +
                "<li>Username: %s</li>" +
                "<li>Password: %s</li>" +
                "</ul>" +
                "<p>Thank you for joining us!</p>" +
                "<p>Sincerely,</p>" +
                "<p>TPS Security</p>",
                staff.getName(), staff.getUsername(), staff.getPassword()
            );
            System.out.println("Email sent to: " + staff.getEmail());

            // Send email
            emailService.sendHtmlEmail(staff.getEmail(), subject, htmlContent);

            // Redirect to the staff list page or another appropriate page
            return "redirect:/Liststaff";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
            model.addAttribute("errorMessage", "An error occurred while adding the staff.");
            return "admin/Addstaff";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/Liststaff";
        }
    }

    //list staff//
    @GetMapping("/Liststaff")
    public String Liststaff(HttpSession session, Staff staff,Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            return "redirect:/login"; // Redirect to login page if session id is null
        }
      try {
          StaffDAO staffDAO = new StaffDAO(dataSource);
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
    public String Updatestaff(HttpSession session, @RequestParam("id") int staffId,Model model) {
     Integer id = (Integer) session.getAttribute("id");
     if (id == null) {
            return "redirect:/login"; // Redirect to login page if session id is null
     }
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

    
    
    @GetMapping("/homepagesecurity")
    public String homepagesecurity(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id != null) {
            try {
                Staff staff = staffDAO.getstaffById(id);
                model.addAttribute("staff", staff);
            } catch (SQLException e) {
                e.printStackTrace();
                return "error";
            }
            return "security/homepagesecurity";
        } else {
            return "redirect:/login";
        }
    }



    // Update profile staff
    @GetMapping("/profilestaff")
    public String showUpdateForm(@RequestParam("id") int id, Model model) {
        try {
            Staff staff = staffDAO.getstaff(id);
            model.addAttribute("staff", staff);
            return "security/profilestaff";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "error";
        }
    }

     @PostMapping("/profilestaff")
    public String updateProfilestaff(@ModelAttribute Staff staff, Model model) {
        try {
            staffDAO.updateProfilestaff(staff);
            return "redirect:/profilestaff";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error updating staff profile");
            return "security/profilestaff";
        }
    }

    
    
    
}



