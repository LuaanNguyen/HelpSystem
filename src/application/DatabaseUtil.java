package application;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    // JDBC driver name and database URL
    private static final String JDBC_URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
    static final String JDBC_DRIVER = "org.h2.Driver";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    private Connection connection = null;
    private Statement statement = null;

    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER); // Load the JDBC driver
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(JDBC_URL, USER, PASS);
            statement = connection.createStatement();
            createTables();  // Create the necessary tables if they don't exist
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    //Create table for all the users
    private void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS helpsystem_users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "email VARCHAR(255) UNIQUE, "
                + "password VARCHAR(255), "
                + "role VARCHAR(20))";
        statement.execute(userTable);
    }

    //Check whether the DB is empty or not



}

