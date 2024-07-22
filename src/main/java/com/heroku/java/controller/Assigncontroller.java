

package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.heroku.java.DAO.AssignDAO;
import com.heroku.java.DAO.StaffDAO;
import com.heroku.java.model.Assign;
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

    @Autowired
    public Assigncontroller(AssignDAO assignDAO, DataSource dataSource) {
        this.assignDAO = assignDAO;
        this.dataSource = dataSource;
    }

    @GetMapping("/Addassign")
    public String Addassign(HttpSession session, Model model, @RequestParam("scheduleid") int scheduleid,
                            @RequestParam(value = "week", required = false, defaultValue = "defaultWeek") String week,
                            @RequestParam(value = "month", required = false, defaultValue = "defaultMonth") String month) {
        StaffDAO staffDAO = new StaffDAO(dataSource);
        try {
            List<Staff> stafflist = staffDAO.Liststaff();
            List<Assign> assignList = new ArrayList<>();
            for (Staff staff : stafflist) {
                Assign assign = new Assign();
                assign.setId(staff.getId());
                assign.setName(staff.getName());
                assign.setScheduleid(scheduleid);
                assignList.add(assign);
            }

            assignList = autoSchedule(assignList); // Auto schedule the assigns

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
        AssignDAO assignDAO = new AssignDAO(dataSource);
        System.out.println("Schedule id = " + scheduleid);
        System.out.println("Received scheduleid: " + scheduleid); // Add this line
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
    
            // Auto-generate schedule
            assignList = autoSchedule(assignList);
    
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
    private static final int MAX_NIGHT_SHIFTS = 2;
    private static final int MAX_DAY_SHIFTS = 1;

    public List<Assign> autoSchedule(List<Assign> assignList) {
        int totalStaff = assignList.size();
        int daysInWeek = 7;
        Random random = new Random();

        // Create a matrix to hold the shifts
        String[][] shiftMatrix = new String[totalStaff][daysInWeek + 1];

        // Assign one off day per staff member
        IntStream.range(0, totalStaff).forEach(i -> {
            int offDay = random.nextInt(daysInWeek) + 1;
            IntStream.range(1, daysInWeek + 1).forEach(day -> {
                shiftMatrix[i][day] = (day == offDay) ? Assign.OFF_DAY : "";
            });
        });

        // Process each day sequentially to reduce load
        for (int day = 1; day <= daysInWeek; day++) {
            assignShiftsForDay(assignList, shiftMatrix, day, random);
        }

        // Assign the generated shift matrix back to the assignList
        IntStream.range(0, totalStaff).forEach(i -> {
            IntStream.range(1, daysInWeek + 1).forEach(day -> {
                assignList.get(i).setShift(day, shiftMatrix[i][day]);
            });
        });

        return assignList;
    }

    private void assignShiftsForDay(List<Assign> assignList, String[][] shiftMatrix, int currentDay, Random random) {
        int totalStaff = assignList.size();
        List<Integer> indices = IntStream.range(0, totalStaff).boxed().collect(Collectors.toList());
        Collections.shuffle(indices, random);

        int nightShiftCount = 0;
        int dayShiftCount = 0;

        // First pass: Assign shifts to available staff
        for (int idx : indices) {
            Assign assign = assignList.get(idx);
            if (shiftMatrix[idx][currentDay].equals(Assign.OFF_DAY)) continue;

            if (nightShiftCount < MAX_NIGHT_SHIFTS && canAssignShift(shiftMatrix, idx, currentDay, Assign.NIGHT_SHIFT)) {
                shiftMatrix[idx][currentDay] = Assign.NIGHT_SHIFT;
                nightShiftCount++;
            } else if (dayShiftCount < MAX_DAY_SHIFTS && canAssignShift(shiftMatrix, idx, currentDay, Assign.DAY_SHIFT)) {
                shiftMatrix[idx][currentDay] = Assign.DAY_SHIFT;
                dayShiftCount++;
            }

            if (nightShiftCount == MAX_NIGHT_SHIFTS && dayShiftCount == MAX_DAY_SHIFTS) break;
        }

        // Ensure at least 1 staff in the day shift
        if (dayShiftCount < MAX_DAY_SHIFTS) {
            for (int idx : indices) {
                if (shiftMatrix[idx][currentDay].equals("") && canAssignShift(shiftMatrix, idx, currentDay, Assign.DAY_SHIFT)) {
                    shiftMatrix[idx][currentDay] = Assign.DAY_SHIFT;
                    dayShiftCount++;
                }
            }
        }

        // Ensure exactly 2 staff in the night shift
        while (nightShiftCount < MAX_NIGHT_SHIFTS) {
            for (int idx : indices) {
                if (shiftMatrix[idx][currentDay].equals("") && canAssignShift(shiftMatrix, idx, currentDay, Assign.NIGHT_SHIFT)) {
                    shiftMatrix[idx][currentDay] = Assign.NIGHT_SHIFT;
                    nightShiftCount++;
                }
            }
        }

        // Ensure all shifts are assigned
        for (int idx : indices) {
            if (shiftMatrix[idx][currentDay].equals("")) {
                shiftMatrix[idx][currentDay] = Assign.DAY_SHIFT;
            }
        }
    }

    private boolean canAssignShift(String[][] shiftMatrix, int staffIndex, int currentDay, String shiftType) {
        // Check previous day shift
        if (currentDay > 1) {
            String previousShift = shiftMatrix[staffIndex][currentDay - 1];
            if ((shiftType.equals(Assign.DAY_SHIFT) && previousShift.equals(Assign.NIGHT_SHIFT)) ||
                (shiftType.equals(Assign.NIGHT_SHIFT) && previousShift.equals(Assign.DAY_SHIFT))) {
                return false;
            }
        }
        // Check next day shift
        if (currentDay < shiftMatrix[staffIndex].length - 1) {
            String nextShift = shiftMatrix[staffIndex][currentDay + 1];
            if ((shiftType.equals(Assign.DAY_SHIFT) && nextShift.equals(Assign.NIGHT_SHIFT)) ||
                (shiftType.equals(Assign.NIGHT_SHIFT) && nextShift.equals(Assign.DAY_SHIFT))) {
                return false;
            }
        }
        return true;
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

    @PostMapping("/Deleteassign")
    public String Deleteschedule(@RequestParam("assignid") int assignId, @RequestParam("week") String week, @RequestParam("month") String month, @RequestParam("scheduleid") int scheduleid) {
        try {
            assignDAO.deleteassign(assignId);
            return "redirect:/listassigns?week=" + week + "&month=" + month + "&scheduleid=" + scheduleid;
        } catch (SQLException e) {
            System.out.println("Error deleting assign: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
}


