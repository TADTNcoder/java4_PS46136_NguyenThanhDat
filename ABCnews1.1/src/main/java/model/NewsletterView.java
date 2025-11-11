package model;

import java.sql.Timestamp;

public class NewsletterView {
    private String email;
    private boolean enabled;
    private Timestamp subscribedDate;
    private String authorId;
    private String authorName;

    // ===== Constructors =====
    public NewsletterView() {}

    public NewsletterView(String email, boolean enabled, Timestamp subscribedDate, String authorId, String authorName) {
        this.email = email;
        this.enabled = enabled;
        this.subscribedDate = subscribedDate;
        this.authorId = authorId;
        this.authorName = authorName;
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

    public Timestamp getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(Timestamp subscribedDate) {
        this.subscribedDate = subscribedDate;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "NewsletterView{" +
                "email='" + email + '\'' +
                ", enabled=" + enabled +
                ", subscribedDate=" + subscribedDate +
                ", authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
