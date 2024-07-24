// package com.heroku.java.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpSession;

// import com.heroku.java.DAO.AssignDAO;
// import com.heroku.java.DAO.StaffDAO;
// import com.heroku.java.model.Assign;
// import com.heroku.java.model.Staff;

// import javax.sql.DataSource;
// import java.sql.SQLException;
// import java.util.List;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Random;
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;

// @Controller
// public class Assigncontroller {

//     private final AssignDAO assignDAO;
//     private final DataSource dataSource;
//     private final StaffDAO staffDAO; // Assuming you have a StaffDAO to get the staff list


//     @Autowired
//     public Assigncontroller(AssignDAO assignDAO, DataSource dataSource, StaffDAO staffDAO) {
//         this.assignDAO = assignDAO;
//         this.dataSource = dataSource;
//         this.staffDAO = staffDAO;
//     }

//     @GetMapping("/Addassign")
//     public String addAssign(HttpSession session, Model model, @RequestParam("scheduleid") int scheduleid,
//                             @RequestParam(value = "week", required = false, defaultValue = "defaultWeek") String week,
//                             @RequestParam(value = "month", required = false, defaultValue = "defaultMonth") String month) {
//         try {
//             List<Staff> stafflist = staffDAO.Liststaff(); // Assuming this method returns the list of staff
//             List<Assign> assignList = new ArrayList<>();
//             for (Staff staff : stafflist) {
//                 Assign assign = new Assign();
//                 assign.setId(staff.getId());
//                 assign.setName(staff.getName());
//                 assign.setScheduleid(scheduleid);
//                 assignList.add(assign);
//             }

//             // Auto-generate schedule using AssignDAO
//             assignList = assignDAO.autoSchedule(assignList);

//             model.addAttribute("assigns", assignList);
//             model.addAttribute("scheduleid", scheduleid);
//             model.addAttribute("week", week);
//             model.addAttribute("month", month);

//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }
//         return "admin/Addassign";
//     }

//     @PostMapping("/Addassign")
//     public String addAssign(@RequestParam("id") List<Integer> ids, @RequestParam("scheduleid") int scheduleid,
//                             @RequestParam("week") String week, @RequestParam("month") String month, HttpServletRequest request) {
//         System.out.println("Schedule id = " + scheduleid);
//         System.out.println("Received scheduleid: " + scheduleid);
//         try {
//             List<Assign> assignList = new ArrayList<>();
//             for (Integer id : ids) {
//                 Assign assign = new Assign();
//                 assign.setId(id);
//                 assign.setScheduleid(scheduleid);
//                 assign.setDt1(request.getParameter("dt1_" + id));
//                 assign.setDt2(request.getParameter("dt2_" + id));
//                 assign.setDt3(request.getParameter("dt3_" + id));
//                 assign.setDt4(request.getParameter("dt4_" + id));
//                 assign.setDt5(request.getParameter("dt5_" + id));
//                 assign.setDt6(request.getParameter("dt6_" + id));
//                 assign.setDt7(request.getParameter("dt7_" + id));
//                 assignList.add(assign);
//             }

//             // Auto-generate schedule using AssignDAO
//             assignList = assignDAO.autoSchedule(assignList);

//             // Save assignments to the database
//             for (Assign assign : assignList) {
//                 assignDAO.AddAssign(assign);
//             }

//             // Redirect to listassigns with the provided week and month
//             return "redirect:/listschedules";
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "admin/Addassign";
//         }
//     }
    
//     @GetMapping("/listassigns")
//     public String listassigns(HttpSession session, Model model,
//                               @RequestParam(name = "week", required = false, defaultValue = "defaultWeek") String week,
//                               @RequestParam(name = "month", required = false, defaultValue = "defaultMonth") String month,
//                               @RequestParam(name = "scheduleid", required = false, defaultValue = "0") int scheduleid) {
//         AssignDAO assignDAO = new AssignDAO(dataSource);
//         System.out.println("weeks: " + week);
//         System.out.println("months: " + month);
//         try {
//             List<Assign> assignlist = assignDAO.listassigns(week, month, scheduleid);
//             model.addAttribute("assigns", assignlist);
//             model.addAttribute("week", week);
//             model.addAttribute("month", month);
//             model.addAttribute("scheduleid", scheduleid);
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }

//         return "admin/listassigns";
//     }

//     @PostMapping("/Deleteassign")
//     public String Deleteschedule(@RequestParam("assignid") int assignId, @RequestParam("week") String week, @RequestParam("month") String month, @RequestParam("scheduleid") int scheduleid) {
//         try {
//             assignDAO.deleteassign(assignId);
//             return "redirect:/listassigns?week=" + week + "&month=" + month + "&scheduleid=" + scheduleid;
//         } catch (SQLException e) {
//             System.out.println("Error deleting assign: " + e.getMessage());
//             e.printStackTrace();
//             return "error";
//         }
//     }



//     //staff//

//     @GetMapping("/listassignstaff")
//     public String listassignstaff(HttpSession session, Model model,
//                               @RequestParam(name = "week", required = false, defaultValue = "defaultWeek") String week,
//                               @RequestParam(name = "month", required = false, defaultValue = "defaultMonth") String month,
//                               @RequestParam(name = "scheduleid", required = false, defaultValue = "0") int scheduleid) {
//         AssignDAO assignDAO = new AssignDAO(dataSource);
//         System.out.println("weeks: " + week);
//         System.out.println("months: " + month);
//         try {
//             List<Assign> assignlist = assignDAO.listassigns(week, month, scheduleid);
//             model.addAttribute("assigns", assignlist);
//             model.addAttribute("week", week);
//             model.addAttribute("month", month);
//             model.addAttribute("scheduleid", scheduleid);
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return "error";
//         }

//         return "security/listassignstaff";
//     }
// }


package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.heroku.java.DAO.AssignDAO;
import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.model.Assign;
import com.heroku.java.model.AssignListWrapper;
import com.heroku.java.model.Staff;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class Assigncontroller {

    private final AssignDAO assignDAO;
    private final DataSource dataSource;
    private final StaffDAO staffDAO; // Assuming you have a StaffDAO to get the staff list


    @Autowired
    public Assigncontroller(AssignDAO assignDAO, DataSource dataSource, StaffDAO staffDAO) {
        this.assignDAO = assignDAO;
        this.dataSource = dataSource;
        this.staffDAO = staffDAO;
    }

    @GetMapping("/Addassign")
    public String addAssign(HttpSession session, Model model, @RequestParam("scheduleid") int scheduleid,
                            @RequestParam(value = "week", required = false, defaultValue = "defaultWeek") String week,
                            @RequestParam(value = "month", required = false, defaultValue = "defaultMonth") String month) {
        try {
            List<Staff> stafflist = staffDAO.Liststaff(); // Assuming this method returns the list of staff
            List<Assign> assignList = new ArrayList<>();
            for (Staff staff : stafflist) {
                Assign assign = new Assign();
                assign.setId(staff.getId());
                assign.setName(staff.getName());
                assign.setScheduleid(scheduleid);
                assignList.add(assign);
            }

            // Auto-generate schedule using AssignDAO
            assignList = assignDAO.autoSchedule(assignList);

            model.addAttribute("assigns", assignList);
            model.addAttribute("scheduleid", scheduleid);
            model.addAttribute("week", week);
            model.addAttribute("month", month);

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "admin/Addassign";
    }

    @PostMapping("/Addassign")
    public String addAssign(@RequestParam("id") List<Integer> ids, @RequestParam("scheduleid") int scheduleid,
                            @RequestParam("week") String week, @RequestParam("month") String month, HttpServletRequest request) {
        System.out.println("Schedule id = " + scheduleid);
        System.out.println("Received scheduleid: " + scheduleid);
        try {
            List<Assign> assignList = new ArrayList<>();
            for (Integer id : ids) {
                Assign assign = new Assign();
                assign.setId(id);
                assign.setScheduleid(scheduleid);
                assign.setDt1(request.getParameter("dt1_" + id));
                assign.setDt2(request.getParameter("dt2_" + id));
                assign.setDt3(request.getParameter("dt3_" + id));
                assign.setDt4(request.getParameter("dt4_" + id));
                assign.setDt5(request.getParameter("dt5_" + id));
                assign.setDt6(request.getParameter("dt6_" + id));
                assign.setDt7(request.getParameter("dt7_" + id));
                assignList.add(assign);
            }

            // Auto-generate schedule using AssignDAO
            assignList = assignDAO.autoSchedule(assignList);

            // Save assignments to the database
            for (Assign assign : assignList) {
                assignDAO.AddAssign(assign);
            }

            // Redirect to listassigns with the provided week and month
            return "redirect:/listschedules";
        } catch (SQLException e) {
            e.printStackTrace();
            return "admin/Addassign";
        }
    }
    
    @GetMapping("/listassigns")
    public String listassigns(HttpSession session, Model model,
                              @RequestParam(name = "week", required = false, defaultValue = "defaultWeek") String week,
                              @RequestParam(name = "month", required = false, defaultValue = "defaultMonth") String month,
                              @RequestParam(name = "scheduleid", required = false, defaultValue = "0") int scheduleid) {
        AssignDAO assignDAO = new AssignDAO(dataSource);
        System.out.println("weeks: " + week);
        System.out.println("months: " + month);
        try {
            List<Assign> assignlist = assignDAO.listassigns(week, month, scheduleid);
            model.addAttribute("assigns", assignlist);
            model.addAttribute("week", week);
            model.addAttribute("month", month);
            model.addAttribute("scheduleid", scheduleid);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        return "admin/listassigns";
    }
    
    // @GetMapping("/Updateassign")
    // public String updateAssignForm(@RequestParam("assignid") int assignId, 
    //                             @RequestParam("week") String week, 
    //                             @RequestParam("month") String month, 
    //                             @RequestParam("scheduleid") int scheduleid, 
    //                             Model model) {
    //     try {
    //         Assign assign = assignDAO.getassignByAssignid(assignId);
    //         model.addAttribute("assign", assign);
    //         model.addAttribute("week", week);
    //         model.addAttribute("month", month);
    //         model.addAttribute("scheduleid", scheduleid);
    //         return "admin/Updateassign";
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return "error";
    //     }
    // }


// @PostMapping("/Updateassign")
// public String updateAssign(@RequestParam("assignid") int assignId,
//                            @RequestParam("dt1") String dt1,
//                            @RequestParam("dt2") String dt2,
//                            @RequestParam("dt3") String dt3,
//                            @RequestParam("dt4") String dt4,
//                            @RequestParam("dt5") String dt5,
//                            @RequestParam("dt6") String dt6,
//                            @RequestParam("dt7") String dt7,
//                            @RequestParam("week") String week, 
//                            @RequestParam("month") String month, 
//                            @RequestParam("scheduleid") int scheduleid) {
//     try {
//         Assign assign = new Assign();
//         assign.setAssignid(assignId);
//         assign.setDt1(dt1);
//         assign.setDt2(dt2);
//         assign.setDt3(dt3);
//         assign.setDt4(dt4);
//         assign.setDt5(dt5);
//         assign.setDt6(dt6);
//         assign.setDt7(dt7);

//         assignDAO.updateAssign(assign);

//         return "redirect:/listassigns?week=" + week + "&month=" + month + "&scheduleid=" + scheduleid;
//     } catch (SQLException e) {
//         e.printStackTrace();
//         return "error";
//     }
// }
@GetMapping("/Updateassign")
    public String getUpdateAssignPage(@RequestParam("week") String week,
                                      @RequestParam("month") String month,
                                      @RequestParam("scheduleid") int scheduleid,
                                      Model model) throws SQLException {
        List<Assign> assignList = assignDAO.listassigns(week, month, scheduleid);
        AssignListWrapper assignListWrapper = new AssignListWrapper();
        assignListWrapper.setAssigns(assignList);

        model.addAttribute("assignListWrapper", assignListWrapper);
        model.addAttribute("week", week);
        model.addAttribute("month", month);
        model.addAttribute("scheduleid", scheduleid);
        return "admin/Updateassign";
    }

    @PostMapping("/Updateassign")
    public String updateAssigns(@RequestParam("week") String week,
                                @RequestParam("month") String month,
                                @RequestParam("scheduleid") int scheduleid,
                                @ModelAttribute("assignListWrapper") AssignListWrapper assignListWrapper) {
        try {
            for (Assign assign : assignListWrapper.getAssigns()) {
                System.out.println("Updating assign with ID: " + assign.getAssignid());
                System.out.println("New values - dt1: " + assign.getDt1() + ", dt2: " + assign.getDt2() + ", dt3: " + assign.getDt3() + 
                                    ", dt4: " + assign.getDt4() + ", dt5: " + assign.getDt5() + ", dt6: " + assign.getDt6() + ", dt7: " + assign.getDt7());

                if (assign.getAssignid() != 0) {
                    assignDAO.updateAssign(assign);
                } else {
                    System.out.println("Skipping update for assign with ID 0.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/listassigns?week=" + week + "&month=" + month + "&scheduleid=" + scheduleid;
    }

    //staff//

    @GetMapping("/listassignstaff")
    public String listassignstaff(HttpSession session, Model model,
                              @RequestParam(name = "week", required = false, defaultValue = "defaultWeek") String week,
                              @RequestParam(name = "month", required = false, defaultValue = "defaultMonth") String month,
                              @RequestParam(name = "scheduleid", required = false, defaultValue = "0") int scheduleid) {
        AssignDAO assignDAO = new AssignDAO(dataSource);
        System.out.println("weeks: " + week);
        System.out.println("months: " + month);
        try {
            List<Assign> assignlist = assignDAO.listassigns(week, month, scheduleid);
            model.addAttribute("assigns", assignlist);
            model.addAttribute("week", week);
            model.addAttribute("month", month);
            model.addAttribute("scheduleid", scheduleid);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        return "security/listassignstaff";
    }
}