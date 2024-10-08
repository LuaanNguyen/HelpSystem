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
        String userTableQuery = "CREATE TABLE IF NOT EXISTS helpsystem_users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(255) UNIQUE, "
                + "password VARCHAR(255), "
                + "roles VARCHAR(255))";
        statement.execute(userTableQuery);
    }

    //Check whether the DB is empty or not
    public boolean isDBEmpty() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM helpsystem_users";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("count") == 0;
        }
        return true;
    }

    //register new user
    public void register(String username, String password, String role) throws SQLException {
        String query = "INSERT INTO helpsystem_users (username, password, roles) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        }
    }

    //login new user
    public boolean login(String username, String password) throws SQLException {
        String query = "SELECT * FROM helpsystem_users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    //check if the user exists
    public boolean doesUserExist(String username) {
        String query = "SELECT COUNT(*) FROM helpsystem_users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    //Display user in terminal (for now)
    public String displayUsersByUser() {
        String query = "SELECT * FROM helpsystem_users";
        StringBuilder result = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Retrieve by column name
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("roles");

                // Append values to result
                result.append("ID: ").append(id)
                        .append(", username: ").append(username)
                        .append(", password: ").append(password)
                        .append(", Role(s): ").append(role)
                        .append("\n");

//                // Display values
//                System.out.print("ID: " + id);
//                System.out.print(", username: " + username);
//                System.out.print(", password: " + password);
//                System.out.println(", Role(s): " + role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while displaying users.";
        }
        return result.toString();
    }

    //Close DB connection
    public void closeConnection() {
        try{
            if(statement!=null) statement.close();
        } catch(SQLException se2) {
            se2.printStackTrace();
        }
        try {
            if(connection!=null) connection.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }
}

