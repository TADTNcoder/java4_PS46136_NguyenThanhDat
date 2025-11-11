package entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Lớp thực thể (Entity) ánh xạ với bảng Users trong CSDL PolyOE
 * Chạy được với JDK 21, Tomcat 9, Hibernate 5.6 (javax.persistence)
 */
@Entity
@Table(name = "Users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // ====== Thuộc tính ánh xạ cột trong bảng Users ======
    @Id
    @Column(name = "Id", nullable = false, length = 20)
    private String id;

    @Column(name = "Password", nullable = false, length = 50)
    private String password;

    @Column(name = "Fullname", nullable = false, length = 50)
    private String fullname;

    @Column(name = "Email", nullable = false, length = 50)
    private String email;

    @Column(name = "Admin", nullable = false)
    private boolean admin = false;

    // ====== Constructors ======
    public User() {
        // Bắt buộc có constructor rỗng để Hibernate khởi tạo đối tượng
    }

    public User(String id, String password, String email, String fullname, boolean admin) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.admin = admin;
    }

    // ====== Getter & Setter ======
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    // ====== Phương thức hỗ trợ (hiển thị đối tượng) ======
    @Override
    public String toString() {
        return String.format("User[id=%s, fullname=%s, email=%s, admin=%s]",
                id, fullname, email, admin);
    }
}
