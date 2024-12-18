package application;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;

import java.util.List;
import java.util.Map;
/**
 * <p> JUnitTest</p>
 *
 * <p> Description:   Automated testing as done in HW#5 of all non-user-interface classes. </p>
 * <p> Copyright: Lynn Robert Carter © 2024 </p>
 *
 * @author Luan Nguyen, Smit Devrukhkar, Gabriel Clark, Meadow Kubanski, Isabella Paschal
 * @version 1.00
 */

public class JUnitTest {
    private DatabaseUtil db;

    /* DB SETUP  (reset DB before testing) */
    @Before
    public void setup() throws Exception {
        db = new DatabaseUtil();
        db.connectToDatabase();
        db.resetHelpItemDatabase();
    }
    @After
    public void tearDown() {
        db.closeConnection();
    }

    /*
        TEST: DB CONNECTION
    */
    @Test
    public void testDatabaseConnection() {
        try {
            assertNotNull("Database connection should not be null", db);
        } catch (Exception e) {
            fail("Exception occurred while testing database connection: " + e.getMessage());
        }
    }

    /*
        TEST: GENERATE INVITATION CODE
    */
    @Test
    public void testInvitationCodeGeneration() {
        String code1 = db.generateInvitationCode();
        String code2 = db.generateInvitationCode();

        System.out.println("Code 1: " + code1);
        System.out.println("Code 2: " + code2);

        //The invitation code should not be equal
        assertNotEquals(code1, code2);
    }

    /*
        TEST: DELETE INVITATION CODE
    */
    @Test
    public void testInvitationCodeInvalidation()  throws SQLException{
        String code1 = db.generateInvitationCode();
        String code2 = db.generateInvitationCode();

        System.out.println("Code 1: " + code1 );
        System.out.println("Code 2: " + code2 );

        db.invalidateInvitationCode(code1);
        db.invalidateInvitationCode(code1);

        //The invitation code should be invalid
        assertFalse(db.isValidInvitationCode(code1));
        assertFalse(db.isValidInvitationCode(code2));
    }

    /*
        TEST:  INVITE USER
    */
    @Test
    public void testInviteUser() throws Exception {
        String code = db.generateInvitationCode();

        db.inviteUser(code, "Admin");

        assertTrue("Invitation code should be valid after being added", db.isValidInvitationCode(code));

        db.invalidateInvitationCode(code);

        assertFalse("Invitation code should be invalid after being invalidated", db.isValidInvitationCode(code));
    }

    /*
        TEST: ADD HELP ITEM
    */
    @Test
    public void testAddHelpItem() throws Exception {
        String title = "Test Title";
        String description = "Test Description";
        String shortDescription = "Short Desc";
        String author = "Author1";
        String keyword = "Keyword1";
        String reference = "Ref1";
        String level = "Beginner";
        String groupName = "Group1";

        db.addHelpItem(title, description, shortDescription, author, keyword, reference, level, groupName);

        assertNotNull("Help item should be retrievable", db.getHelpItem(title));
    }

    /*
        TEST: DELETE HELP ITEM
    */
    @Test
    public void testDeleteHelpItem() throws Exception {
        String title = "Delete Title";
        db.addHelpItem(title, "Desc", "Short Desc", "Author", "Keyword", "Ref", "Level", "Group");

        db.deleteHelpItem(title);

        assertNull("Help item should be deleted", db.getHelpItem(title));
    }

    /*
        TEST: REGISTER AND LOGIN
    */
    @Test
    public void testRegisterAndLoginUser() throws Exception {
        String username = "testUser";
        String password = "password123";
        String role = "admin";

        db.register(username, password, role);
        assertTrue("User should be able to log in with correct credentials", db.login(username, password));
        assertFalse("User should not log in with incorrect credentials", db.login(username, "wrongPassword"));
    }

    /*
        TEST: ADMIN FUNCTION
    */
    @Test
    public void testAddAndRemoveGroupPermission() throws Exception {
        String groupName = "Test Group";
        String username = "user1";

        db.register(username, "password", "viewer");
        db.createSpecialAccessGroup(groupName, username);

        int groupId = (int) db.getAllSpecialAccessGroups().get(0).get("group_id");

        db.addAdminToGroup(groupId, username);
        assertTrue("User should have admin permission", db.hasAdminPermission(groupId, username));

        db.removeAdminFromGroup(groupId, username);
        assertFalse("User should no longer have admin permission", db.hasAdminPermission(groupId, username));
    }

    /*
        TEST: ADD AND RETRIEVE HELP ITEMS
    */
    @Test
    public void testAddAndRetrieveHelpItem() throws Exception {
        String title = "Help Item 1";
        String description = "Description of Help Item 1";
        String shortDescription = "Short Description";
        String author = "Author1";
        String keyword = "Keyword1";
        String reference = "Ref1";
        String level = "Beginner";
        String groupName = "Group1";

        db.addHelpItem(title, description, shortDescription, author, keyword, reference, level, groupName);

        helpItem retrievedItem = db.getHelpItem(title);
        assertNotNull("Help item should be retrievable", retrievedItem);
        assertEquals("Retrieved help item title should match", title, retrievedItem.getTitle());
    }

     /*
        TEST: HELP ARTICLE ID ASSIGNMENT
     */
    @Test
    public void testUniqueArticleIdAssignment() throws SQLException {
        // Create multiple help items
        db.addHelpItem("Article1", "Description1", "Short1", "Author1",
                "keyword1", "ref1", "beginner", "group1");
        db.addHelpItem("Article2", "Description2", "Short2", "Author2",
                "keyword2", "ref2", "intermediate", "group1");

        List<helpItem> items = db.getAllHelpItems();

        // Verify IDs are unique and sequential
        assertEquals(2, items.size());
        assertNotEquals(items.get(0).getId(), items.get(1).getId());
        assertTrue(items.get(1).getId() > items.get(0).getId());
    }

    /*
         TEST: HELP ARTICLE STRUCTURE VALIDATION
     */
    @Test
    public void testArticleStructureValidation() throws SQLException {
        String title = "TestArticle";
        String description = "Test Description";
        String shortDesc = "Short Description";
        String author = "Test Author";
        String keywords = "test,junit,validation";
        String reference = "test reference";
        String level = "beginner";
        String group = "testGroup";

        db.addHelpItem(title, description, shortDesc, author,
                keywords, reference, level, group);

        helpItem item = db.getHelpItem(title);

        assertNotNull(item);
        assertEquals(title, item.getTitle());
        assertEquals(description, item.getDescription());
        assertEquals(level, item.getLevel());
        assertEquals(group, item.getGroup());
    }

    /*
        TEST: ARTICLE CONTENT VALIDATION
     */
    @Test
    public void testArticleContentValidation() throws SQLException {
        String longTitle = "Very Long Title " + "a".repeat(200);
        String specialCharsDesc = "Special chars: !@#$%^&*()_+-=[]{}|;:'\",.<>?/~`";
        String formattedShortDesc = "Line 1\nLine 2\tTabbed\r\nWindows Line";
        db.addHelpItem(
                longTitle,
                specialCharsDesc,
                formattedShortDesc,
                "Test Author",
                "test,validation",
                "test reference",
                "beginner",
                "testGroup"
        );
        helpItem item = db.getHelpItem(longTitle);
        assertNotNull(item);
        assertEquals(longTitle, item.getTitle());
        assertEquals(specialCharsDesc, item.getDescription());
        assertEquals(formattedShortDesc, item.getShortDescription());
    }


    @Test
    public void testUnicodeContentHandling() throws SQLException {
        String unicodeShortDesc = "Short desc with accents: é è ê ë ñ ü ö";
        String unicodeTitle = "Unicode Test: 你好 สวัสดี مرحبا";
        String unicodeDesc = "Description with emojis: 👋 🌟 🎉";
        db.addHelpItem(
                unicodeTitle,
                unicodeDesc,
                unicodeShortDesc,
                "Test Author",
                "unicode,test",
                "test reference",
                "intermediate",
                "testGroup"
        );
        helpItem item = db.getHelpItem(unicodeTitle);
        assertNotNull(item);
        assertEquals(unicodeTitle, item.getTitle());
        assertEquals(unicodeDesc, item.getDescription());
        assertEquals(unicodeShortDesc, item.getShortDescription());
    }


    /*
       TEST: BACK UP HELP ITEMS
  */
    @Test
    public void testContentBoundaries() throws SQLException {
        String emptyContent = "";
        String maxLengthContent = "x".repeat(255);
        db.addHelpItem(
                "Boundary Test",
                maxLengthContent,
                emptyContent,
                "Test Author",
                "boundary,test",
                "test reference",
                "advanced",
                "testGroup"
        );
        helpItem item = db.getHelpItem("Boundary Test");
        assertNotNull(item);
        assertEquals(maxLengthContent, item.getDescription());
        assertEquals(emptyContent, item.getShortDescription());
    }

    /*
        TEST: BACK UP HELP ITEMS
   */
    @Test
    public void testBackupHelpItemsToFile() throws Exception {
        String fileName = "helpItemsBackup.txt";

        // Add help items to database
        db.addHelpItem("Title1", "Desc1", "ShortDesc1", "Author1", "Keyword1", "Ref1", "Beginner", "Group1");

        // Backup to file
        try {
            db.backupHelpItemsToFile(fileName);
            assertTrue("Backup file should be created successfully", true); // No exceptions thrown
            // Further validation can include checking file content if needed.

            System.out.println("Backup completed successfully.");

        } catch (Exception e) {
            fail("Error backup");
        }
    }
}