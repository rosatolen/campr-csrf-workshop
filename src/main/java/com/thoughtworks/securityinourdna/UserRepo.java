package com.thoughtworks.securityinourdna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {

    private final Connection connection;

    public UserRepo(Connection connection) {
        this.connection = connection;
    }

    public void addName(String firstname, String password) throws Exception {
        final String query = "insert into users values (?, ?)";
        final PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, firstname);
        stmt.setString(2, password);
        stmt.execute();
    }

    public String login(String username, String password) {
        final String query = "select * from users where username = '" + username + "' and password = '" + password + "'";

        final ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery(query);
            resultSet.next();
            return resultSet.getString("username");
        } catch (SQLException e) {
            throw new InvalidCredentials();
        }
    }

    public void delete(String name) throws SQLException {
        final String query = "delete from users where username = ?";
        final PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.execute();
    }
}
