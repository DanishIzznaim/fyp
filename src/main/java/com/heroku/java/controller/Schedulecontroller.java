// package com.heroku.java.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;

// //import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;

// import jakarta.servlet.http.HttpSession;


// import com.heroku.java.DAO.ScheduleDAO;
// import com.heroku.java.DAO.StaffDAO;
// import com.heroku.java.model.Schedule;
// import com.heroku.java.model.Staff;

// import javax.sql.DataSource;
// import java.sql.Connection;
// import java.sql.Date;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.Map;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Base64;


// @Controller

// public class Schedulecontroller {

// private final ScheduleDAO scheduleDAO;
// private final DataSource dataSource;

// @Autowired
//     public Schedulecontroller(ScheduleDAO scheduleDAO, DataSource dataSource) {
//         this.scheduleDAO = scheduleDAO;
//         this.dataSource = dataSource;
//     }
//     //Add schedule//
//     @GetMapping("/Addschedule")
//     public String Addstaff(Model model) {
//         model.addAttribute("schedule", new Schedule());
//         return "admin/Addschedule";
//     }

//     @PostMapping("/Addschedule")
//     public String Addschedule(@ModelAttribute("schedule") Schedule schedule) {
//         try {
//             scheduleDAO.AddSchedule(schedule);
//             // Redirect to a success page or another appropriate page
//             return "redirect:/listschedules";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             // Handle database exception
//             return "admin/Addschedule";
//         }
//     }

//     //list schedule//
//     @GetMapping("/listschedules")
//     public String listschedules(HttpSession session, Schedule schedule,Model model) {
//       ScheduleDAO scheduleDAO = new ScheduleDAO(dataSource);
//       try {
//           List<Schedule> schedulelist = scheduleDAO.listschedules();
//           model.addAttribute("schedules", schedulelist);
//       } catch (SQLException e) {
//           e.printStackTrace();
//           // Handle the exception or display an error message as per your requirement
//           return "error";
//       }

//     return "admin/listschedules";
//     }
    
//     //Update schedule//
//     @GetMapping("/Updateschedule")
//     public String Updateschedule(@RequestParam("scheduleid") int scheduleId,Model model) {
//       try {
//         ScheduleDAO scheduleDAO = new ScheduleDAO(dataSource);
//         Schedule schedule = scheduleDAO.getscheduleByScheduleid(scheduleId);
//         model.addAttribute("schedule", schedule);
//         return "admin/Updateschedule";
//       } catch (SQLException e) {
//         e.printStackTrace();
//         System.out.println("damn error bro");
//         return "error";
//     }
//     }

//     @PostMapping("/Updateschedule")
//     public String Updateschedule(@ModelAttribute("Updateschedule") Schedule schedule) {
//         try {
//             ScheduleDAO scheduleDAO = new ScheduleDAO(dataSource);
//             scheduleDAO.updateschedule(schedule);
//         } catch (SQLException e) {
//             e.printStackTrace();
//             System.out.println("Damn error BRO!");
//         }
//         return "redirect:/listschedules"; // Replace with the appropriate redirect URL after updating the schedule details
//     }

//      //Delete the schedule//
//      @PostMapping("/Deleteschedule")
//     public String Deleteschedule(@RequestParam("scheduleid") int scheduleId) {
//         try {
//             scheduleDAO.deleteschedule(scheduleId);
//             return "redirect:/listschedules";
//         } catch (SQLException e) {
//             System.out.println("Error deleting schedule: " + e.getMessage());
//             e.printStackTrace();
//             return "error";
//         }
//     }
// }
// // package com.heroku.java.controller;

// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.stereotype.Controller;
// // import org.springframework.ui.Model;
// // import org.springframework.web.bind.annotation.GetMapping;
// // import org.springframework.web.bind.annotation.ModelAttribute;
// // import org.springframework.web.bind.annotation.PostMapping;
// // import org.springframework.web.bind.annotation.RequestParam;

// // import com.heroku.java.DAO.ScheduleDAO;
// // import com.heroku.java.model.Schedule;

// // import jakarta.servlet.http.HttpSession;

// // import java.sql.SQLException;
// // import java.util.List;

// // @Controller
// // public class Schedulecontroller {

// //     private final ScheduleDAO scheduleDAO;

// //     @Autowired
// //     public Schedulecontroller(ScheduleDAO scheduleDAO) {
// //         this.scheduleDAO = scheduleDAO;
// //     }

// //     // Add schedule
// //     @GetMapping("/Addschedule")
// //     public String Addschedule(Model model) {
// //         model.addAttribute("schedule", new Schedule());
// //         return "admin/Addschedule";
// //     }

// //     @PostMapping("/Addschedule")
// //     public String Addschedule(@ModelAttribute("schedule") Schedule schedule) {
// //         try {
// //             scheduleDAO.AddSchedule(schedule); // Correct method name
// //             return "redirect:/listschedules";
// //         } catch (SQLException e) {
// //             e.printStackTrace();
// //             return "admin/Addschedule";
// //         }
// //     }

// //     // List schedules
// //     @GetMapping("/listschedules")
// //     public String listSchedules(Model model) {
// //         try {
// //             List<Schedule> scheduleList = scheduleDAO.listSchedules();
// //             model.addAttribute("schedules", scheduleList);
// //         } catch (SQLException e) {
// //             e.printStackTrace();
// //             return "error";
// //         }
// //         return "admin/listschedules";
// //     }

// //     // Update schedule
// //     @GetMapping("/Updateschedule")
// //     public String Updateschedule(@RequestParam("scheduleid") int scheduleId, Model model) {
// //         try {
// //             Schedule schedule = scheduleDAO.getScheduleByScheduleid(scheduleId);
// //             model.addAttribute("schedule", schedule);
// //             return "admin/Updateschedule";
// //         } catch (SQLException e) {
// //             e.printStackTrace();
// //             return "error";
// //         }
// //     }

// //     @PostMapping("/Updateschedule")
// //     public String Updateschedule(@ModelAttribute("schedule") Schedule schedule) {
// //         try {
// //             scheduleDAO.updateSchedule(schedule); // Correct method name
// //             return "redirect:/listschedules";
// //         } catch (SQLException e) {
// //             e.printStackTrace();
// //             return "admin/Updateschedule";
// //         }
// //     }

// //     // Delete schedule
// //     @PostMapping("/Deleteschedule")
// //     public String Deleteschedule(@RequestParam("scheduleid") int scheduleId) {
// //         try {
// //             scheduleDAO.deleteSchedule(scheduleId); // Correct method name
// //             return "redirect:/listschedules";
// //         } catch (SQLException e) {
// //             e.printStackTrace();
// //             return "error";
// //         }
// //     }
// // }
package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.heroku.java.DAO.ScheduleDAO;
import com.heroku.java.model.Schedule;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Controller
public class Schedulecontroller {

    private final ScheduleDAO scheduleDAO;
    private final DataSource dataSource;

    @Autowired
    public Schedulecontroller(ScheduleDAO scheduleDAO, DataSource dataSource) {
        this.scheduleDAO = scheduleDAO;
        this.dataSource = dataSource;
    }

    // Add schedule
    @GetMapping("/Addschedule")
    public String addSchedule(Model model) {
        model.addAttribute("schedule", new Schedule());
        return "admin/Addschedule";
    }

    @PostMapping("/Addschedule")
    public String addSchedule(@ModelAttribute("schedule") Schedule schedule) {
        try {
            scheduleDAO.AddSchedule(schedule);
            return "redirect:/listschedules";
        } catch (SQLException e) {
            e.printStackTrace();
            return "admin/Addschedule";
        }
    }

    // List schedules
    @GetMapping("/listschedules")
    public String listSchedules(Model model) {
        try {
            List<Schedule> scheduleList = scheduleDAO.listschedules();
            model.addAttribute("schedules", scheduleList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "admin/listschedules";
    }

    // Update schedule
    @GetMapping("/Updateschedule")
    public String updateSchedule(@RequestParam("scheduleid") int scheduleId, Model model) {
        try {
            Schedule schedule = scheduleDAO.getscheduleByScheduleid(scheduleId);
            model.addAttribute("schedule", schedule);
            return "admin/Updateschedule";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/Updateschedule")
    public String updateSchedule(@ModelAttribute("schedule") Schedule schedule) {
        try {
            scheduleDAO.updateschedule(schedule);
            return "redirect:/listschedules";
        } catch (SQLException e) {
            e.printStackTrace();
            return "admin/Updateschedule";
        }
    }

    // Delete schedule
    @PostMapping("/Deleteschedule")
    public String deleteSchedule(@RequestParam("scheduleid") int scheduleId) {
        try {
            scheduleDAO.deleteschedule(scheduleId);
            return "redirect:/listschedules";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
