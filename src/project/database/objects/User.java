package project.database.objects;

public class User {
    private final String email;
    private final String password;

    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    protected String getPassword() {
        return password;
    }
}
