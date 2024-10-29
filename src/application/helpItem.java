package application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class helpItem {
    private String title;
    private String uuid;
    private String description;
    private String shortdescription;
    private List<String> authors = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private List<String> refrences = new ArrayList<>();

    /* Default Constructor */
    public helpItem() {
        this.title = "Default Title";
        this.uuid = "Default UUID";
        this.description = "Default Description";
        this.shortdescription = "Default Short Description";
        this.authors.add("Default Author");
        this.keywords.add("Default Keyword");
        this.refrences.add("Default Refrence");
    }

    /* Constructor for creating new Help Item */

    public helpItem(String title, String description, String shortdescription, String author, String keyword, String refrence) {
        this.title = title;
        this.uuid = UUID.randomUUID().toString();
        this.description = description;
        this.shortdescription = shortdescription;
        this.authors.add(author);
        this.keywords.add(keyword);
        this.refrences.add(refrence);
    }

    /* Getter Methods */
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortdescription;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getRefrences() {
        return refrences;
    }

    /* Setter Methods */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShortDescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setRefrences(List<String> refrences) {
        this.refrences = refrences;
    }

    /* Add Methods */
    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
    }

    public void addRefrence(String refrence) {
        this.refrences.add(refrence);
    }

    /* Remove Methods */
    public void removeAuthor(String author) {
        this.authors.remove(author);
    }

    public void removeKeyword(String keyword) {
        this.keywords.remove(keyword);
    }

    public void removeRefrence(String refrence) {
        this.refrences.remove(refrence);
    }

    public void deleteItem() {
        this.title = null;
        this.description = null;
        this.shortdescription = null;
        this.authors = null;
        this.keywords = null;
        this.refrences = null;
    }

    public void print() {
        System.out.println("Title: " + this.title);
        System.out.println("Description: " + this.description);
        System.out.println("Short Description: " + this.shortdescription);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}