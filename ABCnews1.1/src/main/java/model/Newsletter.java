package model;

import java.sql.Timestamp;

public class Newsletter {
    private String email;              // 📧 Email người đăng ký
    private boolean enabled;           // ✅ Trạng thái: true = đang theo dõi, false = đã hủy
    private String authorId;           // ✍️ ID tác giả mà người này theo dõi
    private Timestamp subscribedDate;  // 📅 Ngày đăng ký nhận tin

    // ===== Constructors =====
    public Newsletter() {}

    // Dùng khi chỉ cần email + trạng thái (ví dụ khi đăng ký)
    public Newsletter(String email, boolean enabled) {
        this.email = email;
        this.enabled = enabled;
    }

    // Dùng khi tạo bản ghi đầy đủ
    public Newsletter(String email, boolean enabled, String authorId, Timestamp subscribedDate) {
        this.email = email;
        this.enabled = enabled;
        this.authorId = authorId;
        this.subscribedDate = subscribedDate;
    }

    // ===== Getters & Setters =====
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Timestamp getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(Timestamp subscribedDate) {
        this.subscribedDate = subscribedDate;
    }

    // ===== Utility =====
    @Override
    public String toString() {
        return "Newsletter{" +
                "email='" + email + '\'' +
                ", enabled=" + enabled +
                ", authorId='" + authorId + '\'' +
                ", subscribedDate=" + subscribedDate +
                '}';
    }
}
