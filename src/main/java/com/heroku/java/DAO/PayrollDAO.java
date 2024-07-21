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
        String query = "SELECT sign_in_time, sign_out_time FROM attendance WHERE id = ? AND attendance_date BETWEEN ? AND ? AND Status = 'Present'";
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
        String query = "INSERT INTO payroll (id, month, hours_worked, hourly_rate, total_pay) VALUES (?, ?, ?, ?, ?)";
        Connection connection = dataSource.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, payroll.getSid());
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
            String sql = "Select s.id, s.staffname, p.payrollid, p.month, p.hours_worked, p.hourly_rate, p.total_pay " + 
                                "            FROM staff s " + 
                                "            JOIN payroll p ON p.id = s.id";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payroll payroll = new Payroll();
                    payroll.setPayrollId(resultSet.getInt("payrollid"));
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


    public boolean updatePayroll(int payrollid) throws SQLException {
        String sql = "UPDATE payroll SET publish = TRUE WHERE payrollid = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payrollid);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public List<Payroll> getPayrollsById(int id) {
        List<Payroll> payrolls = new ArrayList<>();

        String sql = "SELECT s.id, s.staffname, p.payrollid, p.month, p.hours_worked, p.hourly_rate, p.total_pay " +
                     "FROM staff s " +
                     "JOIN payroll p ON p.id = s.id " +
                     "WHERE s.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Payroll payroll = new Payroll();
                    payroll.setSid(rs.getInt("id"));
                    payroll.setSname(rs.getString("staffname"));
                    payroll.setPayrollId(rs.getInt("payrollid"));
                    payroll.setMonth(rs.getString("month"));
                    payroll.setHoursWorked(rs.getInt("hours_worked"));
                    payroll.setHourlyRate(rs.getDouble("hourly_rate"));
                    payroll.setTotalPay(rs.getDouble("total_pay"));
                    payrolls.add(payroll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payrolls;
    }

    public Payroll getPayrollByPayrollId(int payrollId, String month) {
        Payroll payroll = null;
        System.out.println("month"  + month);
        String sql = "SELECT s.id, s.staffname, s.staffaddress, s.staffic, p.payrollid, p.month, p.hours_worked, p.hourly_rate, p.total_pay " +
                     "FROM staff s " +
                     "JOIN payroll p ON p.id = s.id " +
                     "WHERE p.payrollid = ? " +
                     "AND p.month = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payrollId);
            stmt.setString(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    payroll = new Payroll();
                    payroll.setSid(rs.getInt("id"));
                    payroll.setSname(rs.getString("staffname"));
                    payroll.setSaddress(rs.getString("staffaddress"));
                    payroll.setSic(rs.getString("staffic"));
                    payroll.setPayrollId(rs.getInt("payrollid"));
                    payroll.setMonth(rs.getString("month"));
                    payroll.setHoursWorked(rs.getInt("hours_worked"));
                    payroll.setHourlyRate(rs.getDouble("hourly_rate"));
                    payroll.setTotalPay(rs.getDouble("total_pay"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payroll;
    }

    //downloadpdf//
    public List<Payroll> getPayrolls(int payrollId, String month) throws SQLException {
        List<Payroll> listofPayroll = new ArrayList<>();
        String query = "SELECT s.id, s.staffname, s.staffaddress, s.staffic, p.payrollid, p.month, p.hours_worked, p.hourly_rate, p.total_pay " +
                       "FROM staff s " +
                       "JOIN payroll p ON p.id = s.id " +
                       "WHERE p.payrollid = ? AND p.month = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, payrollId);
            preparedStatement.setString(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int sid = resultSet.getInt("id");
                String sname = resultSet.getString("staffname");
                String sic = resultSet.getString("staffic");
                String saddress = resultSet.getString("staffaddress");
                int hoursWorked = resultSet.getInt("hours_worked");
                double hourlyRate = resultSet.getDouble("hourly_rate");
                double totalPay = resultSet.getDouble("total_pay");

                Payroll payroll = new Payroll(payrollId, month, hoursWorked, hourlyRate, totalPay, sname, sid, saddress, sic);
                listofPayroll.add(payroll);
            }
        }
        return listofPayroll;
    }
}