package Dao;

import Entity.Share;
import java.util.List;

public interface ShareDAO {
    Share findById(Long id);
    List<Share> findAll();
    List<Share> findByUser(String userId);
    void insert(Share share);
    void update(Share share);
    void delete(Long id);
}