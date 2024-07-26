
package com.heroku.java.DAO;

import com.heroku.java.model.Assign;
import com.heroku.java.model.Schedule;
import com.heroku.java.model.Staff;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class AssignDAO {
    private final DataSource dataSource;

    private static final int MAX_NIGHT_SHIFTS = 2;
    private static final int MAX_DAY_SHIFTS = 1;

    public AssignDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // public Assign AddAssign(@ModelAttribute("Addassign") Assign assign) throws SQLException {
    //     try (Connection connection = dataSource.getConnection()) {
    //         String sql = "INSERT INTO assign (id, scheduleid, dt1, dt2, dt3, dt4, dt5, dt6, dt7) VALUES (?,?,?,?,?,?,?,?,?)";
    //         final var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

    //         statement.setInt(1, assign.getId());
    //         statement.setInt(2, assign.getScheduleid());
    //         statement.setString(3, assign.getDt1());
    //         statement.setString(4, assign.getDt2());
    //         statement.setString(5, assign.getDt3());
    //         statement.setString(6, assign.getDt4());
    //         statement.setString(7, assign.getDt5());
    //         statement.setString(8, assign.getDt6());
    //         statement.setString(9, assign.getDt7());
    //         statement.executeUpdate();

    //         ResultSet generatedKeys = statement.getGeneratedKeys();
    //         if (generatedKeys.next()) {
    //             assign.setAssignid(generatedKeys.getInt(1));
    //         }
    //     }
    //     return assign;
    // }

    public Assign AddAssign(Assign assign) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO assign (id, scheduleid, dt1, dt2, dt3, dt4, dt5, dt6, dt7) VALUES (?,?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1, assign.getId());
            statement.setInt(2, assign.getScheduleid());
            statement.setString(3, assign.getDt1());
            statement.setString(4, assign.getDt2());
            statement.setString(5, assign.getDt3());
            statement.setString(6, assign.getDt4());
            statement.setString(7, assign.getDt5());
            statement.setString(8, assign.getDt6());
            statement.setString(9, assign.getDt7());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                assign.setAssignid(generatedKeys.getInt(1));
            }
        }
        return assign;
    }

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
                (shiftType.equals(Assign.NIGHT_SHIFT) && previousShift.equals(Assign.DAY_SHIFT)) ||
                shiftOverlap(previousShift, shiftType)) {
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

    private boolean shiftOverlap(String shift1, String shift2) {
        // Assuming shift1 is the current shift and shift2 is the previous or next shift
        if (shift1.equals(Assign.NIGHT_SHIFT) && shift2.equals(Assign.DAY_SHIFT)) {
            // Night shift from previous day ends at 8:00 AM, day shift starts at 8:00 AM
            return true;
        }
        if (shift1.equals(Assign.DAY_SHIFT) && shift2.equals(Assign.NIGHT_SHIFT)) {
            // Day shift from previous day ends at 8:00 PM, night shift starts at 8:00 PM
            return true;
        }
        return false;
    
    }

    public List<Assign> listassigns(String week, String month, int scheduleid) throws SQLException {
        List<Assign> assignlist = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT a.*, s.staffname AS staff_name " + 
                         "FROM assign a " +
                         "JOIN staff s ON a.id = s.id " +
                         "JOIN schedule sch ON a.scheduleid = sch.scheduleid " +
                         "WHERE sch.week = ? AND sch.month = ? AND sch.scheduleid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, week); // Set the week parameter
            statement.setString(2, month); // Set the month parameter
            statement.setInt(3, scheduleid); // Set the scheduleid parameter
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Assign assign = new Assign();
                assign.setAssignid(resultSet.getInt("assignid"));
                assign.setId(resultSet.getInt("id"));
                assign.setScheduleid(resultSet.getInt("scheduleid"));
                assign.setDt1(resultSet.getString("dt1"));
                assign.setDt2(resultSet.getString("dt2"));
                assign.setDt3(resultSet.getString("dt3"));
                assign.setDt4(resultSet.getString("dt4"));
                assign.setDt5(resultSet.getString("dt5"));
                assign.setDt6(resultSet.getString("dt6"));
                assign.setDt7(resultSet.getString("dt7"));
                assign.setName(resultSet.getString("staff_name")); // Setting staff name
    
                assignlist.add(assign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    
        return assignlist;
    }

    // public Assign getassignByAssignid(int assignId) throws SQLException {
    //     Assign assign = null;
    //     try (Connection connection = dataSource.getConnection()) {
    //         String sql = "SELECT * FROM assign WHERE assignid=?";
    //         PreparedStatement statement = connection.prepareStatement(sql);
    //         statement.setInt(1, assignId);
    //         ResultSet resultSet = statement.executeQuery();

    //         if (resultSet.next()) {
    //             assign = new Assign();
    //             assign.setAssignid(resultSet.getInt("assignid"));
    //             assign.setId(resultSet.getInt("id"));
    //             assign.setScheduleid(resultSet.getInt("scheduleid"));
    //             assign.setDt1(resultSet.getString("dt1"));
    //             assign.setDt2(resultSet.getString("dt2"));
    //             assign.setDt3(resultSet.getString("dt3"));
    //             assign.setDt4(resultSet.getString("dt4"));
    //             assign.setDt5(resultSet.getString("dt5"));
    //             assign.setDt6(resultSet.getString("dt6"));
    //             assign.setDt7(resultSet.getString("dt7"));
    //         }
    //         connection.close();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }

    //     return assign;
    // }

    public Assign getassignByAssignid(int assignId) throws SQLException {
        Assign assign = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT a.*, s.staffname AS name FROM assign a JOIN staff s ON a.id = s.id WHERE a.assignid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, assignId);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                assign = new Assign();
                assign.setAssignid(resultSet.getInt("assignid"));
                assign.setId(resultSet.getInt("id"));
                assign.setScheduleid(resultSet.getInt("scheduleid"));
                assign.setDt1(resultSet.getString("dt1"));
                assign.setDt2(resultSet.getString("dt2"));
                assign.setDt3(resultSet.getString("dt3"));
                assign.setDt4(resultSet.getString("dt4"));
                assign.setDt5(resultSet.getString("dt5"));
                assign.setDt6(resultSet.getString("dt6"));
                assign.setDt7(resultSet.getString("dt7"));
                assign.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    
        return assign;
    }
    
    public void updateAssign(Assign assign) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE assign SET dt1=?, dt2=?, dt3=?, dt4=?, dt5=?, dt6=?, dt7=? WHERE assignid=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, assign.getDt1());
            statement.setString(2, assign.getDt2());
            statement.setString(3, assign.getDt3());
            statement.setString(4, assign.getDt4());
            statement.setString(5, assign.getDt5());
            statement.setString(6, assign.getDt6());
            statement.setString(7, assign.getDt7());
            statement.setInt(8, assign.getAssignid());

            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteassign(int assignId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM assign WHERE assignid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, assignId);

            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
