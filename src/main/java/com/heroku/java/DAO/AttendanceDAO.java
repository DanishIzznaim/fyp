package com.heroku.java.DAO;

import com.heroku.java.model.Attendance;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceDAO {
    private final DataSource dataSource;

    public AttendanceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Attendance save(Attendance attendance) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql;
            if (attendance.getAttendanceId() == 0) {
                sql = "INSERT INTO attendance (id, attendance_date, sign_in_time, sign_out_time, status) VALUES (?,?,?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, attendance.getId());
                    statement.setTimestamp(2, Timestamp.valueOf(attendance.getAttendanceDate()));
                    statement.setTimestamp(3, Timestamp.valueOf(attendance.getSignInTime()));
                    statement.setTimestamp(4, attendance.getSignOutTime() != null ? Timestamp.valueOf(attendance.getSignOutTime()) : null);
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
                    statement.setTimestamp(1, Timestamp.valueOf(attendance.getSignInTime()));
                    statement.setTimestamp(2, attendance.getSignOutTime() != null ? Timestamp.valueOf(attendance.getSignOutTime()) : null);
                    statement.setString(3, attendance.getStatus());
                    statement.setInt(4, attendance.getAttendanceId());
                    statement.executeUpdate();
                }
            }
        }
        return attendance;
    }

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
                        attendance.setAttendanceDate(resultSet.getTimestamp("attendance_date").toLocalDateTime());
                        attendance.setSignInTime(resultSet.getTimestamp("sign_in_time").toLocalDateTime());
                        attendance.setSignOutTime(resultSet.getTimestamp("sign_out_time") != null ? resultSet.getTimestamp("sign_out_time").toLocalDateTime() : null);
                        attendance.setStatus(resultSet.getString("status"));
                    }
                }
            }
        }
        return attendance;
    }

    public List<Attendance> findByStaffAndDate(int staffId, LocalDate date) throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM attendance WHERE id = ? AND attendance_date::date = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, staffId);
                statement.setDate(2, Date.valueOf(date));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Attendance attendance = new Attendance();
                        attendance.setAttendanceId(resultSet.getInt("attendanceid"));
                        attendance.setId(resultSet.getInt("id"));
                        attendance.setAttendanceDate(resultSet.getTimestamp("attendance_date").toLocalDateTime());
                        attendance.setSignInTime(resultSet.getTimestamp("sign_in_time").toLocalDateTime());
                        attendance.setSignOutTime(resultSet.getTimestamp("sign_out_time") != null ? resultSet.getTimestamp("sign_out_time").toLocalDateTime() : null);
                        attendance.setStatus(resultSet.getString("status"));
                        attendances.add(attendance);
                    }
                }
            }
        }
        return attendances;
    }

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
                    attendance.setAttendanceDate(resultSet.getTimestamp("attendance_date").toLocalDateTime());
                    attendance.setSignInTime(resultSet.getTimestamp("sign_in_time").toLocalDateTime());
                    attendance.setSignOutTime(resultSet.getTimestamp("sign_out_time") != null ? resultSet.getTimestamp("sign_out_time").toLocalDateTime() : null);
                    attendance.setStatus(resultSet.getString("status"));
                    attendances.add(attendance);
                }
            }
        }
        return attendances;
    }
}
