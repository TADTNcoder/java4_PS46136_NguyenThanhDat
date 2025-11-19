package Entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Users", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_User_Email", columnNames = "Email")
})
public class User {

    @Id
    @Column(name = "Id", length = 50)
    private String id; // ✅ không dùng @GeneratedValue vì SQL không tự tăng

    @Column(name = "Fullname", nullable = false, length = 100)
    private String fullname;

    @Column(name = "Password", nullable = false, length = 100)
    private String password;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "Admin", nullable = false)
    private Boolean admin = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Share> shares = new ArrayList<>();

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getAdmin() { return admin; }
    public void setAdmin(Boolean admin) { this.admin = admin; }

    public List<Favorite> getFavorites() { return favorites; }
    public void setFavorites(List<Favorite> favorites) { this.favorites = favorites; }

    public List<Share> getShares() { return shares; }
    public void setShares(List<Share> shares) { this.shares = shares; }
}