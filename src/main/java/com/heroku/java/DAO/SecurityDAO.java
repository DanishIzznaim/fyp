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

import com.heroku.java.model.Security;

@Repository
public class SecurityDAO {
    private final DataSource dataSource;

    public SecurityDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Security AddSecurity(@ModelAttribute("Addsecurity") Security security) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Masuk DAO la sial");
            String sql = "INSERT INTO security (securityname, securityaddress, securityemail, securityphone, securityusername, securitypassword, securityic) VALUES (?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String securityname = security.getName();
            String securityaddress = security.getAddress();
            String securityemail = security.getEmail();
            int securityphone = security.getPhone();
            String securityusername = security.getUsername();
            String securitypassword = security.getPassword();
            String securityic = security.getIcnumber();

            System.out.println("security name: " + securityname);
            System.out.println("security IC: " + securityic);

           
            statement.setString(1, securityname);
            statement.setString(2, securityaddress);
            statement.setString(3, securityemail);
            statement.setInt(4, securityphone);
            statement.setString(5, securityusername);
            statement.setString(6, securitypassword);
            statement.setString(7, securityic);
            statement.executeUpdate();
        }
        return security;
    }

    public List<Security> ListSecurity() throws SQLException {
        List<Security> securitylist = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM security ORDER BY securityid";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Security security = new Security();
                security.setId(resultSet.getInt("securityid"));
                security.setName(resultSet.getString("securityname"));
                security.setAddress(resultSet.getString("securityaddress"));
                security.setEmail(resultSet.getString("securityemail"));
                security.setPhone(resultSet.getInt("securityphone"));
                security.setUsername(resultSet.getString("securityusername"));
                security.setPassword(resultSet.getString("securitypassword"));
                security.setIcnumber(resultSet.getString("securityic"));

                securitylist.add(security);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return securitylist;
    }
    //getSecurity by ID
    public Security getSecurityById(int securityId) throws SQLException {
        Security security = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM security WHERE securityid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, securityId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                security = new Security();
                security.setId(resultSet.getInt("securityid"));
                security.setName(resultSet.getString("securityname"));
                security.setAddress(resultSet.getString("securityaddress"));
                security.setEmail(resultSet.getString("securityemail"));
                security.setPhone(resultSet.getInt("securityphone"));
                security.setUsername(resultSet.getString("securityusername"));
                security.setPassword(resultSet.getString("securitypassword"));
                security.setIcnumber(resultSet.getString("securityic"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return security;
    }

    //update security
    public void updateSecurity(Security security) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE security SET securityname=?, securityaddress=?, securityemail=?, securityphone=?, securityic=? "
                    + "WHERE securityid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, security.getName());
            statement.setString(2, security.getAddress());
            statement.setString(3, security.getEmail());
            statement.setInt(4, security.getPhone());
            statement.setString(5, security.getIcnumber());
            statement.setInt(6, security.getId());


            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //delete security
    public void deleteSecurity(int securityId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM security WHERE securityid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, securityId);

            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
