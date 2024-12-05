package application;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;

public class JUnitTest {
    private DatabaseUtil db;

    @Before
    public void setup() throws Exception {
        db = new DatabaseUtil();
        db.connectToDatabase();
        db.resetHelpItemDatabase();
    }
     /*
        HELP ARTICLE ID ASSIGNMENT
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
    HELP ARTICLE STRUCTURE VALIDATION
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
    TEST ARTICLE CONTENT VALIDATION
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
}