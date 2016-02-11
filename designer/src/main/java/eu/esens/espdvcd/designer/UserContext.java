package eu.esens.espdvcd.designer;

/**
 * Created by ixuz on 2/11/16.
 */
public class UserContext {
    private String username;
    private String firstName;
    private String lastName;

    UserContext(String username, String firstName, String lastName) {
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
