package Entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Video")
public class Video {

    @Id
    @Column(name = "Id", length = 50)
    private String id; 

    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "Poster", length = 200)
    private String poster;

    @Column(name = "Views", nullable = false)
    private Integer views = 0;

    @Column(name = "Active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Share> shares = new ArrayList<>();

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<Favorite> getFavorites() { return favorites; }
    public void setFavorites(List<Favorite> favorites) { this.favorites = favorites; }

    public List<Share> getShares() { return shares; }
    public void setShares(List<Share> shares) { this.shares = shares; }
}