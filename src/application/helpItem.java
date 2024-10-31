package application;

public class helpItem {
    /* Instance Variables */
    private final Integer id;
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

    /* Get title */
    public String getTitle() {
        return title;
    }

    /* Get Description */
    public String getDescription() {
        return description;
    }

    /* Get Short Description */
    public String getShortDescription() {
        return shortDescription;
    }

    /* Get Authors */
    public String getAuthors() {
        return authors;
    }

    /* Get Keywords */
    public String getKeywords() {
        return keywords;
    }

    /* Get References */
    public String getReferences() {
        return references;
    }

    /* Get ID */
    public Integer getId() {
        return id;
    }

    /* Setter Methods */
    public void setTitle(String title) {
        this.title = title;
    }

    /* Set Description */
    public void setDescription(String description) {
        this.description = description;
    }

    /* Set Short Description */
    public void setShortDescription(String shortdescription) {
        this.shortDescription = shortdescription;
    }

    /* Set Authors */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /* Set Keywords */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /* Set References */
    public void setreferences(String references) {
        this.references = references;
    }

    /* Add Methods */
    public void addAuthor(String author) {
        this.authors = author;
    }

    /* Add Keywords */
    public void addKeyword(String keyword) {
        this.keywords = keyword;
    }

    /* Add References */
    public void addreference(String reference) {
        this.references = reference;
    }

    /* Remove Methods */
    public void removeAuthor(String author) {
        this.authors = author;
    }

    /* Remove Keywords */
    public void removeKeyword(String keyword) {
        this.keywords = keyword;
    }

    /* Remove References */
    public void removereference(String reference) {
        this.references = reference;
    }

    /* Delete Item */
    public void deleteItem() {
        this.title = null;
        this.description = null;
        this.shortDescription = null;
        this.authors = null;
        this.keywords = null;
        this.references = null;
    }

    /* Print Method */
    public void print() {
        System.out.println("Title: " + this.title);
        System.out.println("Description: " + this.description);
        System.out.println("Short Description: " + this.shortDescription);
    }


}