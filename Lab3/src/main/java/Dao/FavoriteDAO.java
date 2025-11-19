package Dao;

import Entity.Favorite;
import java.util.List;

public interface FavoriteDAO {
    Favorite findById(Long id);
    List<Favorite> findAll();
    List<Favorite> findByUser(String userId);
    void insert(Favorite favorite);
    void update(Favorite favorite);
    void delete(Long id);
}