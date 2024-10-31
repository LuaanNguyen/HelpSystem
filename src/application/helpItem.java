package application;

public class helpItem {
    private Integer id;
    private String title;
    private String description;
    private String shortDescription;
    private String authors;
    private String keywords;
    private String references;

    /* Default Constructor */
    public helpItem() {
        this.id = 0;
        this.title = "Default Title";
        this.description = "Default Description";
        this.shortDescription = "Default Short Description";
        this.authors = "Default Author";
        this.keywords = "Default Keyword";
        this.references = "Default Reference";
    }

    /* Constructor for creating new Help Item */

    public helpItem(Integer id,
                    String title,
                    String description,
                    String shortDescription,
                    String author,
                    String keyword,
                    String reference) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.authors = author;
        this.keywords = keyword;
        this.references = reference;
    }

    /* Getter Methods */
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getAuthors() {
        return authors;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getReferences() {
        return references;
    }

    public Integer getId() {
        return id;
    }

    /* Setter Methods */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShortDescription(String shortdescription) {
        this.shortDescription = shortdescription;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setreferences(String references) {
        this.references = references;
    }

    /* Add Methods */
    public void addAuthor(String author) {
        this.authors = author;
    }

    public void addKeyword(String keyword) {
        this.keywords = keyword;
    }

    public void addreference(String reference) {
        this.references = reference;
    }

    /* Remove Methods */
    public void removeAuthor(String author) {
        this.authors = author;
    }

    public void removeKeyword(String keyword) {
        this.keywords = keyword;
    }

    public void removereference(String reference) {
        this.references = reference;
    }

    public void deleteItem() {
        this.title = null;
        this.description = null;
        this.shortDescription = null;
        this.authors = null;
        this.keywords = null;
        this.references = null;
    }

    public void print() {
        System.out.println("Title: " + this.title);
        System.out.println("Description: " + this.description);
        System.out.println("Short Description: " + this.shortDescription);
    }


}