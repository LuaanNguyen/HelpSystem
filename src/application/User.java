package application;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private List<String> roles = new ArrayList<>();

    /* Constructors */
    public User() {
        this.username = "Admin";
        this.password = "123123";
        this.roles.add("Admin");
        this.roles.add("Student");
        this.roles.add("Instructor");
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.roles.add(role);
    }

    /* Setter Methods */
    public void setFirstName(String firstName) {
        this. firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName =  middleName;
    }

    public void setLastName(String lastName) {
        this.lastName  = lastName;
    }

    public void setPreferredFirstName(String preferredFirstName){
        this.preferredFirstName = preferredFirstName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void  setRole(String role) {
        this.roles.add(role);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* Getter Methods */
    public String getFirstName() {return this.firstName;}

    public String getMiddleName() {return this.middleName;}

    public String getLastName() {return this.lastName;}

    public String getPreferredFirstName() {return this.preferredFirstName;}

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {return this.email;}

    public List<String> getRole() {
        return this.roles;
    }

    public void addRole(String role) {
        if (role !=  null && !role.isEmpty() && !roles.contains(role)) {
            System.out.println("Can not add empty role!");
        }
        if (!roles.contains(role)) {
            roles.add(role);
        } else {
            System.out.println("Role already exists with user: " + this.username);
        }
    }
}



