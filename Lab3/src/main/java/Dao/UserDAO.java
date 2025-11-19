package Dao;

import Entity.User;
import java.util.List;

public interface UserDAO {
    User findById(String id);
    List<User> findAll();
    void insert(User user);
    void update(User user);
    void delete(String id);
}