package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

public class StaffEmailDAO {
    private final DataSource dataSource;

    public StaffEmailDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<String> getStaffEmail() throws SQLException {
        ArrayList<String> staffs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT staffemail From staff";
            final var statement = connection.prepareStatement(sql);
            final var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String EmailToStaff = resultSet.getString("staffemail");
                staffs.add(EmailToStaff);
            }
        }
        return staffs;
    }

    public ArrayList<String> getPayslipEmail(int payrollId) throws SQLException {
        ArrayList<String> staffs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT s.staffemail FROM staff s" + 
                                "JOIN payroll p ON p.id = s.id " + 
                                "WHERE p.publish = TRUE";
            final var statement = connection.prepareStatement(sql);
           
            statement.setInt(1, payrollId);
            
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String EmailToStaff = resultSet.getString("staffemail");
                staffs.add(EmailToStaff);
            }
        }
        return staffs;
    }
}
