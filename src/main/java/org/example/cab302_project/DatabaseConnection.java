package org.example.cab302_project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * This class manages the database connection and ensures that there is only instance
 * of the database connection being used throughout the application
 */
public class DatabaseConnection {

    /**
     * The instance of the database connection
     */
    private static Connection instance = null;

    /**
     * a private constructor which establishes the database connection using SQLite
     */
    private DatabaseConnection() {
        String url = "jdbc:sqlite:database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }

    }

    /**
     * Returns the single instance of the database connection. If it doesn't exist, initialize a new connection
     *
     * @return the instance of the database connection
     */
    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }
        return instance;
    }



    /**
     * This method creates a connection for the primary purpose of testing
     *
     * @param testConnection the variable defining the test connection
     */
    public static void setTestInstance(Connection testConnection) {
        instance = testConnection;
    }
}


//need to  add


//DatabaseConnection