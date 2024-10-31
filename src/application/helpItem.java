package application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class helpItem {
    private String title;
    private String description;
    private String shortDescription;
    private List<String> authors = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private List<String> references = new ArrayList<>();

    /* Default Constructor */
    public helpItem() {
        this.title = "Default Title";
        this.description = "Default Description";
        this.shortDescription = "Default Short Description";
        this.authors.add("Default Author");
        this.keywords.add("Default Keyword");
        this.references.add("Default Reference");
    }

    /* Constructor for creating new Help Item */

    public helpItem(String title,
                    String description,
                    String shortDescription,
                    String author,
                    String keyword,
                    String reference) {
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.authors.add(author);
        this.keywords.add(keyword);
        this.references.add(reference);
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

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getReferences() {
        return references;
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

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setreferences(List<String> references) {
        this.references = references;
    }

    /* Add Methods */
    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
    }

    public void addreference(String reference) {
        this.references.add(reference);
    }

    /* Remove Methods */
    public void removeAuthor(String author) {
        this.authors.remove(author);
    }

    public void removeKeyword(String keyword) {
        this.keywords.remove(keyword);
    }

    public void removereference(String reference) {
        this.references.remove(reference);
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