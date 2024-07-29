// package com.heroku.java.controller;

// import com.heroku.java.DAO.AttendanceDAO;
// import com.heroku.java.DAO.StaffDAO;
// import com.heroku.java.model.Attendance;
// import com.heroku.java.model.Staff;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import jakarta.servlet.http.HttpSession;
// import java.sql.SQLException;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.List;

// @Controller
// public class Attendancecontroller {

//     private final AttendanceDAO attendanceDAO;
//     private final StaffDAO staffDAO;

//     @Autowired
//     public Attendancecontroller(AttendanceDAO attendanceDAO, StaffDAO staffDAO) {
//         this.attendanceDAO = attendanceDAO;
//         this.staffDAO = staffDAO;
//     }

//     // Staff Part

//     @GetMapping("/attendance")
//     public String attendance(HttpSession session, Model model) {
//         Integer id = (Integer) session.getAttribute("id");
//         if (id == null) {
//             model.addAttribute("message", "Session expired. Please log in again.");
//             return "redirect:/login";
//         }
//         try {
//             List<Attendance> attendances = attendanceDAO.findByStaffId(id);
//             model.addAttribute("attendances", attendances);
//             model.addAttribute("staffId", id);
//         } catch (SQLException e) {
//             model.addAttribute("message", "Error retrieving attendance records");
//         }
//         return "security/attendance";
//     }

//     @PostMapping("/attendance/signin")
//     public String signIn(HttpSession session, Model model) {
//         Integer id = (Integer) session.getAttribute("id");
//         if (id == null) {
//             model.addAttribute("message", "Session expired. Please log in again.");
//             return "redirect:/login";
//         }
//         try {
//             Staff staff = staffDAO.getstaffById(id);
//             if (staff == null) {
//                 model.addAttribute("message", "Staff not found");
//                 updateModelWithAttendanceData(id, model);
//                 return "security/attendance";
//             }
//             List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
//             if (attendances.isEmpty() || attendances.stream().allMatch(a -> a.getSignInTime() == null)) {
//                 Attendance attendance = new Attendance();
//                 attendance.setId(staff.getId());
//                 attendance.setAttendanceDate(LocalDate.now());
//                 attendance.setSignInTime(LocalTime.now());
//                 attendance.setStatus("Pending");
//                 attendanceDAO.save(attendance);
//                 model.addAttribute("message", "Signed in successfully at " + attendance.getSignInTime());
//             } else {
//                 model.addAttribute("message", "Already signed in for today");
//             }
//         } catch (SQLException e) {
//             model.addAttribute("message", e.getMessage());
//         }
//         updateModelWithAttendanceData(id, model);
//         return "security/attendance";
//     }

//     @PostMapping("/attendance/signout")
//     public String signOut(HttpSession session, Model model) {
//         Integer id = (Integer) session.getAttribute("id");
//         if (id == null) {
//             model.addAttribute("message", "Session expired. Please log in again.");
//             return "redirect:/login";
//         }
//         try {   
//             Staff staff = staffDAO.getstaffById(id);
//             if (staff == null) {
//                 model.addAttribute("message", "Staff not found");
//                 updateModelWithAttendanceData(id, model);
//                 return "security/attendance";
//             }
//             LocalDate today = LocalDate.now();
//             List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
//             // if (attendances.isEmpty()
//             //         || attendances.stream().noneMatch(a -> a.getSignInTime() != null && a.getSignOutTime() == null)) {
//             //     model.addAttribute("message", "No sign-in record found for today or already signed out");
//             // } else {
//             //     Attendance attendance = attendances.stream()
//             //             .filter(a -> a.getSignInTime() != null && a.getSignOutTime() == null).findFirst().orElse(null);
//             //     if (attendance != null) {
//             //         attendance.setSignOutTime(LocalTime.now());
//             //         attendanceDAO.save(attendance);
//             //         model.addAttribute("message", "Signed out successfully at " + attendance.getSignOutTime());
//             //     }
//             // }
//              // Filter attendances to include only those valid for sign-out (today or previous day's overnight shift)
//         boolean foundValidSignIn = attendances.stream().anyMatch(a -> {
//             LocalDate attendanceDate = a.getAttendanceDate();
//             LocalTime signInTime = a.getSignInTime();
//             LocalTime signOutTime = a.getSignOutTime();

//             // Check if the staff signed in and has not signed out yet
//             boolean isSignedIn = signInTime != null && signOutTime == null;
//             // Check if the attendance date and time match an overnight shift criteria
//             // boolean isValidShift = attendanceDate.equals(today) || 
//             //                        (attendanceDate.equals(today.minusDays(1)) && signInTime.isAfter(LocalTime.of(20, 0)));
//             boolean isValidShift = attendanceDate.equals(today) || 
//                                    (attendanceDate.equals(today.minusDays(1)) || signInTime.isAfter(LocalTime.of(20, 0)) || LocalTime.now().isBefore(LocalTime.of(8, 0)));
//             System.out.println("isSignedIn: " + isSignedIn + ", isValidShift: " + isValidShift);
//             return isSignedIn && isValidShift;
//         });

//         if (!foundValidSignIn) {
//             model.addAttribute("message", "No sign-in record found for today or already signed out");
//         } else {
//             // Find the specific attendance record to update
//             Attendance attendance = attendances.stream().filter(a -> {
//                 LocalDate attendanceDate = a.getAttendanceDate();
//                 LocalTime signInTime = a.getSignInTime();
//                 LocalTime signOutTime = a.getSignOutTime();
                
//                 boolean isSignedIn = signInTime != null && signOutTime == null;
//                 boolean isValidShift = attendanceDate.equals(today) || 
//                                        (attendanceDate.equals(today.minusDays(1)) || signInTime.isAfter(LocalTime.of(20, 0)));
//                 System.out.println("isSignedIn: " + isSignedIn + ", isValidShift: " + isValidShift);
//                 return isSignedIn && isValidShift;
//             }).findFirst().orElse(null);

//             if (attendance != null) {
//                 attendance.setSignOutTime(LocalTime.now());
//                 attendanceDAO.save(attendance);
//                 model.addAttribute("message", "Signed out successfully at " + attendance.getSignOutTime());
//             }
//         }
//         } catch (SQLException e) {
//             model.addAttribute("message", e.getMessage());
//         }
//         updateModelWithAttendanceData(id, model);
//         return "security/attendance";
//     }

//     // Helper method to update model with attendance data (used in staff part)
//     private void updateModelWithAttendanceData(int staffId, Model model) {
//         try {
//             List<Attendance> attendances = attendanceDAO.findByStaffId(staffId);
//             model.addAttribute("attendances", attendances);
//         } catch (SQLException e) {
//             model.addAttribute("message", "Error retrieving updated attendance records");
//         }
//     }
// }
package com.heroku.java.controller;

import com.heroku.java.DAO.AttendanceDAO;
import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.model.Attendance;
import com.heroku.java.model.Staff;
import com.heroku.java.util.TimezoneUtils; // Import the TimezoneUtils class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Controller
public class Attendancecontroller {

    private final AttendanceDAO attendanceDAO;
    private final StaffDAO staffDAO;

    @Autowired
    public Attendancecontroller(AttendanceDAO attendanceDAO, StaffDAO staffDAO) {
        this.attendanceDAO = attendanceDAO;
        this.staffDAO = staffDAO;
    }

    @GetMapping("/attendance")
    public String attendance(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            model.addAttribute("message", "Session expired. Please log in again.");
            return "redirect:/login";
        }
        try {
            List<Attendance> attendances = attendanceDAO.findByStaffId(id);
            model.addAttribute("attendances", attendances);
            model.addAttribute("staffId", id);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving attendance records");
        }
        return "security/attendance";
    }

    @PostMapping("/attendance/signin")
    public String signIn(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            model.addAttribute("message", "Session expired. Please log in again.");
            return "redirect:/login";
        }
        try {
            Staff staff = staffDAO.getstaffById(id);
            if (staff == null) {
                model.addAttribute("message", "Staff not found");
                updateModelWithAttendanceData(id, model);
                return "security/attendance";
            }
            List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
            if (attendances.isEmpty() || attendances.stream().allMatch(a -> a.getSignInTime() == null)) {
                Attendance attendance = new Attendance();
                attendance.setId(staff.getId());
                attendance.setAttendanceDate(LocalDate.now());
                // Convert to desired timezone before setting sign-in time
                ZoneId utcZone = ZoneId.of("UTC");
                ZoneId localZone = ZoneId.of("Asia/Kuala_Lumpur");
                LocalTime signInTime = TimezoneUtils.convertToTimezone(LocalTime.now(utcZone), utcZone, localZone);
                attendance.setSignInTime(signInTime);
                attendance.setStatus("Pending");
                attendanceDAO.save(attendance);
                model.addAttribute("message", "Signed in successfully at " + attendance.getSignInTime());
            } else {
                model.addAttribute("message", "Already signed in for today");
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        updateModelWithAttendanceData(id, model);
        return "security/attendance";
    }

    @PostMapping("/attendance/signout")
    public String signOut(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            model.addAttribute("message", "Session expired. Please log in again.");
            return "redirect:/login";
        }
        try {   
            Staff staff = staffDAO.getstaffById(id);
            if (staff == null) {
                model.addAttribute("message", "Staff not found");
                updateModelWithAttendanceData(id, model);
                return "security/attendance";
            }
            LocalDate today = LocalDate.now();
            List<Attendance> attendances = attendanceDAO.findByStaffAndDate(id, LocalDate.now());
            boolean foundValidSignIn = attendances.stream().anyMatch(a -> {
                LocalDate attendanceDate = a.getAttendanceDate();
                LocalTime signInTime = a.getSignInTime();
                LocalTime signOutTime = a.getSignOutTime();

                boolean isSignedIn = signInTime != null && signOutTime == null;
                boolean isValidShift = attendanceDate.equals(today) || 
                                       (attendanceDate.equals(today.minusDays(1)) || signInTime.isAfter(LocalTime.of(20, 0)) || LocalTime.now().isBefore(LocalTime.of(8, 0)));
                return isSignedIn && isValidShift;
            });

            if (!foundValidSignIn) {
                model.addAttribute("message", "No sign-in record found for today or already signed out");
            } else {
                Attendance attendance = attendances.stream().filter(a -> {
                    LocalDate attendanceDate = a.getAttendanceDate();
                    LocalTime signInTime = a.getSignInTime();
                    LocalTime signOutTime = a.getSignOutTime();
                    
                    boolean isSignedIn = signInTime != null && signOutTime == null;
                    boolean isValidShift = attendanceDate.equals(today) || 
                                           (attendanceDate.equals(today.minusDays(1)) || signInTime.isAfter(LocalTime.of(20, 0)));
                    return isSignedIn && isValidShift;
                }).findFirst().orElse(null);

                if (attendance != null) {
                    // Convert to desired timezone before setting sign-out time
                    ZoneId utcZone = ZoneId.of("UTC");
                    ZoneId localZone = ZoneId.of("Asia/Kuala_Lumpur");
                    LocalTime signOutTime = TimezoneUtils.convertToTimezone(LocalTime.now(utcZone), utcZone, localZone);
                    attendance.setSignOutTime(signOutTime);
                    attendanceDAO.save(attendance);
                    model.addAttribute("message", "Signed out successfully at " + attendance.getSignOutTime());
                }
            }
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        updateModelWithAttendanceData(id, model);
        return "security/attendance";
    }

    // Helper method to update model with attendance data (used in staff part)
    private void updateModelWithAttendanceData(int staffId, Model model) {
        try {
            List<Attendance> attendances = attendanceDAO.findByStaffId(staffId);
            model.addAttribute("attendances", attendances);
        } catch (SQLException e) {
            model.addAttribute("message", "Error retrieving updated attendance records");
        }
    }
}
