
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
import java.util.List;

@Repository
public class AssignDAO {
    private final DataSource dataSource;

    public AssignDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Assign AddAssign(@ModelAttribute("Addassign") Assign assign) throws SQLException {
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
