package eu.esens.espdvcd.designer;

public class UserContext {
    private String username;
    private String firstName;
    private String lastName;
    private String role;

    UserContext(String username, String firstName, String lastName, String role) {
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
