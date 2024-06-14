package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.heroku.java.model.Staff;

import jakarta.servlet.http.HttpSession;

@Repository
public class LoginDAO {
    private DataSource dataSource;

    public LoginDAO(DataSource dataSource, HttpSession session) {
        this.dataSource = dataSource;
    }

    public Staff authenticateUser(String username, String password) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM staff WHERE staffusername = ? AND staffpassword = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                Staff user = new Staff();
                user.setUsername(resultSet.getString("staffusername"));
                user.setPassword(resultSet.getString("staffpassword"));
                user.setRole(resultSet.getString("role"));
                user.setId(resultSet.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
        }
}
