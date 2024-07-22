// package com.heroku.java.service;

// import com.heroku.java.DAO.AssignDAO;
// import com.heroku.java.DAO.ScheduleDAO;
// import com.heroku.java.DAO.StaffDAO;
// import com.heroku.java.model.Assign;
// import com.heroku.java.model.Schedule;
// import com.heroku.java.model.Staff;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.sql.SQLException;
// import java.util.List;
// import java.util.Random;

// @Service
// public class ScheduleService {
//     @Autowired
//     private ScheduleDAO scheduleDAO;

//     @Autowired
//     private StaffDAO staffDAO;

//     @Autowired
//     private AssignDAO assignDAO;

//     public void autoAssignShifts(Schedule schedule) throws SQLException {
//         List<Staff> staffList = staffDAO.Liststaff(); // Corrected method name
//         Random random = new Random();

//         String[] shifts = {"8:00-20:00", "20:00-8:00", "OFF"}; // Define shift constants here

//         for (Staff staff : staffList) {
//             Assign assign = new Assign();
//             assign.setId(staff.getId());
//             assign.setScheduleid(schedule.getScheduleId()); // Ensure this method exists in Schedule class

//             String[] weekSchedule = new String[7];
//             boolean[] nightShiftAssigned = new boolean[7];
//             boolean offDayAssigned = false;

//             for (int day = 0; day < 7; day++) {
//                 if (day < 6 && !offDayAssigned) {
//                     weekSchedule[day] = shifts[random.nextInt(2)];
//                     if (weekSchedule[day].equals("20:00-8:00")) {
//                         nightShiftAssigned[day] = true;
//                     }
//                 } else if (!offDayAssigned) {
//                     weekSchedule[day] = "OFF";
//                     offDayAssigned = true;
//                 } else {
//                     if (nightShiftAssigned[day]) {
//                         weekSchedule[day] = shifts[random.nextInt(2)];
//                     } else {
//                         weekSchedule[day] = "20:00-8:00";
//                         nightShiftAssigned[day] = true;
//                     }
//                 }
//             }

//             assign.setDt1(weekSchedule[0]);
//             assign.setDt2(weekSchedule[1]);
//             assign.setDt3(weekSchedule[2]);
//             assign.setDt4(weekSchedule[3]);
//             assign.setDt5(weekSchedule[4]);
//             assign.setDt6(weekSchedule[5]);
//             assign.setDt7(weekSchedule[6]);

//             assignDAO.AddAssign(assign); // Corrected method name
//         }
//     }
// }
