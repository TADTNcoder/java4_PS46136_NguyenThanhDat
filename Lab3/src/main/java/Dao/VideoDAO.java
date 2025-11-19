package Dao;

import Entity.Video;
import java.util.List;

public interface VideoDAO {
    Video findById(String id);
    List<Video> findAll();
    void insert(Video video);
    void update(Video video);
    void delete(String id);
}