package com.thoughtworks.securityinourdna;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserRepoTest {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private Connection conn;

    @Before
    public void setup() throws Exception {
        conn = connectionFactory.createInMemoryDatabase();
    }

    @Test
    public void add_names_should_insert_names_into_the_database() throws Exception {
        // Given
        UserRepo repo = new UserRepo(conn);

        // When
        repo.addName("Alice", "password");
        repo.addName("Bob", "password");

        // Then
        assertEquals(getUserCount(conn), 2);
    }


    @Test
    public void login_should_work_for_existing_user() throws Exception {
        // Given
        UserRepo repo = new UserRepo(conn);
        repo.addName("Alice", "password");

        // When
        String username = repo.login("Alice", "password");

        // Then
        assertThat(username, is("Alice"));
    }

    @Test (expected = InvalidCredentials.class)
    public void login_should_deny_non_existing_user() throws Exception {
        // Given
        UserRepo repo = new UserRepo(conn);
        repo.addName("Alice", "password");

        // When
        repo.login("nobody", "password");
    }

    private int getUserCount(Connection conn) throws Exception {
        final String userCountQuery = "select count(*) as user_count from users";
        final ResultSet resultSet = conn.createStatement().executeQuery(userCountQuery);

        resultSet.next();

        return resultSet.getInt("user_count");
    }

    @Test
    public void should_delete_existing_user() throws Exception {
        // Given
        UserRepo repo = new UserRepo(conn);
        repo.addName("Alice", "password");
        repo.addName("Bob", "password");

        // When
        repo.delete("Alice");

        // Then
        assertThat(getUserCount(conn), is(1));
    }
}
