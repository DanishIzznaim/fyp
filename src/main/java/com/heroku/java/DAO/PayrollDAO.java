package com.heroku.java.DAO;

import com.heroku.java.model.Payroll;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
@Repository
public class PayrollDAO {

    private final DataSource dataSource;

    public PayrollDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<LocalTime[]> findSignInSignOutTimesByStaffAndMonth(int staffId, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<LocalTime[]> timesList = new ArrayList<>();
        String query = "SELECT sign_in_time, sign_out_time FROM attendance WHERE id = ? AND attendance_date BETWEEN ? AND ?";
        Connection connection = dataSource.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, staffId);
            statement.setDate(2, Date.valueOf(startDate));
            statement.setDate(3, Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LocalTime signInTime = resultSet.getTime("sign_in_time") != null ? resultSet.getTime("sign_in_time").toLocalTime() : null;
                LocalTime signOutTime = resultSet.getTime("sign_out_time") != null ? resultSet.getTime("sign_out_time").toLocalTime() : null;
                timesList.add(new LocalTime[]{signInTime, signOutTime});
            }
        }
        return timesList;
    }

    public void save(Payroll payroll) throws SQLException {
        String query = "INSERT INTO payroll (attendanceid, month, hours_worked, hourly_rate, total_pay) VALUES (?, ?, ?, ?, ?)";
        Connection connection = dataSource.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, payroll.getAttendanceId());
            statement.setString(2, payroll.getMonth());
            statement.setInt(3, payroll.getHoursWorked());
            statement.setDouble(4, payroll.getHourlyRate());
            statement.setDouble(5, payroll.getTotalPay());
            statement.executeUpdate();
        }
    }

    // public List<Payroll> findAll() throws SQLException {
    //     List<Payroll> payrollList = new ArrayList<>();
    //     String query = "SELECT * FROM payroll";
    //     Connection connection = dataSource.getConnection();
    //     try (Statement statement = connection.createStatement()) {
    //         ResultSet resultSet = statement.executeQuery(query);
    //         while (resultSet.next()) {
    //             Payroll payroll = new Payroll();
    //             payroll.setAttendanceId(resultSet.getInt("attendanceid"));
    //             payroll.setMonth(resultSet.getString("month"));
    //             payroll.setHoursWorked(resultSet.getInt("hours_worked"));
    //             payroll.setHourlyRate(resultSet.getDouble("hourly_rate"));
    //             payroll.setTotalPay(resultSet.getDouble("total_pay"));
    //             payrollList.add(payroll);
    //         }
    //     }
    //     return payrollList;
    // }


    // Find all payroll records
    public List<Payroll> findAll() throws SQLException {
        List<Payroll> payrolls = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT payroll.*, staff.id,staff.staffname " +
                                "FROM " +
                                "    payroll " +
                                "JOIN " +
                                "    attendance ON payroll.attendanceid = attendance.attendanceid " +
                                "JOIN " +
                                "    staff ON attendance.id = staff.id " +
                                "WHERE " +
                                "    attendance.status = 'Present'";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payroll payroll = new Payroll();
                    payroll.setPayrollId(resultSet.getInt("payrollid"));
                    payroll.setAttendanceId(resultSet.getInt("attendanceid"));
                    payroll.setMonth(resultSet.getString("month"));
                    payroll.setHoursWorked(resultSet.getInt("hours_worked"));
                    payroll.setHourlyRate(resultSet.getDouble("hourly_rate"));
                    payroll.setTotalPay(resultSet.getDouble("total_pay"));
                    payroll.setSname(resultSet.getString("staffname"));
                    payroll.setSid(resultSet.getInt("id"));
                    payrolls.add(payroll);
                }
            }
        }
        return payrolls;
    }
}
