package Dao;

import java.util.List;
import Entity.User;

public interface UserDAO {
	List<User> findAll();
	  User findById(String id);
	  void create(User entity);
	  void update(User entity);
	  void deleteById(String id);
}
