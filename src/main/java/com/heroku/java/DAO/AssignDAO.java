package com.heroku.java.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.heroku.java.model.Assign;
import com.heroku.java.model.Schedule;
import com.heroku.java.model.Staff;

@Repository
public class AssignDAO {
    private final DataSource dataSource;

    public AssignDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Assign AddAssign(@ModelAttribute("Addassign") Assign assign) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO assign (id, scheduleid, dt1, dt2, dt3, dt4, dt5, dt6, dt7) VALUES (?,?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            Integer id = assign.getId();
            Integer scheduleid = assign.getScheduleid();
            String dt1 = assign.getDt1();
            String dt2 = assign.getDt2();
            String dt3 = assign.getDt3();
            String dt4 = assign.getDt4();
            String dt5 = assign.getDt5();
            String dt6 = assign.getDt6();
            String dt7 = assign.getDt7();
           
            
            

            statement.setInt(1, id);
            statement.setInt(2, scheduleid);
            statement.setString(3, dt1);
            statement.setString(4, dt2);
            statement.setString(5, dt3);
            statement.setString(6, dt4);
            statement.setString(7, dt5);
            statement.setString(8, dt6);
            statement.setString(9, dt7);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                assign.setAssignid(generatedKeys.getInt(1));
            }
        }
        return assign;
    }

    public List<Assign> listassign() throws SQLException {
        List<Assign> assignlist = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM assign ";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Assign assign = new Assign();
                assign. setAssignid(resultSet.getInt("assignid"));
                assign.setId(resultSet.getInt("id"));
                assign.setScheduleid(resultSet.getInt("scheduleid"));
                assign.setDt1(resultSet.getString("dt1"));
                assign.setDt2(resultSet.getString("dt2"));
                assign.setDt3(resultSet.getString("dt3"));
                assign.setDt4(resultSet.getString("dt4"));
                assign.setDt5(resultSet.getString("dt5"));
                assign.setDt6(resultSet.getString("dt6"));
                assign.setDt7(resultSet.getString("dt7"));

                
                

                assignlist.add(assign);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return assignlist;
    }

    //getassign by ID
    public Assign getassignByAssignid(int assignId) throws SQLException {
        Assign assign = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM assign WHERE assignid=?";
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

                
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return assign;
    }
    //update schedule
    // public void updateschedule(Schedule schedule) throws SQLException {
    //     try (Connection connection = dataSource.getConnection()) {
    //         String sql = "UPDATE schedule SET schedulename=?, description=?, startdate=?, enddate=? "
    //                 + "WHERE scheduleid=?";
    //         PreparedStatement statement = connection.prepareStatement(sql);
    //         statement.setString(1, schedule.getSchedulename());
    //         statement.setString(2, schedule.getDescription());
    //         statement.setDate(3, schedule.getStartdate());
    //         statement.setDate(4, schedule.getEnddate());
    //         statement.setInt(5, schedule.getScheduleid());


    //         statement.executeUpdate();
    //         connection.close();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    // }

    // //delete schedule
    // public void deleteschedule(int scheduleId) throws SQLException {
    //     try (Connection connection = dataSource.getConnection()) {
    //         String sql = "DELETE FROM schedule WHERE scheduleid=?";
    //         PreparedStatement statement = connection.prepareStatement(sql);
    //         statement.setInt(1, scheduleId);

    //         statement.executeUpdate();
    //         connection.close();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    // }
    
    
}
