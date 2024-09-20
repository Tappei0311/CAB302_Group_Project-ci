package org.example.cab302_project.test;

import org.example.cab302_project.DatabaseConnection;
import org.example.cab302_project.User;
import org.example.cab302_project.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();

        userDAO.createUserTable();

        clearTable();
    }

    private void clearTable() {
        try {
            Connection connection = DatabaseConnection.getInstance();

            connection.createStatement().execute("DELETE FROM Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRegisterAndGetUser() {

        boolean registered = userDAO.registerUser("testUser", "password123");
        assertTrue(registered, "User should be registered successfully");
        System.out.println("User had a successful registration ");

        User user = userDAO.loginUser("testUser", "password123");
        assertNotNull(user, "The user should be found in the database");
        System.out.println("User found in the database after registration");

        assertEquals("testUser", user.getUsername(), "Username should match the registered one");
    }

    @Test
    void testFailedLoginWithWrongPassword() {

        userDAO.registerUser("testUser", "password123");

        // try logging in with an incorrect password
        User user = userDAO.loginUser("testUser", "wrongPassword");
        assertNull(user, "User should not be able to be logged in with an incorrect password");

        System.out.println("User was unable to login with incorrect password");
    }
}