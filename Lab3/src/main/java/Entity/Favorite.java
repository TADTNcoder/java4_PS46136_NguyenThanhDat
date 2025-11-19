package Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Favorite", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_Favorite_User_Video", columnNames = {"UserId", "VideoId"})
})
public class Favorite {

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

    @Temporal(TemporalType.DATE)
    @Column(name = "LikeDate", nullable = false)
    private Date likeDate;

    // Constructors
    public Favorite() {
        this.likeDate = new Date(); // mặc định là ngày hiện tại
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Video getVideo() { return video; }
    public void setVideo(Video video) { this.video = video; }

    public Date getLikeDate() { return likeDate; }
    public void setLikeDate(Date likeDate) { this.likeDate = likeDate; }
}