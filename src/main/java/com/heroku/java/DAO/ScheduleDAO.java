package com.heroku.java.DAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.heroku.java.model.Schedule;
import com.heroku.java.model.Staff;

@Repository
public class ScheduleDAO {
    private final DataSource dataSource;

    public ScheduleDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Schedule AddSchedule(@ModelAttribute("Addschedule") Schedule schedule) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO schedule (month, week, startdate, enddate) VALUES (?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String schedulename = schedule.getMonth();
            String description = schedule.getWeek();
            Date startdate = schedule.getStartdate();
            Date enddate = schedule.getEnddate();

            // System.out.println("staff name: " + staffname);
            // System.out.println("staff IC: " + staffic);

           
            statement.setString(1, schedulename);
            statement.setString(2, description);
            statement.setDate(3, startdate);
            statement.setDate(4, enddate);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                schedule.setScheduleid(generatedKeys.getInt(1));
            }
        }
        return schedule;
    }

    public List<Schedule> listschedules() throws SQLException {
        List<Schedule> schedulelist = new ArrayList<>();
    
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT schedule.*, EXISTS (SELECT 1 FROM assign WHERE assign.scheduleid = schedule.scheduleid) as isAssigned FROM schedule";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setScheduleid(resultSet.getInt("scheduleid"));
                schedule.setMonth(resultSet.getString("month"));
                schedule.setWeek(resultSet.getString("week"));
                schedule.setStartdate(resultSet.getDate("startdate"));
                schedule.setEnddate(resultSet.getDate("enddate"));
                schedule.setIsAssigned(resultSet.getBoolean("isAssigned")); // Set the assigned status
    
                schedulelist.add(schedule);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    
        return schedulelist;
    }
    

    //getstaff by ID
    public Schedule getscheduleByScheduleid(int scheduleId) throws SQLException {
        Schedule schedule = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM schedule WHERE scheduleid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, scheduleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                schedule = new Schedule();
                schedule.setScheduleid(resultSet.getInt("scheduleid"));
                schedule.setMonth(resultSet.getString("month"));
                schedule.setWeek(resultSet.getString("week"));
                schedule.setStartdate(resultSet.getDate("startdate"));
                schedule.setEnddate(resultSet.getDate("enddate"));
                
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return schedule;
    }
    //update schedule
    public void updateschedule(Schedule schedule) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE schedule SET month=?, week=?, startdate=?, enddate=? "
                    + "WHERE scheduleid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, schedule.getMonth());
            statement.setString(2, schedule.getWeek());
            statement.setDate(3, schedule.getStartdate());
            statement.setDate(4, schedule.getEnddate());
            statement.setInt(5, schedule.getScheduleid());


            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public void deleteSchedule(int scheduleId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // Delete from assign table first
            String deleteAssignSQL = "DELETE FROM assign WHERE scheduleid=?";
            PreparedStatement deleteAssignStmt = connection.prepareStatement(deleteAssignSQL);
            deleteAssignStmt.setInt(1, scheduleId);
            deleteAssignStmt.executeUpdate();

            // Delete from schedule table
            String deleteScheduleSQL = "DELETE FROM schedule WHERE scheduleid=?";
            PreparedStatement deleteScheduleStmt = connection.prepareStatement(deleteScheduleSQL);
            deleteScheduleStmt.setInt(1, scheduleId);
            deleteScheduleStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}


