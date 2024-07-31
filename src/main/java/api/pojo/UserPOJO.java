package api.pojo;

public class UserPOJO {
    private String email;
    private String password;
    private String name;

    public UserPOJO(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserPOJO() {

    }

    public UserPOJO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
