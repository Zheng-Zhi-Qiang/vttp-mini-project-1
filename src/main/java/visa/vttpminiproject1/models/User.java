package visa.vttpminiproject1.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class User {
    private String username;

    @NotBlank(message = "Please enter your email!")
    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank(message = "You need to set a password!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
