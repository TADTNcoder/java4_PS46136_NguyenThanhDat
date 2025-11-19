package Entity;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    private String Id;
    private String Password;
    private String Fullname;
    private String Email;
    private Boolean Admin;

    public User() {}

    public User(String id, String password, String fullname, String email, Boolean admin) {
        this.Id = id;
        this.Password = password;
        this.Fullname = fullname;
        this.Email = email;
        this.Admin = admin;
    }

    // GETTER & SETTER
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        this.Fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        this.Admin = admin;
    }
}
