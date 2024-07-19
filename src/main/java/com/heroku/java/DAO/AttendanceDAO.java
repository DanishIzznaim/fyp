
package com.heroku.java.DAO;

import com.heroku.java.model.Attendance;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceDAO {
    private final DataSource dataSource;

    public AttendanceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Save or update attendance record
    public Attendance save(Attendance attendance) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql;
            if (attendance.getAttendanceId() == 0) {
                sql = "INSERT INTO attendance (id, attendance_date, sign_in_time, sign_out_time, status) VALUES (?,?,?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, attendance.getId());
                    statement.setDate(2, Date.valueOf(attendance.getAttendanceDate()));
                    statement.setTime(3, Time.valueOf(attendance.getSignInTime()));
                    statement.setTime(4, attendance.getSignOutTime() != null ? Time.valueOf(attendance.getSignOutTime()) : null);
                    statement.setString(5, attendance.getStatus());
                    statement.executeUpdate();
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        attendance.setAttendanceId(generatedKeys.getInt(1));
                    }
                }
            } else {
                sql = "UPDATE attendance SET sign_in_time=?, sign_out_time=?, status=? WHERE attendanceid=?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setTime(1, Time.valueOf(attendance.getSignInTime()));
                    statement.setTime(2, attendance.getSignOutTime() != null ? Time.valueOf(attendance.getSignOutTime()) : null);
                    statement.setString(3, attendance.getStatus());
                    statement.setInt(4, attendance.getAttendanceId());
                    statement.executeUpdate();
                }
            }
        }
        return attendance;
    }

    // Find attendance records by staff ID
    public List<Attendance> findByStaffId(int staffId) throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM attendance WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, staffId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Attendance attendance = new Attendance();
                        attendance.setAttendanceId(resultSet.getInt("attendanceid"));
                        attendance.setId(resultSet.getInt("id"));
                        attendance.setAttendanceDate(resultSet.getDate("attendance_date").toLocalDate());
                        attendance.setSignInTime(resultSet.getTime("sign_in_time").toLocalTime());
                        attendance.setSignOutTime(resultSet.getTime("sign_out_time") != null ? resultSet.getTime("sign_out_time").toLocalTime() : null);
                        attendance.setStatus(resultSet.getString("status"));
                        attendances.add(attendance);
                    }
                }
            }
        }
        return attendances;
    }

    // Find attendance record by ID
    public Attendance findById(int attendanceId) throws SQLException {
        Attendance attendance = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM attendance WHERE attendanceid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, attendanceId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        attendance = new Attendance();
                        attendance.setAttendanceId(resultSet.getInt("attendanceid"));
                        attendance.setId(resultSet.getInt("id"));
                        attendance.setAttendanceDate(resultSet.getDate("attendance_date").toLocalDate());
                        attendance.setSignInTime(resultSet.getTime("sign_in_time").toLocalTime());
                        attendance.setSignOutTime(resultSet.getTime("sign_out_time") != null ? resultSet.getTime("sign_out_time").toLocalTime() : null);
                        attendance.setStatus(resultSet.getString("status"));
                    }
                }
            }
        }
        return attendance;
    }

    // Find attendance records by staff ID and date
    public List<Attendance> findByStaffAndDate(int staffId, LocalDate date) throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM attendance WHERE id = ? AND attendance_date = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, staffId);
                statement.setDate(2, Date.valueOf(date));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Attendance attendance = new Attendance();
                        attendance.setAttendanceId(resultSet.getInt("attendanceid"));
                        attendance.setId(resultSet.getInt("id"));
                        attendance.setAttendanceDate(resultSet.getDate("attendance_date").toLocalDate());
                        attendance.setSignInTime(resultSet.getTime("sign_in_time").toLocalTime());
                        attendance.setSignOutTime(resultSet.getTime("sign_out_time") != null ? resultSet.getTime("sign_out_time").toLocalTime() : null);
                        attendance.setStatus(resultSet.getString("status"));
                        attendances.add(attendance);
                    }
                }
            }
        }
        return attendances;
    }

    // Find all attendance records
    public List<Attendance> findAll() throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM attendance";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setAttendanceId(resultSet.getInt("attendanceid"));
                    attendance.setId(resultSet.getInt("id"));
                    attendance.setAttendanceDate(resultSet.getDate("attendance_date").toLocalDate());
                    attendance.setSignInTime(resultSet.getTime("sign_in_time").toLocalTime());
                    attendance.setSignOutTime(resultSet.getTime("sign_out_time") != null ? resultSet.getTime("sign_out_time").toLocalTime() : null);
                    attendance.setStatus(resultSet.getString("status"));
                    attendances.add(attendance);
                }
            }
        }
        return attendances;
    }

    // Find all attendance records with staff names
    public List<Attendance> findAllWithStaffName() throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT a.attendanceid, a.id, a.attendance_date, a.sign_in_time, a.sign_out_time, a.status, s.staffname AS staffName " +
                         "FROM attendance a " +
                         "JOIN staff s ON a.id = s.id WHERE a.status='Pending'";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setAttendanceId(resultSet.getInt("attendanceid"));
                    attendance.setId(resultSet.getInt("id"));
                    attendance.setAttendanceDate(resultSet.getDate("attendance_date").toLocalDate());
                    attendance.setSignInTime(resultSet.getTime("sign_in_time").toLocalTime());
                    attendance.setSignOutTime(resultSet.getTime("sign_out_time") != null ? resultSet.getTime("sign_out_time").toLocalTime() : null);
                    attendance.setStatus(resultSet.getString("status"));
                    attendance.setStaffName(resultSet.getString("staffName"));
                    attendances.add(attendance);
                }
            }
        }
        return attendances;
    }

    // Delete attendance record by ID
    public void deleteattendance(int attendanceId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM attendance WHERE attendanceid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, attendanceId);
                statement.executeUpdate();
            }
        }
    }
}
