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
            String sql = "INSERT INTO schedule (schedulename, description, startdate, enddate) VALUES (?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String schedulename = schedule.getSchedulename();
            String description = schedule.getDescription();
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
            String sql = "SELECT * FROM schedule ";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setScheduleid(resultSet.getInt("scheduleid"));
                schedule.setSchedulename(resultSet.getString("schedulename"));
                schedule.setDescription(resultSet.getString("description"));
                schedule.setStartdate(resultSet.getDate("startdate"));
                schedule.setEnddate(resultSet.getDate("enddate"));
                

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
                schedule.setSchedulename(resultSet.getString("schedulename"));
                schedule.setDescription(resultSet.getString("description"));
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
            String sql = "UPDATE schedule SET schedulename=?, description=?, startdate=?, enddate=? "
                    + "WHERE scheduleid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, schedule.getSchedulename());
            statement.setString(2, schedule.getDescription());
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

    //delete schedule
    public void deleteschedule(int scheduleId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM schedule WHERE scheduleid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, scheduleId);

            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
}





