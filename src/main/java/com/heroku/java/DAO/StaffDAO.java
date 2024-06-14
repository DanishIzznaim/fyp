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

import com.heroku.java.model.Staff;

@Repository
public class StaffDAO {
    private final DataSource dataSource;

    public StaffDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Staff AddStaff(@ModelAttribute("Addstaff") Staff staff) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO staff (staffname, staffaddress, staffemail, staffphone, staffusername, staffpassword, staffic, role) VALUES (?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String staffname = staff.getName();
            String staffaddress = staff.getAddress();
            String staffemail = staff.getEmail();
            int staffphone = staff.getPhone();
            String staffusername = staff.getUsername();
            String staffpassword = staff.getPassword();
            String staffic = staff.getIcnumber();
             
            

            System.out.println("staff name: " + staffname);
            System.out.println("staff IC: " + staffic);

           
            statement.setString(1, staffname);
            statement.setString(2, staffaddress);
            statement.setString(3, staffemail);
            statement.setInt(4, staffphone);
            statement.setString(5, staffusername);
            statement.setString(6, staffpassword);
            statement.setString(7, staffic);
            statement.setString(8, "security"); 
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                staff.setId(generatedKeys.getInt(1));
            }
        }
        return staff;
    }

    public List<Staff> Liststaff() throws SQLException {
        List<Staff> stafflist = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM staff WHERE role = 'security' ORDER BY id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Staff staff = new Staff();
                staff.setId(resultSet.getInt("id"));
                staff.setName(resultSet.getString("staffname"));
                staff.setAddress(resultSet.getString("staffaddress"));
                staff.setEmail(resultSet.getString("staffemail"));
                staff.setPhone(resultSet.getInt("staffphone"));
                staff.setUsername(resultSet.getString("staffusername"));
                staff.setPassword(resultSet.getString("staffpassword"));
                staff.setIcnumber(resultSet.getString("staffic"));

                stafflist.add(staff);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return stafflist;
    }
    //getstaff by ID
    public Staff getstaffById(int staffId) throws SQLException {
        Staff staff = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM staff WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                staff = new Staff();
                staff.setId(resultSet.getInt("id"));
                staff.setName(resultSet.getString("staffname"));
                staff.setAddress(resultSet.getString("staffaddress"));
                staff.setEmail(resultSet.getString("staffemail"));
                staff.setPhone(resultSet.getInt("staffphone"));
                staff.setUsername(resultSet.getString("staffusername"));
                staff.setPassword(resultSet.getString("staffpassword"));
                staff.setIcnumber(resultSet.getString("staffic"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return staff;
    }

    //update staff
    public void updatestaff(Staff staff) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE staff SET staffname=?, staffaddress=?, staffemail=?, staffphone=?, staffic=? "
                    + "WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getAddress());
            statement.setString(3, staff.getEmail());
            statement.setInt(4, staff.getPhone());
            statement.setString(5, staff.getIcnumber());
            statement.setInt(6, staff.getId());


            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //delete staff
    public void deletestaff(int staffId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM staff WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);

            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //search staff
    public List<Staff> searchstaffByName(String name) throws SQLException{
        List<Staff> staffs = new ArrayList<>();
    
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM staff WHERE staffname LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "%" + name + "%");
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Staff staff = new Staff();
                        staff.setId(resultSet.getInt("id"));
                        staff.setName(resultSet.getString("staffname"));
                        staff.setAddress(resultSet.getString("staffaddress"));
                        staff.setEmail(resultSet.getString("staffemail"));
                        staff.setPhone(resultSet.getInt("staffphone"));
                        staff.setUsername(resultSet.getString("staffusername"));
                        staff.setPassword(resultSet.getString("staffpassword"));
                        staff.setIcnumber(resultSet.getString("staffic"));
    
                        staffs.add(staff);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving staff: " + e.getMessage());
        }
        return staffs;
        
    }

    public List<Staff> getSecurityStaff() throws SQLException {
        List<Staff> securityStaff = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, staffname FROM staff WHERE role = 'security' ORDER BY id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Staff staff = new Staff();
                staff.setId(resultSet.getInt("id"));
                staff.setName(resultSet.getString("staffname"));
                securityStaff.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return securityStaff;
    }
    
    
}





