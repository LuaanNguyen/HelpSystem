package application;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;
/**
 * <p> DatabaseUtil </p>
 *
 * <p> Description: DB Class to interact with H2 In-memory database </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2024 </p>
 *
 * @author Luan Nguyen, Smit Devrukhkar, Gabriel Clark, Meadow Kubanski, Isabella Paschal
 * @version 1.00
 */

public class DatabaseUtil {
    // JDBC driver name and database URL
    private static final String JDBC_URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
    static final String JDBC_DRIVER = "org.h2.Driver";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    //Variables for random generator
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    private Connection connection = null;
    private Statement statement = null;

    private EncryptionHelper encryptionHelper;

    public DatabaseUtil() throws Exception {
        encryptionHelper = new EncryptionHelper();
    }

    /* Generate random string for 1 time passcode */
    public String generateInvitationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    /* Attempt to connect to Database with provided credentials */
    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER); // Load the JDBC driver
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(JDBC_URL, USER, PASS);
            statement = connection.createStatement();
            createUserTables();  // Create the necessary tables if they don't exist
            createInvitationsTable(); // Create the invitations table
            createHelpItemTable(); // Create the help items table
            createSpecialAccessGroupTables();
            System.out.println("Database initialized successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    /* Create table for all the users */
    private void createUserTables() throws SQLException {
        String userTableQuery = "CREATE TABLE IF NOT EXISTS helpsystem_users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "email VARCHAR(255) UNIQUE, "
                + "username VARCHAR(255) UNIQUE, "
                + "password VARCHAR(255), "
                + "first_name VARCHAR(255), "
                + "middle_name VARCHAR(255), "
                + "last_name VARCHAR(255), "
                + "preferred_first_name VARCHAR(255), "
                + "roles VARCHAR(255))";
        statement.execute(userTableQuery);

        // Ensure the necessary columns exist
        String addFirstNameColumnQuery = "ALTER TABLE helpsystem_users ADD COLUMN IF NOT EXISTS first_name VARCHAR(255)";
        statement.execute(addFirstNameColumnQuery);

        String addMiddleNameColumnQuery = "ALTER TABLE helpsystem_users ADD COLUMN IF NOT EXISTS middle_name VARCHAR(255)";
        statement.execute(addMiddleNameColumnQuery);

        String addLastNameColumnQuery = "ALTER TABLE helpsystem_users ADD COLUMN IF NOT EXISTS last_name VARCHAR(255)";
        statement.execute(addLastNameColumnQuery);

        String addPreferredFirstNameColumnQuery = "ALTER TABLE helpsystem_users ADD COLUMN IF NOT EXISTS preferred_first_name VARCHAR(255)";
        statement.execute(addPreferredFirstNameColumnQuery);

        String addEmailColumnQuery = "ALTER TABLE helpsystem_users ADD COLUMN IF NOT EXISTS email VARCHAR(255) UNIQUE";
        statement.execute(addEmailColumnQuery);
    }

    /* Create table for all the invitations */
    public void createInvitationsTable() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS invitations ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "role VARCHAR(255), "
                + "code VARCHAR(255))";
        statement.execute(createTableQuery);
    }

    /* Create table for special access group */
    public void createSpecialAccessGroupTables() throws SQLException {
        // Create the main special access groups table
        String specialAccessGroupsTable = "CREATE TABLE IF NOT EXISTS special_access_groups ("
                + "group_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "group_name VARCHAR(255) UNIQUE, "
                + "created_by VARCHAR(255), "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        statement.execute(specialAccessGroupsTable);

        // Create table for encrypted articles in groups
        String groupArticlesTable = "CREATE TABLE IF NOT EXISTS group_articles ("
                + "article_id INT, "
                + "group_id INT, "
                + "encrypted_content TEXT, "
                + "PRIMARY KEY (article_id, group_id), "
                + "FOREIGN KEY (group_id) REFERENCES special_access_groups(group_id))";
        statement.execute(groupArticlesTable);

        // Create table for group permissions
        String groupPermissionsTable = "CREATE TABLE IF NOT EXISTS group_permissions ("
                + "group_id INT, "
                + "username VARCHAR(255), "
                + "permission_type VARCHAR(50), "  // 'ADMIN' or 'VIEW'
                + "PRIMARY KEY (group_id, username, permission_type), "
                + "FOREIGN KEY (group_id) REFERENCES special_access_groups(group_id))";
        statement.execute(groupPermissionsTable);
    }

    /* Create a special access group */
    public void createSpecialAccessGroup(String groupName, String creatorUsername) throws SQLException {
        String query = "INSERT INTO special_access_groups (group_name, created_by) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, groupName);
            pstmt.setString(2, creatorUsername);
            pstmt.executeUpdate();

            // Get the generated group ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int groupId = rs.getInt(1);
                    // Add creator as both admin and viewer
                    addGroupPermission(groupId, creatorUsername, "ADMIN");
                    addGroupPermission(groupId, creatorUsername, "VIEW");
                }
            }
        }
    }

    /* Add permission to special access group*/
    private void addGroupPermission(int groupId, String username, String permissionType) throws SQLException {
        String query = "INSERT INTO group_permissions (group_id, username, permission_type) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            pstmt.setString(2, username);
            pstmt.setString(3, permissionType);
            pstmt.executeUpdate();
        }
    }


    /* Check if DB is empty */
    public boolean isDBEmpty() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM helpsystem_users";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("count") == 0;
        }
        return true;
    }

    /* Register a new user*/
    public void register(String username, String password, String role) throws SQLException {
        String query = "INSERT INTO helpsystem_users ( username, password, roles) VALUES ( ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        }
    }

    /* Login */
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

    /* Invite new user with 1-time code*/
    public void inviteUser(String code, String role) throws SQLException {
        String query = "INSERT INTO invitations ( role, code) VALUES ( ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, role);
            pstmt.setString(2, code);
            pstmt.executeUpdate();
        }
        System.out.println("Invitation code: " + code);
    }

    /* Use invitation code */
    public boolean useInvitationCode(String code, String username, String password) throws SQLException {
        String query = "SELECT * FROM invitations WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("email");
                    String role = rs.getString("role");
                    register(username, password, role);
                    deleteInvitationCode(code);
                    return true;
                }
            }
        }
        return false;
    }

    /* Delete invitation code */
    private void deleteInvitationCode(String code) throws SQLException {
        String query = "DELETE FROM invitations WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        }
    }

    /* Get user by username */
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM helpsystem_users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("password"), rs.getString("roles"));
                }
            }
        }
        return null;
    }

    /* Reset user account */
    public void resetUserAccount(String username, String oneTimePassword, Timestamp expiration) throws SQLException {
        String query = "UPDATE helpsystem_users SET one_time_password = ?, expiration = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, oneTimePassword);
            pstmt.setTimestamp(2, expiration);
            pstmt.setString(3, username);
            pstmt.executeUpdate();
        }
    }

    /* Delete user account */
    public void deleteUserAccount(String username) throws SQLException {
        String query = "DELETE FROM helpsystem_users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    /* Add new roles to user */
    public void addRoleToUser(String username, String role) throws SQLException {
        String query = "UPDATE helpsystem_users SET roles = CONCAT(roles, ?) WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "," + role);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    /* Remove role for user */
    public void removeRoleFromUser(String username, String role) throws SQLException {
        String query = "UPDATE helpsystem_users SET roles = REPLACE(roles, ?, '') WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, role);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }


    /* List all users */
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


    /* Check if the user exists */
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

    /*Check if the invitation code is valid */
    public boolean isValidInvitationCode(String code) throws SQLException {
        String query = "SELECT COUNT(*) FROM invitations WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /* Invalidate invitation code */
    public void invalidateInvitationCode(String code) throws SQLException {
        String query = "DELETE FROM invitations WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
        }
    }


    /*  Update user details */
    public void updateUserDetails(String username, String email, String firstName, String middleName, String lastName, String preferredFirstName) throws SQLException {
        String query = "UPDATE helpsystem_users SET email = ?, first_name = ?, middle_name = ?, last_name = ?, preferred_first_name = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, firstName);
            pstmt.setString(3, middleName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, preferredFirstName);
            pstmt.setString(6, username);
            pstmt.executeUpdate();
        }
    }

    /* Get user by username */
    public void resetUserDatabase() throws SQLException {
        String dropUserTableQuery = "DROP TABLE IF EXISTS helpsystem_users";
        statement.execute(dropUserTableQuery);
        createUserTables();  // Recreate the tables
    }

    /* Get user by username */
    public void resetInvitationDatabase() throws SQLException {
        String dropInvitationQuery = "DROP TABLE IF EXISTS invitations";
        statement.execute(dropInvitationQuery);
        createInvitationsTable();  // Recreate the tables
    }


    /* Shut down DB */
    public void closeConnection() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException se2) {
            se2.printStackTrace();
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }


    /* Create table for all the help items */
    private void createHelpItemTable() throws SQLException {
        String helpItemTableQuery = "CREATE TABLE IF NOT EXISTS helpsystem_helpitems ("
                + "title VARCHAR(255), "
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "description VARCHAR(255), "
                + "short_description VARCHAR(255), "
                + "authors VARCHAR(255), "
                + "keywords VARCHAR(255), "
                + "references VARCHAR(255))";
        statement.execute(helpItemTableQuery);
    }


    /* Get all help items */
    public List<helpItem> getAllHelpItems() throws SQLException {
        String query = "SELECT * FROM helpsystem_helpitems";
        // create list of help items
        List<helpItem> helpItems = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                helpItem helpItem = new helpItem(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("short_description"),
                        rs.getString("authors"),
                        rs.getString("keywords"),
                        rs.getString("references"));
                helpItems.add(helpItem);
            }
        }
        return helpItems;
    }

    /* Add new help item */
    public void addHelpItem(String title, String description, String shortDescription, String author, String keyword, String reference) throws SQLException {
        String query = "INSERT INTO helpsystem_helpitems (title, description, short_description, authors, keywords, references) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, shortDescription);
            pstmt.setString(4, author);
            pstmt.setString(5, keyword);
            pstmt.setString(6, reference);
            pstmt.executeUpdate();
        }
    }

    /* Reset help item database */
    public void resetHelpItemDatabase() throws SQLException {
        String dropHelpItemTableQuery = "DROP TABLE IF EXISTS helpsystem_helpitems";
        statement.execute(dropHelpItemTableQuery);
        createHelpItemTable();  // Recreate the tables
    }

    /* Get help item by title */
    public helpItem getHelpItem(String title) {
        String query = "SELECT * FROM helpsystem_helpitems WHERE title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new helpItem(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("short_description"),
                            rs.getString("authors"),
                            rs.getString("keywords"),
                            rs.getString("references"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* delete item by id */
    public void deleteHelpItem(String title) throws SQLException {
        String query = "DELETE FROM helpsystem_helpitems WHERE title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        }
    }

    /* Update item by id */
    public void updateHelpItem(Integer id, helpItem newItem) throws SQLException {
        String query = "UPDATE helpsystem_helpitems SET title = ?, description = ?, short_description = ?, authors = ?, keywords = ?, references = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newItem.getTitle());
            pstmt.setString(2, newItem.getDescription());
            pstmt.setString(3, newItem.getShortDescription());
            pstmt.setString(4, newItem.getAuthors());
            pstmt.setString(5, newItem.getKeywords());
            pstmt.setString(6, newItem.getReferences());
            pstmt.setInt(7, id);
            pstmt.executeUpdate();
        }
    }


    public void addArticleToGroup(int groupId, int articleId, String content) throws Exception {
        // Convert content to bytes and get IV
        byte[] contentBytes = EncryptionUtils.toByteArray(content.toCharArray());
        byte[] iv = EncryptionUtils.getInitializationVector(content.toCharArray());

        // Encrypt the content using existing encryptionHelper
        byte[] encryptedBytes = encryptionHelper.encrypt(contentBytes, iv);

        // Store both IV and encrypted content in Base64 format
        String encryptedContent = Base64.getEncoder().encodeToString(iv) +
                ":" +
                Base64.getEncoder().encodeToString(encryptedBytes);

        String query = "INSERT INTO group_articles (article_id, group_id, encrypted_content) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, articleId);
            pstmt.setInt(2, groupId);
            pstmt.setString(3, encryptedContent);
            pstmt.executeUpdate();
        }
    }

    public String getGroupArticleContent(int groupId, int articleId, String username) throws Exception {
        // First check if user has permission
        if (!hasViewPermission(groupId, username)) {
            throw new SecurityException("User does not have permission to view this article");
        }

        String query = "SELECT encrypted_content FROM group_articles WHERE group_id = ? AND article_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String encryptedContent = rs.getString("encrypted_content");

                    // Split the stored string to get IV and encrypted content
                    String[] parts = encryptedContent.split(":");
                    byte[] iv = Base64.getDecoder().decode(parts[0]);
                    byte[] encrypted = Base64.getDecoder().decode(parts[1]);

                    // Decrypt using existing encryptionHelper
                    byte[] decryptedBytes = encryptionHelper.decrypt(encrypted, iv);
                    return new String(EncryptionUtils.toCharArray(decryptedBytes));
                }
            }
        }
        return null;
    }

    private boolean hasViewPermission(int groupId, String username) throws SQLException {
        String query = "SELECT 1 FROM group_permissions WHERE group_id = ? AND username = ? "
                + "AND permission_type = 'VIEW'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            pstmt.setString(2, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /* Add admin to special access group */
    public void addAdminToGroup(int groupId, String username) throws SQLException {
        // Check if user already has admin permission
        if (!hasAdminPermission(groupId, username)) {
            addGroupPermission(groupId, username, "ADMIN");
        }
    }

    /* Add viewer to special access group */
    public void addViewerToGroup(int groupId, String username) throws SQLException {
        // Check if user already has view permission
        if (!hasViewPermission(groupId, username)) {
            addGroupPermission(groupId, username, "VIEW");
        }
    }

    /* Remove admin from special access group */
    public void removeAdminFromGroup(int groupId, String username) throws SQLException {
        String query = "DELETE FROM group_permissions WHERE group_id = ? AND username = ? AND permission_type = 'ADMIN'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    /* Remove viewer from special access group */
    public void removeViewerFromGroup(int groupId, String username) throws SQLException {
        String query = "DELETE FROM group_permissions WHERE group_id = ? AND username = ? AND permission_type = 'VIEW'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    /* Check if user has admin permission */
    public boolean hasAdminPermission(int groupId, String username) throws SQLException {
        String query = "SELECT 1 FROM group_permissions WHERE group_id = ? AND username = ? AND permission_type = 'ADMIN'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            pstmt.setString(2, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /* Get all admins for a group */
    public List<String> getGroupAdmins(int groupId) throws SQLException {
        List<String> admins = new ArrayList<>();
        String query = "SELECT username FROM group_permissions WHERE group_id = ? AND permission_type = 'ADMIN'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    admins.add(rs.getString("username"));
                }
            }
        }
        return admins;
    }

    /* Get all viewers for a group */
    public List<String> getGroupViewers(int groupId) throws SQLException {
        List<String> viewers = new ArrayList<>();
        String query = "SELECT username FROM group_permissions WHERE group_id = ? AND permission_type = 'VIEW'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    viewers.add(rs.getString("username"));
                }
            }
        }
        return viewers;
    }

    /* Get all groups where user has admin permission */
    public List<Integer> getUserAdminGroups(String username) throws SQLException {
        List<Integer> groups = new ArrayList<>();
        String query = "SELECT group_id FROM group_permissions WHERE username = ? AND permission_type = 'ADMIN'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(rs.getInt("group_id"));
                }
            }
        }
        return groups;
    }

    /* Get all groups where user has view permission */
    public List<Integer> getUserViewGroups(String username) throws SQLException {
        List<Integer> groups = new ArrayList<>();
        String query = "SELECT group_id FROM group_permissions WHERE username = ? AND permission_type = 'VIEW'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(rs.getInt("group_id"));
                }
            }
        }
        return groups;
    }

    /**
     * Backup encrypted information into a file
     */
    public void backupHelpItemsToFile(String fileName) throws SQLException, IOException, Exception {
        String query = "SELECT * FROM helpsystem_helpitems";

        try (ResultSet resultSet = statement.executeQuery(query);
             FileWriter writer = new FileWriter(fileName)) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String short_description = resultSet.getString("short_description");
                String authors = resultSet.getString("authors");
                String references = resultSet.getString("references");

                writer.write(resultSet.getInt("id") + ",");
                writer.write(title + ",");
                writer.write(description + ",");
                writer.write(short_description + ",");
                writer.write(authors + ",");
                writer.write(references + "\n");
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error backing up articles.");
            e.printStackTrace();
        }
    }

}

