package application;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.SecureRandom;
import java.util.List;
import java.util.ArrayList;

public class DatabaseUtil {
    // JDBC driver name and database URL
    private static final String JDBC_URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
    static final String JDBC_DRIVER = "org.h2.Driver";

    //Variables for random generator
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    private Connection connection = null;
    private Statement statement = null;

    public String generateInvitationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

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
                + "email VARCHAR(255) UNIQUE, "
                + "username VARCHAR(255) UNIQUE, "
                + "password VARCHAR(255), "
                + "roles VARCHAR(255))";
        statement.execute(userTableQuery);

        // Ensure the email column exists
        String addEmailColumnQuery = "ALTER TABLE helpsystem_users ADD COLUMN IF NOT EXISTS email VARCHAR(255) UNIQUE";
        statement.execute(addEmailColumnQuery);
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
    public void register( String username, String password, String role) throws SQLException {
        String query = "INSERT INTO helpsystem_users ( username, password, roles) VALUES ( ?, ?, ?)";
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

    //invite new user
    public void inviteUser(String email, String role) throws SQLException {
        String invitationCode = generateInvitationCode();
        String query = "INSERT INTO invitations (email, role, code) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, role);
            pstmt.setString(3, invitationCode);
            pstmt.executeUpdate();
        }
        System.out.println("Invitation code: " + invitationCode);
    }

    //Reset user account
    public void resetUserAccount(String username, String oneTimePassword, Timestamp expiration) throws SQLException {
        String query = "UPDATE helpsystem_users SET one_time_password = ?, expiration = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, oneTimePassword);
            pstmt.setTimestamp(2, expiration);
            pstmt.setString(3, username);
            pstmt.executeUpdate();
        }
    }

    //delete user account
    public void deleteUserAccount(String username) throws SQLException {
        String query = "DELETE FROM helpsystem_users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    //Add and remove roles
    public void addRoleToUser(String username, String role) throws SQLException {
        String query = "UPDATE helpsystem_users SET roles = CONCAT(roles, ?) WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "," + role);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public void removeRoleFromUser(String username, String role) throws SQLException {
        String query = "UPDATE helpsystem_users SET roles = REPLACE(roles, ?, '') WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, role);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }


    //list user accounts
    public List<User> listUserAccounts() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM helpsystem_users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User(rs.getString("username"), rs.getString("password"), rs.getString("roles"));
                users.add(user);
            }
        }
        return users;
    }

    //Use invitation code
    public boolean useInvitationCode(String code, String username, String password) throws SQLException {
        String query = "SELECT * FROM invitations WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("email");
                    String role = rs.getString("role");
                    register( username, password, role);
                    deleteInvitationCode(code);
                    return true;
                }
            }
        }
        return false;
    }

    //delete invitation code
    private void deleteInvitationCode(String code) throws SQLException {
        String query = "DELETE FROM invitations WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
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
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("roles");

                // Append values to result
                result.append("ID: ").append(id)
                        .append(", email: ").append(email)
                        .append(", username: ").append(username)
                        .append(", password: ").append(password)
                        .append(", Role(s): ").append(role)
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while displaying users.";
        }
        return result.toString();
    }



    //Reset Database (Sudo action)
    public void resetDatabase() throws SQLException {
        String dropUserTableQuery = "DROP TABLE IF EXISTS helpsystem_users";
        statement.execute(dropUserTableQuery);
        createTables();  // Recreate the tables
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

