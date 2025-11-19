package Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Share")
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VideoId", nullable = false)
    private Video video;

    @Column(name = "Emails", nullable = false, length = 200) // ✅ sửa từ 500 → 200
    private String emails;

    @Temporal(TemporalType.DATE)
    @Column(name = "ShareDate", nullable = false)
    private Date shareDate;

    // Constructors
    public Share() {
        this.shareDate = new Date(); // ✅ mặc định là ngày hiện tại
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Video getVideo() { return video; }
    public void setVideo(Video video) { this.video = video; }

    public String getEmails() { return emails; }
    public void setEmails(String emails) { this.emails = emails; }

    public Date getShareDate() { return shareDate; }
    public void setShareDate(Date shareDate) { this.shareDate = shareDate; }
}